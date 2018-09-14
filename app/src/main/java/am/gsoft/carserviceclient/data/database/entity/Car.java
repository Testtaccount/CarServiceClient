package am.gsoft.carserviceclient.data.database.entity;

import am.gsoft.carserviceclient.R;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.Objects;

@Entity(tableName = "car")//, indices = {@Index(value = {"key"}, unique = true)})
public class Car implements Parcelable{

  @NonNull
  @PrimaryKey()
  private String key;

  private int icon = R.drawable.ic_directions_car_black_24dp;

  private int color=R.color.white;

  private String carBrand= "-";

  private String model="-";

  private String year ="-";

  private String numbers="-";

  private String vinCode="-";

  private String distanceUnit = "Km";

  @Ignore
  public Car() {
//    this.key = "-";
//    this.id = -1L;
    this.icon= R.drawable.ic_directions_car_black_24dp;
    this.color = R.color.white;
    this.carBrand = "-";
    this.model = "-";
    this.year = "-";
    this.numbers = "-";
    this.vinCode = "-";
    this.distanceUnit = "Km";
  }


  public Car(String key, int icon, int color, String carBrand, String model,
      String year, String numbers, String vinCode, String distanceUnit) {
    this.key = key;
    this.icon = icon;
    this.color = color;
    this.carBrand = carBrand;
    this.model = model;
    this.year = year;
    this.numbers = numbers;
    this.vinCode = vinCode;
    this.distanceUnit = distanceUnit;
  }

  protected Car(Parcel in) {
    key = in.readString();
    icon = in.readInt();
    color = in.readInt();
    carBrand = in.readString();
    model = in.readString();
    year = in.readString();
    numbers = in.readString();
    vinCode = in.readString();
    distanceUnit = in.readString();
  }

  public static final Creator<Car> CREATOR = new Creator<Car>() {
    @Override
    public Car createFromParcel(Parcel in) {
      return new Car(in);
    }

    @Override
    public Car[] newArray(int size) {
      return new Car[size];
    }
  };


  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public String getCarBrand() {
    return carBrand;
  }

  public void setCarBrand(String carBrand) {
    this.carBrand = carBrand;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getNumbers() {
    return numbers;
  }

  public void setNumbers(String numbers) {
    this.numbers = numbers;
  }

  public String getVinCode() {
    return vinCode;
  }

  public void setVinCode(String vinCode) {
    this.vinCode = vinCode;
  }

  public String getDistanceUnit() {
    return distanceUnit;
  }

  public void setDistanceUnit(String distanceUnit) {
    this.distanceUnit = distanceUnit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Car)) {
      return false;
    }
    Car car = (Car) o;
    return  getIcon() == car.getIcon() &&
        getColor() == car.getColor() &&
        Objects.equals(getKey(), car.getKey()) &&
        Objects.equals(getCarBrand(), car.getCarBrand()) &&
        Objects.equals(getModel(), car.getModel()) &&
        Objects.equals(getYear(), car.getYear()) &&
        Objects.equals(getNumbers(), car.getNumbers()) &&
        Objects.equals(getVinCode(), car.getVinCode()) &&
        Objects.equals(getDistanceUnit(), car.getDistanceUnit());
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(getKey(), getIcon(), getColor(), getCarBrand(), getModel(), getYear(),
            getNumbers(), getVinCode(), getDistanceUnit());
  }

  @Override
  public String toString() {
    return "Car{" +
        "  key='" + key + '\'' +
        ", carBrand='" + carBrand + '\'' +
        ", model='" + model + '\'' +
        ", year='" + year + '\'' +
        ", numbers='" + numbers + '\'' +
        ", vinCode='" + vinCode + '\'' +
        ", distanceUnit='" + distanceUnit + '\'' +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(key);
    dest.writeInt(icon);
    dest.writeInt(color);
    dest.writeString(carBrand);
    dest.writeString(model);
    dest.writeString(year);
    dest.writeString(numbers);
    dest.writeString(vinCode);
    dest.writeString(distanceUnit);
  }
}
