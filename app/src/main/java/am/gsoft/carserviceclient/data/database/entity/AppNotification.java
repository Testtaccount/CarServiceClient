package am.gsoft.carserviceclient.data.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.Calendar;

@Entity(tableName = "notifications")
public class AppNotification {
  public static final int TYPE_MONTHLY = 0;
  public static final int TYPE_REMIND = 1;
  public static final int TYPE_MILEAGE = 2;

  @PrimaryKey(autoGenerate = true)
  private int id;

  private int year;

  private int month;

  private int day;

  private int hour;

  private int minute;

  private long lastTime;

  private String carKey;

  private String oilKey;

  private String note;

  private int type;

  private boolean isEnabled;
//
//  @Ignore
//  public AppNotification() {
//  }
//

  public AppNotification(int id, int year, int month, int day, int hour, int minute,long lastTime,
      String carKey, String oilKey, String note, int type, boolean isEnabled) {
    this.id = id;
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.lastTime = lastTime;
    this.carKey = carKey;
    this.oilKey = oilKey;
    this.note = note;
    this.type = type;
    this.isEnabled = isEnabled;
  }

  @Ignore
  public AppNotification(int year, int month, int day, int hour, int minute,long lastTime,
      String carKey, String oilKey, String note, int type, boolean isEnabled) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.lastTime = lastTime;
    this.carKey = carKey;
    this.oilKey = oilKey;
    this.note = note;
    this.type = type;
    this.isEnabled = isEnabled;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public long getLastTime() {
    return lastTime;
  }

  public void setLastTime(long lastTime) {
    this.lastTime = lastTime;
  }

  public String getCarKey() {
    return carKey;
  }

  public void setCarKey(String carKey) {
    this.carKey = carKey;
  }

  public String getOilKey() {
    return oilKey;
  }

  public void setOilKey(String oilKey) {
    this.oilKey = oilKey;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String noteText) {
    this.note = noteText;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

//  public long getSnoozingUntilMillis() {
//    return snoozingUntilMillis;
//  }
//
//  public void setSnoozingUntilMillis(long snoozingUntilMillis) {
//    this.snoozingUntilMillis = snoozingUntilMillis;
//  }
//
//  public boolean isSnoozed() {
//    return snoozingUntilMillis > 0;
//  }
//
//  public void setSnoozing(long snoozingUntilMillis) {
//    this.snoozingUntilMillis = snoozingUntilMillis;
//  }
//
//  public long snoozingUntil() {
//    return isSnoozed() ? snoozingUntilMillis : 0;
//  }
//
//  public void stopSnoozing() {
//    snoozingUntilMillis = 0;
//  }

  public long ringsAt() {
    // Always with respect to the current date and time
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, getYear());
    calendar.set(Calendar.MONTH, getMonth()-1);
    calendar.set(Calendar.DAY_OF_MONTH, getDay());
//            dayOfMonth()+(int) (TimeUnit.MILLISECONDS.toDays(getDuration())));
    calendar.set(Calendar.HOUR_OF_DAY, getHour());
    calendar.set(Calendar.MINUTE, getMinute());//+5);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);


    long ringTime = calendar.getTimeInMillis();

    switch (getType()){
      case AppNotification.TYPE_MONTHLY:
        if (ringTime <= System.currentTimeMillis()) {
          // The specified time has passed for today
//                baseRingTime += TimeUnit.DAYS.toMillis(1);
          ringTime = getNextRingTime(ringTime);
//            ringTime += TimeUnit.MINUTES.toMillis(5);
        }
        break;
      case AppNotification.TYPE_REMIND:
//        if(ringTime >System.currentTimeMillis()){
//          ringTime=ringTime;
//        }
        break;
      case AppNotification.TYPE_MILEAGE:

        break;
    }


//    long reminderTime = snoozingUntil();
//
//    if(isSnoozed()){
//      if(reminderTime>System.currentTimeMillis()){
//        ringTime=reminderTime;
//      }
//    }
//
//    if (ringTime <= System.currentTimeMillis()) {
//      // The specified time has passed for today
////                baseRingTime += TimeUnit.DAYS.toMillis(1);
//
//      ringTime = getNextRingTime(ringTime);
////            ringTime += TimeUnit.MINUTES.toMillis(5);
//    }



    return ringTime;
  }

  public long getNextRingTime(long baseRingTime) {
    long nextTime = baseRingTime;

//        long diff=System.currentTimeMillis()-baseRingTime;
//        int seconds = (int) ( diff / 1000) % 60 ;
//        int minutes = (int) ((diff / (1000*60)) % 60);
//        int hours   = (int) ((diff / (1000*60*60)) % 24);
//        ToastUtils.shortToast("hour:minute:seconds " + hours + ":" + minutes + ":" + seconds);

    long currentTimeMillis = System.currentTimeMillis();
//        long interval = 2629800000L;//30 day;
    long interval = 3 * 60000;

    while (nextTime + interval <= currentTimeMillis + interval) {
      nextTime += interval;
    }

    return nextTime;
  }

  public long ringsIn() {
    return ringsAt() - System.currentTimeMillis();
  }

  @Override
  public String toString() {
    return "AppNotification{" +
        "id=" + id +
        ", year=" + year +
        ", month=" + month +
        ", day=" + day +
        ", hour=" + hour +
        ", minute=" + minute +
        ", lastTime=" + lastTime +
        ", carKey=" + carKey +
        ", oilKey=" + oilKey +
        ", noteText='" + note + '\'' +
        ", type=" + type +
        ", isEnabled=" + isEnabled +
        '}';
  }


}
