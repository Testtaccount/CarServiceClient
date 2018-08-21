package am.gsoft.carserviceclient.notification;

import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.ResultListener;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import java.util.Calendar;
import java.util.List;

public class NotificationsRepository {

  private static final String TAG = NotificationsRepository.class.getSimpleName();

  // For Singleton instantiation
  private static final Object LOCK = new Object();
  private static NotificationsRepository sInstance;
  private final AppDatabase mAppDatabase;
  private final AppExecutors mExecutors;
  private final NotificationsController mNotificationsController;

  private boolean mInitialized = false;

  private NotificationsRepository(AppDatabase appDatabase,
      AppExecutors executors, NotificationsController notificationsController) {

    mAppDatabase = appDatabase;
    mExecutors = executors;
    mNotificationsController = notificationsController;

    // As long as the repository exists, observe the network LiveData.
    // If that LiveData changes, update the appDatabase.
//    LiveData<WeatherEntry[]> networkData = mWeatherNetworkDataSource.getCurrentWeatherForecasts();
//    networkData.observeForever(new Observer<WeatherEntry[]>() {
//      @Override
//      public void onChanged(@Nullable WeatherEntry[] newForecastsFromNetwork) {
//        mExecutors.diskIO().execute(() -> {
//          // Deletes old historical data
//          AppRepository.this.deleteOldData();
//          Log.NotificationActionsFragment(LOG_TAG, "Old weather deleted");
//          // Insert our new weather data into Sunshine's appDatabase
//          mAppDatabase.bulkInsert(newForecastsFromNetwork);
//          Log.NotificationActionsFragment(LOG_TAG, "New values inserted");
//        });
//      }
//    });
  }

  public synchronized static NotificationsRepository getInstance(AppDatabase appDatabase,
      AppExecutors executors, NotificationsController notificationsController) {
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = new NotificationsRepository(appDatabase, executors, notificationsController);
      }
    }
    return sInstance;
  }

  public void setMonthlyNotification(Car car, Oil currentOil, Oil newOil) {
    long date = newOil.getServiceDoneDate();

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH); //+ 1; // Note: zero based!
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);//+5;
    int second = calendar.get(Calendar.SECOND);
    int millis = calendar.get(Calendar.MILLISECOND);

    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        if (currentOil != null) {
          AppNotification previusAppNotification = mAppDatabase.mAppNotificationDao()
              .get(car.getId(), currentOil.getId(), AppNotification.TYPE_MONTHLY);
          if (previusAppNotification != null) {
            mNotificationsController.cancelNotification(previusAppNotification);
            mAppDatabase.mAppNotificationDao().delete(previusAppNotification.getId());
//            deletPreviusMonthNotification(previusAppNotification);
//            mAppDatabase.mAppNotificationDao().update(year,month+1,day,hour,minute,car.getId(),newOil.getId(),"",AppNotification.TYPE_MONTHLY,true,previusAppNotification.getId());
          }

        }

        final AppNotification newAppNotification = new AppNotification(year, month + 1, day, hour,
            minute, newOil.getServiceNextDate(), car.getId(), newOil.getId(), "MONTHLY REMINDER",
            AppNotification.TYPE_MONTHLY, true);
        long id = mAppDatabase.mAppNotificationDao().insert(newAppNotification);
        newAppNotification.setId((int) id);
