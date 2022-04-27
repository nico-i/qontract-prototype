package de.nicoismaili.qontract.data.contracts;

import android.provider.BaseColumns;

public class SettingsContract {
    private SettingsContract() {
    }
    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "models";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_MODEL_MODE = "model_mode";
    }
}
