package de.nicoismaili.qontract;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditContractActivity extends AppCompatActivity {

    public static final String FIRSTNAME = "FIRSTNAME";
    public static final String LASTNAME = "LASTNAME";
    private EditText EditFirstNameView;
    private EditText EditLastNameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contract);
        EditFirstNameView = findViewById(R.id.first_name_input);
        EditLastNameView = findViewById(R.id.lastname_input);
        final Button button = findViewById(R.id.submit_btn);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(EditFirstNameView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String firstname = EditFirstNameView.getText().toString();
                replyIntent.putExtra(FIRSTNAME, firstname);
                String lastname = EditLastNameView.getText().toString();
                replyIntent.putExtra(LASTNAME, lastname);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

}