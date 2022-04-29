package de.nicoismaili.qontract;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import de.nicoismaili.qontract.data.ContractsRepository;

public class MainActivity extends AppCompatActivity {

    private ContractsRepository contractsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contractsRepository = new ContractsRepository(this);
        setContentView(R.layout.activity_main);
    }
}