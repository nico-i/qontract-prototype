package de.nicoismaili.qontract.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class BitmapUtils {
    private BitmapUtils() {
    }

    /**
     * Converts bitmap to byte array in PNG format
     *
     * @param bitmap source bitmap
     * @return result byte array
     */
    private static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e(BitmapUtils.class.getSimpleName(), "ByteArrayOutputStream was not closed");
                }
            }
        }
    }

    /**
     * Converts bitmap to base64 string
     *
     * @param image the image to be converted
     * @return base64 string of the inputted image
     */
    public static String base64ToBitmap(Bitmap image) {
        return Base64.encodeToString(convertBitmapToByteArray(image), Base64.DEFAULT);
    }

    /**
     * Converts base64 string to Bitmap
     *
     * @param base64String the string to be converted
     * @return resulting bitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return convertCompressedByteArrayToBitmap(decodedString);
    }

    /**
     * Converts compressed byte array to bitmap
     *
     * @param src source array
     * @return result bitmap
     */
    private static Bitmap convertCompressedByteArrayToBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }
}
