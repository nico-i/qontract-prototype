package de.nicoismaili.qontract.data.contracts;

import android.provider.BaseColumns;

public class ContractContract {
    private ContractContract() {
    }

    public static class ContractEntry implements BaseColumns {
        public static final String TABLE_NAME = "contracts";
        public static final String COLUMN_NAME_SIGNED = "signed";
        public static final String COLUMN_NAME_READ = "read";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_MODEL_FIRSTNAME = "model_firstname";
        public static final String COLUMN_NAME_MODEL_LASTNAME = "model_lastname";
        public static final String COLUMN_NAME_MODEL_ADDRESS = "model_address";
        public static final String COLUMN_NAME_MODEL_PHONE = "model_phone";
        public static final String COLUMN_NAME_MODEL_EMAIL = "model_email";
    }
}
