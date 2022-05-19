package de.nicoismaili.qontract.data.contract;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import java.io.ByteArrayOutputStream;
import java.util.List;

/** Converters for the ROOM API. */
public class ContractConverters {

  /**
   * Converts a JSON String to a List of Strings.
   *
   * @param jsonString JSON String
   * @return The List of Strings
   */
  @TypeConverter
  public static List<String> fromJson(String jsonString) {
    if (jsonString != null) {
      Gson gson = new Gson();
      return gson.fromJson(jsonString, new TypeToken<List<Contract>>() {}.getType());
    }
    return null;
  }

  /**
   * Converts a List of Strings to a JSON String.
   *
   * @param images List of Strings
   * @return A JSON String
   */
  @TypeConverter
  public static String fromList(List<String> images) {
    if (images != null) {
      Gson gson = new Gson();
      return gson.toJson(images);
    }
    return null;
  }

  /**
   * Takes a Bitmap and converts it to {@link Base64}.
   *
   * @param bm The {@link Bitmap} to be converted
   * @return A Base64 String
   */
  @TypeConverter
  public static String fromBitmapToString(Bitmap bm) {
    if (bm != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap object
      byte[] b = baos.toByteArray();
      return Base64.encodeToString(b, Base64.DEFAULT);
    }
    return "";
  }

  /**
   * Takes a {@link Base64} String and converts it to a {@link Bitmap}.
   *
   * @param encodedImage A Base64 String
   * @return The converted Bitmap
   */
  @TypeConverter
  public static Bitmap fromStringToBitmap(String encodedImage) {
    if (!encodedImage.isEmpty()) {
      byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    return null;
  }
}
