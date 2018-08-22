package am.gsoft.carserviceclient.phone;

import android.text.TextUtils;

public final class PhoneNumber {
  private static final PhoneNumber EMPTY_PHONE_NUMBER = new PhoneNumber("", "", "");

  private final String phoneNumber;
  private final String countryIso;
  private final String countryCode;

  public PhoneNumber(String phoneNumber, String countryIso, String countryCode) {
    this.phoneNumber = phoneNumber;
    this.countryIso = countryIso;
    this.countryCode = countryCode;
  }

  /**
   * Returns an empty instance of this class
   */
  public static PhoneNumber emptyPhone() {
    return EMPTY_PHONE_NUMBER;
  }

  public static boolean isValid(PhoneNumber phoneNumber) {
    return phoneNumber != null
        && !EMPTY_PHONE_NUMBER.equals(phoneNumber)
        && !TextUtils.isEmpty(phoneNumber.getPhoneNumber())
        && !TextUtils.isEmpty(phoneNumber.getCountryCode())
        && !TextUtils.isEmpty(phoneNumber.getCountryIso());
  }

  public static boolean isCountryValid(PhoneNumber phoneNumber) {
    return phoneNumber != null
        && !EMPTY_PHONE_NUMBER.equals(phoneNumber)
        && !TextUtils.isEmpty(phoneNumber.getCountryCode())
        && !TextUtils.isEmpty(phoneNumber.getCountryIso());
  }

  /**
   * Returns country code
   */
  public String getCountryCode() {
    return countryCode;
  }

  /**
   * Returns phone number without country code
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Returns 2 char country ISO
   */
  public String getCountryIso() {
    return countryIso;
  }
}
