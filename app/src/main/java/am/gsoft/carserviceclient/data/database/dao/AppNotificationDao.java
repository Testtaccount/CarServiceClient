package am.gsoft.carserviceclient.data.database.dao;

import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface AppNotificationDao {

  @Query("SELECT * FROM notifications")
  List<AppNotification> getAll();

  @Query("SELECT * FROM notifications WHERE carKey=:carKey AND oilKey=:oilKey")
  AppNotification getByCarIdAndOilId(String carKey,String oilKey);

  @Query("SELECT * FROM notifications WHERE carKey=:carKey AND oilKey=:oilKey AND type=:type")
  AppNotification get(String carKey,String oilKey,int type);

  @Query("SELECT * FROM notifications WHERE carKey=:carKey")
  LiveData<List<AppNotification>> getAllNotificationsByCarKey(String carKey);

  @Query("SELECT * FROM notifications WHERE id=:id")
  AppNotification getAppNotification(int id);

  @Query("select * from notifications where isEnabled = 1")
  LiveData<List<AppNotification>> getAllEnabledNotifications();

  @Insert
  long insert(AppNotification notification);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<AppNotification> notifications);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(AppNotification notification);

//  @Query("UPDATE notifications SET id=:id  WHERE id = :id")
//  int update(AppNotification notification,long id);

  @Query("UPDATE notifications SET isEnabled=:enabled  WHERE id = :id")
  int update(boolean enabled,long id);

  @Query("UPDATE notifications SET year=:year,month=:month,day=:day,hour=:hour,minute=:minute,carKey=:carKey,oilKey=:oilKey,note=:noteText, type=:type,isEnabled=:enabled  WHERE id = :id")
  int update(int year, int month, int day, int hour, int minute, String carKey, String oilKey, String noteText,int type, boolean enabled, long id);

  @Delete
  void delete(AppNotification notification);

  @Query("DELETE FROM notifications WHERE id=:id")
  void delete(long id);

  @Query("DELETE FROM notifications WHERE carKey=:carKey")
  void deleteAllMonthNotificationsByCarId(String carKey);

}
