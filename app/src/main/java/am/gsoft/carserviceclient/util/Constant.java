package am.gsoft.carserviceclient.util;

public class Constant {

    public static class Action {
        public static final String ACTION_MAIN_ACTIVITY_INTENT = "ACTION_MAIN_ACTIVITY_INTENT";
        public static final String ACTION_CREATE_NEW_OIL_ACTIVITY_INTENT = "ACTION_CREATE_NEW_OIL_ACTIVITY_INTENT";
        public static final String ACTION_EDIT_CAR_ACTIVITY_INTENT ="ACTION_EDIT_CAR_ACTIVITY_INTENT";
        public static final String ACTION_EDIT_OIL_ACTIVITY_INTENT = "ACTION_EDIT_OIL_ACTIVITY_INTENT";
        public static final String ACTION_GARAGE_ACTIVITY_INTENT = "ACTION_GARAGE_ACTIVITY_INTENT";
        public static final String ACTION_OIL_HISTORY_ACTIVITY_INTENT = "ACTION_OIL_HISTORY_ACTIVITY_INTENT";
        public static final String ACTION_CREATE_NEW_CAR_ACTIVITY = "ACTION_CREATE_NEW_CAR_ACTIVITY";
        public static final String ACTION_NOTIFICATION_REPEAT = "ACTION_NOTIFICATION_REPEAT";
        public static final String ACTION_NOTIFICATION_MILEAGE = "ACTION_NOTIFICATION_MILEAGE";
        public static final String ACTION_SEND_MONTH_NOTIFICATION = "ACTION_SEND_MONTH_NOTIFICATION";
        public static final String ACTION_CANCEL_NOTIFICATION = "ACTION_CANCEL_NOTIFICATION";

    }

    public static class Argument {
        public static final String ARGUMENT_DATA = "ARGUMENT_DATA";
        public static final String ARGUMENT_OIL_HISTORY_LIST = "ARGUMENT_OIL_HISTORY_LIST";
        public static final String ARGUMENT_OIL_HISTORY = "ARGUMENT_OIL_HISTORY";
    }

    public class Extra {
        public static final String EXTRA_CURRENT_CAR="EXTRA_CURRENT_CAR";
        public static final String EXTRA_FIRST_OIL="EXTRA_FIRST_OIL";
        public static final String EXTRA_CURRENT_OIL="EXTRA_CURRENT_OIL";
        public static final String EXTRA_INDEX_OF_CURRENT_OIL="EXTRA_INDEX_OF_CURRENT_OIL";
        public static final String EXTRA_CURRENT_OIL_LIST="EXTRA_CURRENT_OIL_LIST";
        public static final String EXTRA_NOTIFICATION_MESSAGE_CAR_ID = "EXTRA_NOTIFICATION_MESSAGE_CAR_ID";
        public static final String EXTRA_NOTIFICATION_MESSAGE_ID = "EXTRA_NOTIFICATION_MESSAGE_ID";
        public static final String EXTRA_APP_NOTIFICATION_ID = "EXTRA_APP_NOTIFICATION_ID";
        public static final String EXTRA_USER = "EXTRA_USER";
    }

    public static class Symbol {
        public static final String ASTERISK = "*";
        public static final String NEW_LINE = "\n";
        public static final String SPACE = " ";
        public static final String NULL = "";
        public static final String COLON = ":";
        public static final String COMMA = ",";
        public static final String SLASH = "/";
        public static final String DOT = ".";
        public static final String UNDERLINE = "_";
        public static final String DASH = "-";
        public static final String AT = "@";
        public static final String AMPERSAND = "&";
    }

    public static class Util {
        public static final int QUALITY = 100;
        public static final String ANDROID_DATA_ROOT = "Android/data/";
        public static final String SD = "file://";
        public static final String SHA = "SHA";
    }

    public static class Identifier {
        public static final String ID = "id";
        public static final String ANDROID = "android.support";
        public static final String ALERT_TITLE = "alertTitle";
    }

    public static class BuildType {
        public static final String RELEASE = "release";
        public static final String DEBUG = "debug";
    }
}
