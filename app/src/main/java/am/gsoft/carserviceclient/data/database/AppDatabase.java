package am.gsoft.carserviceclient.data.database;

import am.gsoft.carserviceclient.data.database.dao.AppNotificationDao;
import am.gsoft.carserviceclient.data.database.dao.CarDao;
import am.gsoft.carserviceclient.data.database.dao.OilDao;
import am.gsoft.carserviceclient.data.database.dao.UserDao;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.database.entity.User;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class,Car.class, Oil.class,AppNotification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  public static final String DATABASE_NAME = "carservice.db";

  private static AppDatabase sInstance;

  public abstract UserDao mUserDao();
  public abstract CarDao mCarDao();
  public abstract OilDao mOilDao();
  public abstract AppNotificationDao mAppNotificationDao();

  public static AppDatabase getInstance(final Context context) {
    if (sInstance == null) {
      synchronized (AppDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(context.getApplicationContext(),
              AppDatabase.class, AppDatabase.DATABASE_NAME).build();
        }
      }
    }
    return sInstance;
  }

}
