package de.nicoismaili.qontract.data.contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

public class ContractConverters {

    @TypeConverter
    public static List<String> fromJson(String jsonString) {
        if (jsonString != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, new TypeToken<List<Contract>>() {
            }.getType());
        }
        return null;
    }

    @TypeConverter
    public static String fromList(List<String> images) {
        if (images != null) {
            Gson gson = new Gson();
            return gson.toJson(images);
        }
        return null;
    }

    @TypeConverter
    public static String fromBitmapToString(Bitmap bm) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return "";
    }

    @TypeConverter
    public static Bitmap fromStringToBitmap(String encodedImage) {
        if (!encodedImage.isEmpty()) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }

}
