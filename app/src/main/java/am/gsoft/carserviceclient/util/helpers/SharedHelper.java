package am.gsoft.carserviceclient.util.helpers;

import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.AUTH_TOKEN;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.CAR_OIL_LIST;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.DRIVEN_KM;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.FIREBASE_TOKEN;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.FIRST_AUTH;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.IMPORT_INITIALIZED;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.LAST_OPEN_ACTIVITY;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.OIL_NOTES;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.PERMISSIONS_SAVE_FILE_WAS_REQUESTED;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_CAR_LIST;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_EMAIL;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_FNAME;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_ID;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_KEY;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_LNAME;
import static am.gsoft.carserviceclient.util.helpers.SharedHelper.Constants.USER_PHONE_NUMBER;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.database.entity.User;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class SharedHelper extends CoreSharedHelper {

    private static final String TAG = SharedHelper.class.getSimpleName();
    private static volatile SharedHelper sInstance;

    public class Constants {
        public static final String IMPORT_INITIALIZED = "import_initialized";
        public static final String FIRST_AUTH = "first_auth";
        public static final String AUTH_TOKEN = "qb_token";
        public static final String FIREBASE_TOKEN = "firebase_token";

        public static final String USER = "user";
        public static final String USER_KEY = "user_key";
        public static final String USER_ID = "user_id";
        public static final String USER_FNAME = "user_fname";
        public static final String USER_LNAME = "user_lname";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PHONE_NUMBER = "user_phone_number";
        public static final String USER_CAR_LIST = "user_car_list";
        public static final String CAR_OIL_LIST = "car_oil_list";
        public static final String OIL_NOTES = "oil_notes";
        public static final String DRIVEN_KM = "driven_km";

        public static final String LAST_OPEN_ACTIVITY = "last_open_activity";

        public static final String PERMISSIONS_SAVE_FILE_WAS_REQUESTED = "permission_save_file_was_requested";
        public static final String USER_AGREEMENT = "user_agreement";
        public static final String REMEMBER_ME = "remember_me";
        public static final String ACCOUNT_ID = "account_id";
        public static final String ACCOUNT_PHONE_NUMBER = "account_phone_number";
        public static final String ENABLING_PUSH_NOTIFICATIONS = "enabling_push_notifications";
        public static final String SPINNER_POSITION = "spinner_position";
        public static final String PREVIUS_DRIVEN_SUM = "PREVIUS_DRIVEN_SUM";

    }


    public static SharedHelper getInstance() {
        if (sInstance == null) {
            synchronized (TAG) {
                if (sInstance == null) {
                    sInstance = new SharedHelper(App.getInstance());
                }
            }
        }
        return sInstance;
    }

    private SharedHelper(Context context) {
        super(context);
    }


    public void clearAll() {
        sharedPreferencesEditor.clear();
    }

    public void clearSaveOilListByCarKey(String key) {
        sharedPreferencesEditor.remove(key);
        sharedPreferencesEditor.apply();
    }


    public boolean isUsersImportInitialized() {
        return getPref(IMPORT_INITIALIZED, false);
    }

    public void saveUsersImportInitialized(boolean save) {
        savePref(IMPORT_INITIALIZED, save);
    }

    public boolean isFirstAuth() {
        return getPref(FIRST_AUTH, true);
    }

    public void saveFirstAuth(boolean firstAuth) {
        savePref(FIRST_AUTH, firstAuth);
    }

    public String getAuthToken() {
        return getPref(AUTH_TOKEN, null);
    }

    public void saveAuthToken(String token) {
        savePref(AUTH_TOKEN, token);
    }

    public void saveFirebaseToken(String firebaseToken){
        savePref(FIREBASE_TOKEN, firebaseToken);
    }

    public String getFirebaseToken(){
        return getPref(FIREBASE_TOKEN, null);
    }

    public String getUserKey() {
        return getPref(USER_KEY);
    }

    public void saveUserKey(String key) {
        savePref(USER_KEY, key);
    }


    public Long getUserId() {
        return getPref(USER_ID);
    }

    public void saveUserId(Long id) {
        savePref(USER_ID, id);
    }

    public void removeUserId() {
        delete(USER_ID);
        sharedPreferencesEditor.clear().apply();
    }


    public String getUserfName() {
        return getPref(USER_FNAME, null);
    }

    public void saveUserfName(String fName) {
        savePref(USER_FNAME, fName);
    }


    public String getUserlName() {
        return getPref(USER_LNAME, null);
    }

    public void saveUserlName(String lName) {
        savePref(USER_LNAME, lName);
    }

  public String getAppUser() {
    return getPref(USER, null);
  }

  public void saveAppUser(String id) {
    savePref(USER, id);
  }

    public String getUserCarList() {
        return getPref(USER_CAR_LIST, null);
    }

    public void saveUserCarList(String id) {
        savePref(USER_CAR_LIST, id);
    }


    public String getCarOilsList(String carKey) {
        return getPref(getCarOilListFileName(carKey), null);
    }

    public void saveCarOilsList(String carKey, String id) {
        savePref(getCarOilListFileName(carKey), id);
    }

    public String getUserEmail() {
        return getPref(USER_EMAIL, null);
    }

    public void saveUserEmail(String email) {
        savePref(USER_EMAIL, email);
    }

    public String getUserFullPhoneNumber() {
        return getPref(USER_PHONE_NUMBER, null);
    }

    public void saveUserFullPhoneNumber(String phoneNumber) {
        savePref(USER_PHONE_NUMBER, phoneNumber);
    }

    public boolean isPermissionsSaveFileWasRequested(){
        return getPref(PERMISSIONS_SAVE_FILE_WAS_REQUESTED, false);
    }

    public void savePermissionsSaveFileWasRequested(boolean requested){
        savePref(PERMISSIONS_SAVE_FILE_WAS_REQUESTED, requested);
    }

    public void clearUserData() {
        saveUserId(null);
        saveUserfName(null);
        saveUserlName(null);
        saveUserEmail(null);
        saveUserFullPhoneNumber(null);
        saveSavedRememberMe(false);
        saveUserCarList(null);
    }


    public void saveLastOpenActivity(String activityClassName) {
        savePref(LAST_OPEN_ACTIVITY, activityClassName);
    }

    public String getLastOpenActivity() {
        return getPref(LAST_OPEN_ACTIVITY);
    }




    public boolean isShownUserAgreement() {
        return getPref(Constants.USER_AGREEMENT, false);
    }

    public void saveShownUserAgreement(boolean save) {
        savePref(Constants.USER_AGREEMENT, save);
    }

    public boolean isSavedRememberMe() {
        return getPref(Constants.REMEMBER_ME, false);
    }

    public void saveSavedRememberMe(boolean save) {
        savePref(Constants.REMEMBER_ME, save);
    }

    public String getAccountId() {
        return getPref(Constants.ACCOUNT_ID, null);
    }

    public void saveAccountId(String id) {
        savePref(Constants.ACCOUNT_ID, id);
    }

    public boolean isEnablePushNotifications() {
        return getPref(Constants.ENABLING_PUSH_NOTIFICATIONS, false);
    }

    public void saveEnablePushNotifications(boolean enable) {
        savePref(Constants.ENABLING_PUSH_NOTIFICATIONS, enable);
    }


    public long getSpinnerPosition() {
        return getPref(Constants.SPINNER_POSITION, 0L);
    }

    public void saveSpinnerPosition(long position) {
        savePref(Constants.SPINNER_POSITION, position);
    }


    private String getCarOilListFileName(String carKey){
        return String.format("%s_%s",
            CAR_OIL_LIST,
            carKey);
    }

    private String getOilNotesFileName(String carKey, String oilKey) {
        return String.format("%s_%s_%s", carKey, OIL_NOTES, oilKey);
    }

    public String getOilNotes(String  carKey,String  oilKey) {
        return getPref(getOilNotesFileName(carKey,oilKey), null);
    }

    public void saveOilNotes(String  carKey,String oilKey,String notes) {
        savePref(getOilNotesFileName(carKey,oilKey), notes);
    }

//    public void saveUser(User user) {
//        saveUserKey(user.getKey());
//        saveUserId(user.getId());
//        saveUserfName(user.getFirstName());
//        saveUserlName(user.getLastName());
//        saveUserEmail(user.getMail());
//        saveUserFullPhoneNumber(user.getPhoneNumber());
////        saveUserCarList(setCarListToSave(user.getUserCarList();
//    }


//  public User loadUser(){
//    User user = new User();
//    user.setKey(getUserKey());
//    user.setId(getUserId());
//    user.setFirstName(getUserfName());
//    user.setLastName(getUserlName());
//    user.setMail(getUserEmail());
//    user.setPhoneNumber(getUserFullPhoneNumber());
////        user.setUserCars(getCarListToSave());
//    return user;
//  }

   public void saveUser(User user) {
     Gson gson = new Gson();
     String jsonStr = gson.toJson(user);
     saveAppUser(jsonStr);
    }


  public User loadUser(){
    String strList = getAppUser();
    if(strList!=null) {
      Gson gson = new Gson();
      Type type = new TypeToken<User>(){}.getType();
      return gson.fromJson(strList, type);
    }
    return null;
  }

    public  List<Car> getCarListToSave(){
        String strList = getUserCarList();
        if(strList!=null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Car>>(){}.getType();
            return gson.fromJson(strList, type);
        }
        return null;
    }

    public  void setCarListToSave(List<Car> carList){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(carList);
        saveUserCarList(jsonStr);
    }

    public  List<Oil> getSaveOilListByCarKey(String carKey){
        String strList = getCarOilsList(carKey);
        if(strList!=null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Oil>>(){}.getType();
            return gson.fromJson(strList, type);
        }
        return null;
    }

    public  void setSaveOilListByCarKey(String carKey,List<Oil> oils){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(oils);
        saveCarOilsList(carKey,jsonStr);
    }

  private String getPreviusDrivenSumFileName(long carId, long oilId) {
    return String.format("%s_%s_%s", DRIVEN_KM, String.valueOf(carId), String.valueOf(oilId));
  }

    public long getPreviusDrivenSum(long carId, long oilId) {
        return getPref(getPreviusDrivenSumFileName(carId,oilId), 0L);
    }

    public void setPreviusDrivenSum(long carId, long oilId,long drivenSum) {
        savePref(getPreviusDrivenSumFileName(carId,oilId), drivenSum);
    }

}