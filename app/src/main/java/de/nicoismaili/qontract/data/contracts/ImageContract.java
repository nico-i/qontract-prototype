package de.nicoismaili.qontract.data.contracts;

import android.provider.BaseColumns;

public class ImageContract {
    private ImageContract() {
    }

    public static class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_CONTRACT_ID = "contract_id";
        public static final String COLUMN_NAME_BASE64 = "base64";
    }
}
