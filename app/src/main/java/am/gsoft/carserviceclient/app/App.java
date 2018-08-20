package am.gsoft.carserviceclient.app;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.util.ActivityLifecycleHandler;
import am.gsoft.carserviceclient.util.helpers.SharedHelper;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

public class App extends MultiDexApplication {

  private static final String LOG_TAG = App.class.getName();
  private static App instance;
  private static FirebaseAnalytics mFirebaseAnalytics = null;
  private static SharedHelper appSharedHelper;
  //    private static FirebaseApi firebaseApi;
  private static AlarmManager alarmManager;
  private static AppRepository repository;
  private static Context context;
  public boolean isSimulator;

  public static App getInstance() {
    return instance;
  }


  @Override
  public void onCreate() {
    super.onCreate();
    isSimulator = true;

//        initFabric();
    initApplication();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel();
    }
  }

  private void initApplication() {
    instance = this;
    context = getApplicationContext();
    registerActivityLifecycleCallbacks(new ActivityLifecycleHandler());

    appSharedHelper = SharedHelper.getInstance();
//        firebaseApi= FirebaseApi.getInstance();
    alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

//    repository = InjectorUtils.provideRepository(context);

    mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        AppSession.load();
  }

  public static SharedHelper getAppSharedHelper() {
    return appSharedHelper;
  }

//    public static FirebaseApi getAppFirebaseApi() {
//        return firebaseApi;
//    }

  public static FirebaseAnalytics getFirebaseAnalytics() {
    return mFirebaseAnalytics;
  }

  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.channel_name);
      String description = getString(R.string.channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("1", name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

}

