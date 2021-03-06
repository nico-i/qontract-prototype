package de.nicoismaili.qontract.data.contract.pojo;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import de.nicoismaili.qontract.data.contract.ContractConverters;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Contract POJO (and ROOM {@link Entity}) containing all necessary information to model a contract.
 */
@Entity(tableName = "contracts")
@TypeConverters({ContractConverters.class})
public class Contract implements Serializable {
  @Ignore public static final String DATE_STRING_FORMAT = "dd.MM.yyyy";

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "contract_id")
  private int id;

  @ColumnInfo(name = "read")
  private boolean isRead;

  private long dateLong;
  @NonNull private String location;

  @NonNull
  @ColumnInfo(name = "model_first_name")
  private String modelFirstname;

  @NonNull
  @ColumnInfo(name = "model_last_name")
  private String modelLastname;

  private String modelAddress;
  private String modelPhone;
  private String modelEmail;

  @ColumnInfo(name = "images_json")
  // Contains paths to the images belonging to a contract
  private List<String> images;
  // Contains the signature of the model as an SVG
  private Bitmap modelSignature;

  @Ignore
  public Contract(Contract another) {
    this.id = another.id;
    this.isRead = another.isRead;
    this.dateLong = another.dateLong;
    this.location = another.location;
    this.modelFirstname = another.modelFirstname;
    this.modelLastname = another.modelLastname;
    this.modelAddress = another.modelAddress;
    this.modelPhone = another.modelPhone;
    this.modelEmail = another.modelEmail;
    this.images = another.images;
    this.modelSignature = another.modelSignature;
  }

  public Contract() {
    location = "";
    modelLastname = "";
    modelFirstname = "";
    dateLong = new Date().getTime();
    modelSignature = null;
  }

  @NonNull
  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_STRING_FORMAT, Locale.ROOT);
    return String.format(
        "Model Release Form (%s, %s)\n\n- Model -\n\n%s %s\n%s%s%ssigned: %s",
        sdf.format(new Date(dateLong)),
        location,
        modelFirstname,
        modelLastname,
        modelAddress != null && !modelAddress.isEmpty() ? modelAddress + '\n' : "",
        modelPhone != null && !modelPhone.isEmpty() ? modelPhone + '\n' : "",
        modelEmail != null && !modelEmail.isEmpty() ? modelEmail + '\n' : "",
        modelSignature != null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Contract contract = (Contract) o;

    if (id != contract.id) return false;
    if (isRead != contract.isRead) return false;
    if (dateLong != contract.dateLong) return false;
    if (!location.equals(contract.location)) return false;
    if (!modelFirstname.equals(contract.modelFirstname)) return false;
    if (!modelLastname.equals(contract.modelLastname)) return false;
    if (!Objects.equals(modelAddress, contract.modelAddress)) return false;
    if (!Objects.equals(modelPhone, contract.modelPhone)) return false;
    if (!Objects.equals(modelEmail, contract.modelEmail)) return false;
    if (!Objects.equals(images, contract.images)) return false;
    return Objects.equals(modelSignature, contract.modelSignature);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (isRead ? 1 : 0);
    result = 31 * result + (int) (dateLong ^ (dateLong >>> 32));
    result = 31 * result + location.hashCode();
    result = 31 * result + modelFirstname.hashCode();
    result = 31 * result + modelLastname.hashCode();
    result = 31 * result + (modelAddress != null ? modelAddress.hashCode() : 0);
    result = 31 * result + (modelPhone != null ? modelPhone.hashCode() : 0);
    result = 31 * result + (modelEmail != null ? modelEmail.hashCode() : 0);
    result = 31 * result + (images != null ? images.hashCode() : 0);
    result = 31 * result + (modelSignature != null ? modelSignature.hashCode() : 0);
    return result;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    isRead = read;
  }

  public long getDateLong() {
    return dateLong;
  }

  public void setDateLong(long dateLong) {
    this.dateLong = dateLong;
  }

  @NonNull
  public String getLocation() {
    return location;
  }

  public void setLocation(@NonNull String location) {
    this.location = location;
  }

  @NonNull
  public String getModelFirstname() {
    return modelFirstname;
  }

  public void setModelFirstname(@NonNull String modelFirstname) {
    this.modelFirstname = modelFirstname;
  }

  @NonNull
  public String getModelLastname() {
    return modelLastname;
  }

  public void setModelLastname(@NonNull String modelLastname) {
    this.modelLastname = modelLastname;
  }

  public String getModelAddress() {
    return modelAddress;
  }

  public void setModelAddress(String modelAddress) {
    this.modelAddress = modelAddress;
  }

  public String getModelPhone() {
    return modelPhone;
  }

  public void setModelPhone(String modelPhone) {
    this.modelPhone = modelPhone;
  }

  public String getModelEmail() {
    return modelEmail;
  }

  public void setModelEmail(String modelEmail) {
    this.modelEmail = modelEmail;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }

  public Bitmap getModelSignature() {
    return modelSignature;
  }

  public void setModelSignature(Bitmap modelSignature) {
    this.modelSignature = modelSignature;
  }

  public boolean isValid() {
    return this.isRead
        && this.dateLong != 0
        && !this.location.isEmpty()
        && !this.modelFirstname.isEmpty()
        && !this.modelLastname.isEmpty()
        && (this.modelAddress != null && !this.modelAddress.isEmpty())
        && this.modelSignature != null;
  }

  public boolean hasMinFields() {
    return this.dateLong != 0
        && !this.location.isEmpty()
        && !this.modelFirstname.isEmpty()
        && !this.modelLastname.isEmpty();
  }
}
