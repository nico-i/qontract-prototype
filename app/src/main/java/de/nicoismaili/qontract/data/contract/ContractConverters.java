package de.nicoismaili.qontract.data.contract;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

public class ContractConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

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

}
