package am.gsoft.carserviceclient.data.database.dao;

import java.util.Objects;

public class ServiceDate {

  public String phoneNumber;
  public long date;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServiceDate)) {
      return false;
    }
    ServiceDate that = (ServiceDate) o;
    return Objects.equals(phoneNumber, that.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phoneNumber);
  }
}
