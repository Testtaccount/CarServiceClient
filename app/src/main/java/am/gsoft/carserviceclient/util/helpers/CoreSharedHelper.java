package am.gsoft.carserviceclient.util.helpers;



import static am.gsoft.carserviceclient.util.helpers.CoreSharedHelper.Constants.NAME;

import android.content.Context;
import android.content.SharedPreferences;


public abstract class CoreSharedHelper {


    public class Constants {

        public static final String NAME = "CAR_SERVICE_CLIENT";
//        public static final String IMPORT_INITIALIZED = "import_initialized";
//        public static final String FIRST_AUTH = "first_auth";
//        public static final String AUTH_TOKEN = "qb_token";
//        public static final String FIREBASE_TOKEN = "firebase_token";
//
//        public static final String USER_KEY = "user_key";
//        public static final String USER_ID = "user_id";
//        public static final String USER_FNAME = "user_fname";
//        public static final String USER_LNAME = "user_lname";
//        public static final String USER_EMAIL = "user_email";
//        public static final String USER_PHONE_NUMBER = "user_phone_number";
//        public static final String USER_CAR_LIST = "user_car_list";
//
//        public static final String LAST_OPEN_ACTIVITY = "last_open_activity";
//
//        public static final String PERMISSIONS_SAVE_FILE_WAS_REQUESTED = "permission_save_file_was_requested";
    }

    protected final SharedPreferences sharedPreferences;
    protected final SharedPreferences.Editor sharedPreferencesEditor;

//    private static CoreSharedHelper instance;
//
//    public static CoreSharedHelper getInstance() {
//        if (instance == null) {
//            throw new NullPointerException("CoreSharedHelper was not initialized!");
//        }
//        return instance;
//    }

    public CoreSharedHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    protected void delete(String key) {
        if (sharedPreferences.contains(key)) {
            sharedPreferencesEditor.remove(key).commit();
        }
    }

    protected void savePref(String key, Object value) {
        delete(key);

        if (value instanceof Boolean) {
            sharedPreferencesEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            sharedPreferencesEditor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            sharedPreferencesEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            sharedPreferencesEditor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            sharedPreferencesEditor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            sharedPreferencesEditor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to saveUser non-primitive preference");
        }

        sharedPreferencesEditor.commit();
    }

    protected <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    protected <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

}