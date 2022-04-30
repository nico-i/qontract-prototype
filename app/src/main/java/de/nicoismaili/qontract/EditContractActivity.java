package de.nicoismaili.qontract;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditContractActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.contractlistsql.REPLY";

    private EditText mEditContractView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contract);
        mEditContractView = findViewById(R.id.edit_word);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditContractView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String firstName = mEditContractView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, firstName);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

}