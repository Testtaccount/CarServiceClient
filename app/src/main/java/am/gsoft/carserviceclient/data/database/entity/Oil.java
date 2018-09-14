package am.gsoft.carserviceclient.data.database.entity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.Objects;

@Entity(tableName = "oil",
    foreignKeys = @ForeignKey(entity = Car.class,
        parentColumns = "key",
        childColumns = "carKey",
        onDelete = CASCADE),
    indices = {@Index(value = {"carKey"})}
//    indices = {@Index(value = {"carKey"},unique = true)}
)
public class Oil implements Parcelable{

  @NonNull
  @PrimaryKey
  private String key;

  private String carKey;

  private String serviceCompanyId="-";

  private String serviceOwnerName ="";

  private long serviceDoneDate;

  private long serviceNextDate;

  private String brand="-";

  private String type ="-";

  private double volume=0.0;

  private long serviceDoneKm;

  private long serviceNextKm;

  private long middleMonthKm;

  private long recomendedKm;

  private int isFilterChanged;

  @Ignore
  public Oil() {
    this.key = "-";
    this.serviceCompanyId = "-";
    this.brand = "-";
    this.type = "-";
    this.volume = 0.0;
  }

  public Oil(String carKey,String key, String serviceCompanyId,String serviceOwnerName, long serviceDoneDate,
      long serviceNextDate,
      String brand, String type, double volume, long serviceDoneKm, long recomendedKm,
      long serviceNextKm, long middleMonthKm, int isFilterChanged) {
    this.carKey=carKey;
    this.key = key;
    this.serviceCompanyId = serviceCompanyId;
    this.serviceOwnerName = serviceOwnerName;
    this.serviceDoneDate = serviceDoneDate;
    this.serviceNextDate = serviceNextDate;
    this.brand = brand;
    this.type = type;
    this.volume = volume;
    this.serviceDoneKm = serviceDoneKm;
    this.recomendedKm = recomendedKm;
    this.serviceNextKm = serviceNextKm;
    this.middleMonthKm = middleMonthKm;
    this.isFilterChanged = isFilterChanged;
  }

  protected Oil(Parcel in) {
    carKey = in.readString();
    key = in.readString();
    serviceCompanyId = in.readString();
    serviceOwnerName = in.readString();
    serviceDoneDate = in.readLong();
    serviceNextDate = in.readLong();
    brand = in.readString();
    type = in.readString();
    volume = in.readDouble();
    serviceDoneKm = in.readLong();
    serviceNextKm = in.readLong();
    middleMonthKm = in.readLong();
    recomendedKm = in.readLong();
    isFilterChanged = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(carKey);
    dest.writeString(key);
    dest.writeString(serviceCompanyId);
    dest.writeString(serviceOwnerName);
    dest.writeLong(serviceDoneDate);
    dest.writeLong(serviceNextDate);
    dest.writeString(brand);
    dest.writeString(type);
    dest.writeDouble(volume);
    dest.writeLong(serviceDoneKm);
    dest.writeLong(serviceNextKm);
    dest.writeLong(middleMonthKm);
    dest.writeLong(recomendedKm);
    dest.writeInt(isFilterChanged);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Oil> CREATOR = new Creator<Oil>() {
    @Override
    public Oil createFromParcel(Parcel in) {
      return new Oil(in);
    }

    @Override
    public Oil[] newArray(int size) {
      return new Oil[size];
    }
  };


  public String getCarKey() {
    return carKey;
  }

  public void setCarKey(String carKey) {
    this.carKey = carKey;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getServiceCompanyId() {
    return serviceCompanyId;
  }

  public void setServiceCompanyId(String serviceCompanyId) {
    this.serviceCompanyId = serviceCompanyId;
  }

  public String getServiceOwnerName() {
    return serviceOwnerName;
  }

  public void setServiceOwnerName(String serviceOwnerName) {
    this.serviceOwnerName = serviceOwnerName;
  }

  public long getServiceDoneDate() {
    return serviceDoneDate;
  }

  public void setServiceDoneDate(long serviceDoneDate) {
    this.serviceDoneDate = serviceDoneDate;
  }

  public long getServiceNextDate() {
    return serviceNextDate;
  }

  public void setServiceNextDate(long serviceNextDate) {
    this.serviceNextDate = serviceNextDate;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public long getServiceDoneKm() {
    return serviceDoneKm;
  }

  public void setServiceDoneKm(long serviceDoneKm) {
    this.serviceDoneKm = serviceDoneKm;
  }

  public long getServiceNextKm() {
    return serviceNextKm;
  }

  public void setServiceNextKm(long serviceNextKm) {
    this.serviceNextKm = serviceNextKm;
  }

  public long getMiddleMonthKm() {
    return middleMonthKm;
  }

  public void setMiddleMonthKm(long middleMonthKm) {
    this.middleMonthKm = middleMonthKm;
  }

  public long getRecomendedKm() {
    return recomendedKm;
  }

  public void setRecomendedKm(long recomendedKm) {
    this.recomendedKm = recomendedKm;
  }

  public int getIsFilterChanged() {
    return isFilterChanged;
  }

  public void setIsFilterChanged(int isFilterChanged) {
    this.isFilterChanged = isFilterChanged;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Oil)) {
      return false;
    }
    Oil oil = (Oil) o;
    return
        Objects.equals(getCarKey(),oil.getCarKey()) &&
        getServiceDoneDate() == oil.getServiceDoneDate() &&
        getServiceNextDate() == oil.getServiceNextDate() &&
        Double.compare(oil.getVolume(), getVolume()) == 0 &&
        getServiceDoneKm() == oil.getServiceDoneKm() &&
        getServiceNextKm() == oil.getServiceNextKm() &&
        getMiddleMonthKm() == oil.getMiddleMonthKm() &&
        getRecomendedKm() == oil.getRecomendedKm() &&
        getIsFilterChanged() == oil.getIsFilterChanged() &&
        Objects.equals(getKey(), oil.getKey()) &&
        Objects.equals(getServiceCompanyId(), oil.getServiceCompanyId()) &&
        Objects.equals(getServiceOwnerName(), oil.getServiceOwnerName()) &&
        Objects.equals(getBrand(), oil.getBrand()) &&
        Objects.equals(getType(), oil.getType());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getCarKey(), getKey(), getServiceCompanyId(), getServiceOwnerName(), getServiceDoneDate(),
        getServiceNextDate(), getBrand(), getType(), getVolume(), getServiceDoneKm(),
        getServiceNextKm(), getMiddleMonthKm(), getRecomendedKm(), getIsFilterChanged());
  }

  @Override
  public String toString() {
    return "Oil{" +
        ", carKey=" + carKey +
        ", key='" + key + '\'' +
        ", brand='" + brand + '\'' +
        ", type='" + type + '\'' +
        ", volume=" + volume +
        ", serviceDoneKm=" + serviceDoneKm +
        ", serviceNextKm=" + serviceNextKm +
        ", middleMonthKm=" + middleMonthKm +
        ", recomendedKm=" + recomendedKm +
        '}';
  }
}
