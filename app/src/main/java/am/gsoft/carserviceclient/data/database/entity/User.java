package am.gsoft.carserviceclient.data.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User{

  @PrimaryKey
  private long id;
  private String key;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String mail;
//  private List<String> carKeys;

  @Ignore
  public User() {
  }

  public User(long id, String key, String firstName, String lastName, String phoneNumber,
      String mail) {
    this.id = id;
    this.key = key;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.mail = mail;
  }

  @Ignore
  public User(String key, String firstName, String lastName, String phoneNumber, String mail) {
    this.key = key;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.mail = mail;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", key='" + key + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", mail='" + mail + '\'' +
        '}';
  }
}
