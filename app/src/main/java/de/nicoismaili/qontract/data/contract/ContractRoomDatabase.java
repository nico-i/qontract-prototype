package de.nicoismaili.qontract.data.contract;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

//TODO Setup database migrations (exportSchema = true) -> https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
@Database(entities = {Contract.class}, version = 1, exportSchema = false)
public abstract class ContractRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile ContractRoomDatabase INSTANCE;
    private final static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ContractDAO dao = INSTANCE.contractDAO();
                dao.deleteAll();

                Contract contract = new Contract();
                contract.setModelFirstname("Nico");
                contract.setModelLastname("Ismaili");
                contract.setSigned(false);
                contract.setDate(new Date().getTime());
                contract.setLocation("Location2");
                dao.insertContract(contract);
                contract = new Contract();
                contract.setModelFirstname("Josi");
                contract.setModelLastname("Paris");
                contract.setDate(new Date().getTime());
                contract.setLocation("Location1");
                contract.setSigned(true);
                dao.insertContract(contract);
            });
        }
    };

    public static ContractRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContractRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContractRoomDatabase.class, "contracts_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ContractDAO contractDAO();
}