//            mAppDatabase.mAppNotificationDao().update(newAppNotification);
        mNotificationsController.scheduleNotification(newAppNotification);
      }
    });

  }

  public void getAppNotification(int id, ResultListener<AppNotification> resultListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppNotification notification = mAppDatabase.mAppNotificationDao().getAppNotification(id);
        if (notification != null) {
          resultListener.onLoad(notification);
        } else {
          resultListener.onFail("Error");
        }
      }
    });

  }

  public void deleteAllNotificationsByCarId(long id) {
    LiveData<List<AppNotification>> liveData = mAppDatabase.mAppNotificationDao().getAllNotificationsByCarId(id);
    liveData.observeForever(new Observer<List<AppNotification>>() {
      @Override
      public void onChanged(@Nullable List<AppNotification> appNotifications) {
        liveData.removeObserver(this);
        if (appNotifications != null) {
          for (AppNotification mn : appNotifications) {
            mNotificationsController.cancelNotification(mn);
            if (mn.getType() == AppNotification.TYPE_REMIND
                || mn.getType() == AppNotification.TYPE_MILEAGE) {
              mNotificationsController.removeUpcomingNotificationFromBar(mn);
            }
            mExecutors.diskIO().execute(new Runnable() {
              @Override
              public void run() {
                mAppDatabase.mAppNotificationDao().delete(mn);
              }
            });
          }
        }
      }
    });

  }

  //////////////////////

  public void setReminderNotification(Car car, Oil oil, long reminderTime) {
//    long date = oil.getServiceDoneDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(reminderTime);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH); //+ 1; // Note: zero based!
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);//+5;
    int second = calendar.get(Calendar.SECOND);
    int millis = calendar.get(Calendar.MILLISECOND);

    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppNotification previusAppNotification = mAppDatabase.mAppNotificationDao()
            .get(car.getId(), oil.getId(), AppNotification.TYPE_REMIND);
        if (previusAppNotification != null) {
          mNotificationsController.cancelNotification(previusAppNotification);
          mNotificationsController.removeUpcomingNotificationFromBar(previusAppNotification);

          mAppDatabase.mAppNotificationDao().delete(previusAppNotification);
//            deletPreviusReminderNotification(previusAppNotification);
        }

        AppNotification newAppNotification = new AppNotification(year, month + 1, day, hour, minute,
            oil.getServiceNextDate(), car.getId(), oil.getId(), "REMINDER NOTIFICATION",
            AppNotification.TYPE_REMIND, true);
        long id = mAppDatabase.mAppNotificationDao().insert(newAppNotification);
        newAppNotification.setId((int) id);
//        mAppDatabase.mAppNotificationDao().update(newAppNotification);
        mNotificationsController.scheduleNotification(newAppNotification);
      }
    });

  }

  public void cancelPreviusReminderNotification(Car car, Oil oil) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppNotification previusAppNotification = mAppDatabase.mAppNotificationDao() .get(car.getId(), oil.getId(), AppNotification.TYPE_REMIND);
        if (previusAppNotification != null) {
          mNotificationsController.cancelNotification(previusAppNotification);
          mNotificationsController.removeUpcomingNotificationFromBar(previusAppNotification);
          mAppDatabase.mAppNotificationDao().delete(previusAppNotification);
//            deletPreviusReminderNotification(previusAppNotification);
        }
      }
    });
  }

  ///////////////////////

  public void setMileageNotification(Car car, Oil oil, long reminderTime) {
//    long date = oil.getServiceDoneDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(reminderTime);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH); //+ 1; // Note: zero based!
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);//+5;
    int second = calendar.get(Calendar.SECOND);
    int millis = calendar.get(Calendar.MILLISECOND);

    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppNotification previusAppNotification = mAppDatabase.mAppNotificationDao()
            .get(car.getId(), oil.getId(), AppNotification.TYPE_MILEAGE);
        if (previusAppNotification != null) {
          mNotificationsController.cancelNotification(previusAppNotification);
          mNotificationsController.removeUpcomingNotificationFromBar(previusAppNotification);
          mAppDatabase.mAppNotificationDao().delete(previusAppNotification);
        }

        AppNotification newAppNotification = new AppNotification(year, month + 1, day, hour, minute,
            oil.getServiceNextDate(), car.getId(), oil.getId(), "MILEAGE NOTIFICATION",
            AppNotification.TYPE_MILEAGE, true);
        long id = mAppDatabase.mAppNotificationDao().insert(newAppNotification);
        newAppNotification.setId((int) id);
//        mAppDatabase.mAppNotificationDao().update(newAppNotification);
        mNotificationsController.scheduleNotification(newAppNotification);
      }
    });

  }

  public void cancelPreviusMileageNotification(Car car, Oil oil) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        AppNotification previusAppNotification = mAppDatabase.mAppNotificationDao()
            .get(car.getId(), oil.getId(), AppNotification.TYPE_MILEAGE);
        if (previusAppNotification != null) {
          mNotificationsController.cancelNotification(previusAppNotification);
          mNotificationsController.removeUpcomingNotificationFromBar(previusAppNotification);
          mAppDatabase.mAppNotificationDao().delete(previusAppNotification);
        }
      }
    });
  }

  //////////////////////

  public void deleteAppNotification(AppNotification appNotification) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mAppDatabase.mAppNotificationDao().delete(appNotification);
      }
    });

  }

  public void disableAppNotification(AppNotification appNotification) {
    appNotification.setEnabled(false);
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mAppDatabase.mAppNotificationDao().update(false, appNotification.getId());
      }
    });

  }

}
