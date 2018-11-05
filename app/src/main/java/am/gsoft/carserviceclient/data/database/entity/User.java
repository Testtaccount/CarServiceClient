package am.gsoft.carserviceclient.data.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.Objects;

@Entity(tableName = "user")
public class User{

  @NonNull
  @PrimaryKey
  private String key;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String mail;
//  private HashMap<String,Long> serviceDateMap;

  @Ignore
  public User() {
  }

  public User(String key, String firstName, String lastName, String phoneNumber,
      String mail) {
     this.key = key;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.mail = mail;
//    this.serviceDateMap = new HashMap<String, Long>();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

//  public HashMap<String, Long> getServiceDateMap() {
//    return serviceDateMap;
//  }

//  public void setServiceDateMap(HashMap<String, Long> serviceDateMap) {
//    this.serviceDateMap = serviceDateMap;
//  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(getKey(), user.getKey()) &&
        Objects.equals(getFirstName(), user.getFirstName()) &&
        Objects.equals(getLastName(), user.getLastName()) &&
        Objects.equals(getPhoneNumber(), user.getPhoneNumber()) &&
        Objects.equals(getMail(), user.getMail()) ;
//        && Objects.equals(getServiceDateMap(), user.getServiceDateMap());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getKey(), getFirstName(), getLastName(), getPhoneNumber(), getMail());//,getServiceDateMap());
  }

  @Override
  public String toString() {
    return "User{" +
        "key='" + key + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", mail='" + mail + '\'' +
//        ", serviceDateMap=" + serviceDateMap +
        '}';
  }
}
