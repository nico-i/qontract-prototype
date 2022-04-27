package de.nicoismaili.qontract.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.nicoismaili.qontract.data.contracts.ContractContract;
import de.nicoismaili.qontract.data.contracts.ImageContract;
import de.nicoismaili.qontract.data.contracts.SettingsContract;
import de.nicoismaili.qontract.data.models.Contract;
import de.nicoismaili.qontract.data.models.Model;
import de.nicoismaili.qontract.data.models.Settings;
import de.nicoismaili.qontract.utils.BitmapUtils;

public class ContractsRepository extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "qontract.db";

    private SQLiteDatabase database;

    public ContractsRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.database = this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTables());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL(dropTables());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String createTables() {
        // Create contracts table
        String createContractsTable = "CREATE TABLE " +
                ContractContract.ContractEntry.TABLE_NAME + " (" +
                ContractContract.ContractEntry._ID + " INTEGER PRIMARY KEY, " +
                ContractContract.ContractEntry.COLUMN_NAME_DATE + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_LOCATION + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_SIGNED + " INTEGER, " +
                ContractContract.ContractEntry.COLUMN_NAME_READ + " INTEGER, " +
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_FIRSTNAME + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_LASTNAME + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_ADDRESS + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_PHONE + " TEXT," +
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_EMAIL + " TEXT);";
        String createSettingsTable = "CREATE TABLE " +
                SettingsContract.SettingsEntry.TABLE_NAME + " (" +
                SettingsContract.SettingsEntry._ID + " INTEGER PRIMARY KEY, " +
                SettingsContract.SettingsEntry.COLUMN_NAME_FIRSTNAME + " TEXT, " +
                SettingsContract.SettingsEntry.COLUMN_NAME_LASTNAME + " TEXT, " +
                SettingsContract.SettingsEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                SettingsContract.SettingsEntry.COLUMN_NAME_EMAIL + " TEXT," +
                SettingsContract.SettingsEntry.COLUMN_NAME_PHONE + " TEXT," +
                SettingsContract.SettingsEntry.COLUMN_NAME_MODEL_MODE + " INTEGER);";
        String createImagesTable = "CREATE TABLE " +
                SettingsContract.SettingsEntry.TABLE_NAME + " (" +
                ImageContract.ImageEntry._ID + " INTEGER PRIMARY KEY, " +
                ImageContract.ImageEntry.COLUMN_NAME_TYPE + " TEXT, " +
                ImageContract.ImageEntry.COLUMN_NAME_CONTRACT_ID + " INTEGER, " +
                ImageContract.ImageEntry.COLUMN_NAME_BASE64 + " TEXT);";
        return createContractsTable + createSettingsTable + createImagesTable;
    }

    /**
     * Creates a SQLite command string to delete all tables in the database.
     *
     * @return SQLite command as string
     */
    private String dropTables() {
        String dropContractsTable = "DROP TABLE IF EXISTS " +
                ContractContract.ContractEntry.TABLE_NAME + ";";
        String dropSettingsTable = "DROP TABLE IF EXISTS " +
                SettingsContract.SettingsEntry.TABLE_NAME + ";";
        String dropImagesTable = "DROP TABLE IF EXISTS " +
                ImageContract.ImageEntry.TABLE_NAME + ";";
        return dropContractsTable + dropImagesTable + dropSettingsTable;
    }

    /**
     * Gets all contracts in the 'stripped' format from database.
     * This means that the returned list will contain only contract
     * objects with minimal data to improve performance when querying
     * the database.
     *
     * @return an ArrayList containing all existing contracts
     */
    public ArrayList<Contract> getAllStrippedContracts() throws ParseException {
        String[] projection = {
                BaseColumns._ID,
                ContractContract.ContractEntry.COLUMN_NAME_SIGNED,
                ContractContract.ContractEntry.COLUMN_NAME_READ,
                ContractContract.ContractEntry.COLUMN_NAME_DATE,
                ContractContract.ContractEntry.COLUMN_NAME_LOCATION,
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_FIRSTNAME,
                ContractContract.ContractEntry.COLUMN_NAME_MODEL_LASTNAME

        };
        ArrayList<Contract> contracts = new ArrayList<>();
        // How you want the results sorted in the resulting Cursor
        String sortOrder = ContractContract.ContractEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = database.query(
                ContractContract.ContractEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        while (cursor.moveToNext()) {
            long id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            boolean isSigned = cursor.getInt(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_SIGNED)) == 1;
            boolean isRead = cursor.getInt(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_READ)) == 1;
            @SuppressLint("SimpleDateFormat") // This is suppressed because the string will always be in ISO format
            Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(cursor.getString(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_DATE)));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_LOCATION));
            String modelFirstName = cursor.getString(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_FIRSTNAME));
            String modelLastName = cursor.getString(cursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_LASTNAME));
            Model contractModel = new Model(modelFirstName, modelLastName);
            Contract contract = new Contract(id, isSigned, isRead, date, location, contractModel);
            contracts.add(contract);
        }
        cursor.close();
        return contracts;
    }

    /**
     * Inserts a contract into the 'contracts' table and the
     * contained images into the 'images' table with a reference
     * to their original contract.
     *
     * @param contract the object to be saved to the database
     * @return the id of the saved item
     */
    public long insertContract(Contract contract) {
        // Insert contract into contracts database
        ContentValues contractValues = new ContentValues();
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_SIGNED, contract.isSigned() ? 1 : 0);
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_READ, contract.isRead() ? 1 : 0);
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_DATE, contract.getDate().toString());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_LOCATION, contract.getLocation());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_MODEL_FIRSTNAME, contract.getModelDetails().getFirstname());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_MODEL_LASTNAME, contract.getModelDetails().getLastname());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_MODEL_ADDRESS, contract.getModelDetails().getAddress());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_MODEL_PHONE, contract.getModelDetails().getPhone());
        contractValues.put(ContractContract.ContractEntry.COLUMN_NAME_MODEL_EMAIL, contract.getModelDetails().getEmail());
        long contractId = database.insert(ContractContract.ContractEntry.TABLE_NAME, null,
                contractValues);
        // Insert contract's images into images database
        for (Bitmap base64Image : contract.getImages()) {
            ContentValues imageValues = new ContentValues();
            imageValues.put(ImageContract.ImageEntry.COLUMN_NAME_CONTRACT_ID, contractId);
            imageValues.put(ImageContract.ImageEntry.COLUMN_NAME_TYPE, Contract.IMAGE_TYPE_PHOTO);
            imageValues.put(ImageContract.ImageEntry.COLUMN_NAME_BASE64, BitmapUtils.base64ToBitmap(base64Image));
            database.insert(ImageContract.ImageEntry.TABLE_NAME, null,
                    imageValues);
        }
        // Insert contract's qr code into images database
        ContentValues qrValues = new ContentValues();
        qrValues.put(ImageContract.ImageEntry.COLUMN_NAME_CONTRACT_ID, contractId);
        qrValues.put(ImageContract.ImageEntry.COLUMN_NAME_TYPE, Contract.IMAGE_TYPE_QR);
        qrValues.put(ImageContract.ImageEntry.COLUMN_NAME_BASE64, BitmapUtils.base64ToBitmap(contract.getQrCode()));
        database.insert(ImageContract.ImageEntry.TABLE_NAME, null,
                qrValues);
        // Insert contract's model signature into images database
        ContentValues signatureValues = new ContentValues();
        signatureValues.put(ImageContract.ImageEntry.COLUMN_NAME_CONTRACT_ID, contractId);
        signatureValues.put(ImageContract.ImageEntry.COLUMN_NAME_TYPE, Contract.IMAGE_TYPE_SIGNATURE);
        signatureValues.put(ImageContract.ImageEntry.COLUMN_NAME_BASE64, BitmapUtils.base64ToBitmap(contract.getModelSignature()));
        database.insert(ImageContract.ImageEntry.TABLE_NAME, null,
                signatureValues);

        return contractId;
    }

    /**
     * Update a contract already existent in the data
     *
     * @param id          id of the contract to be deleted
     * @param newContract Contract instance with which to update the database entry
     * @return id of the updated contract
     */
    public long updateContractById(long id, Contract newContract) {
        boolean isDeleted = deleteContractById(id);
        if (!isDeleted) {
            throw new SQLiteException("The given contract id does not exist. Deletion aborted.");
        }
        return insertContract(newContract);
    }

    /**
     * Gets a contract from the database by its id
     *
     * @param id id of the contract to get
     * @return a contract instance
     * @throws ParseException when datetime format is wrong
     */
    public Contract getContractById(long id) throws ParseException {
        String[] selectionArgs = {Long.toString(id)};
        Cursor contractCursor = database.query(
                ContractContract.ContractEntry.TABLE_NAME, // The table to query
                null, // The array of columns to return (pass null to get all)
                BaseColumns._ID + " = ?", // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        long contract_id =
                contractCursor.getLong(contractCursor.getColumnIndexOrThrow(BaseColumns._ID));
        boolean isSigned = contractCursor.getInt(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_SIGNED)) == 1;
        boolean isRead = contractCursor.getInt(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_READ)) == 1;
        @SuppressLint("SimpleDateFormat") // This is suppressed because the string will always be in ISO format
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_DATE)));
        String location = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_LOCATION));
        String modelFirstName = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_FIRSTNAME));
        String modelLastName = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_LASTNAME));
        String modelAddress = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_ADDRESS));
        String modelPhone = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_PHONE));
        String modelEmail = contractCursor.getString(contractCursor.getColumnIndexOrThrow(ContractContract.ContractEntry.COLUMN_NAME_MODEL_EMAIL));
        Model contractModel = new Model(modelFirstName, modelLastName, modelAddress, modelPhone, modelEmail);
        contractCursor.close();
        // Get related images
        Cursor imagesCursor = database.query(
                ImageContract.ImageEntry.TABLE_NAME, // The table to query
                null, // The array of columns to return (pass null to get all)
                BaseColumns._ID + " = ?", // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        Contract contract = new Contract(contract_id, isSigned, isRead, date, location, contractModel);
        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap signature = null;
        Bitmap qr = null;
        while (imagesCursor.moveToNext()) {
            String type = imagesCursor.getString(imagesCursor.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_TYPE));
            String base64String = imagesCursor.getString(imagesCursor.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_BASE64));
            switch (type) {
                case Contract.IMAGE_TYPE_PHOTO:
                    images.add(BitmapUtils.base64ToBitmap(base64String));
                    break;
                case Contract.IMAGE_TYPE_QR:
                    qr = BitmapUtils.base64ToBitmap(base64String);
                    break;
                case Contract.IMAGE_TYPE_SIGNATURE:
                    signature = BitmapUtils.base64ToBitmap(base64String);
                    break;
            }
        }
        imagesCursor.close();
        contract.setImages(images);
        contract.setQrCode(qr);
        contract.setModelSignature(signature);
        return contract;
    }

    /**
     * Deletes a contract from the database via its ID.
     *
     * @param id id of the contract to be deleted
     * @return true if deletion was successful otherwise returns false
     */
    public boolean deleteContractById(long id) {
        String[] selectionArgs = {Long.toString(id)};
        return database.delete(
                ContractContract.ContractEntry.TABLE_NAME, // The table to query
                BaseColumns._ID + " = ?", // The columns for the WHERE clause
                selectionArgs// The values for the WHERE clause
        ) == 1;
    }


    public long insertSettings(Settings settings) {
        return 0;
    }

    public long updateSettingsById(long id) {
        return 0;
    }

    public Contract getSettingsById(long id) {
        return null;
    }
}
