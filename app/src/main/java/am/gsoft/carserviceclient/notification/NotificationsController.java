/*
 * Copyright 2017 Phillip Hsu
 *
 * This file is part of ClockPlus.
 *
 * ClockPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClockPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClockPlus.  If not, see <http://www.gnu.org/licenses/>.
 */

package am.gsoft.carserviceclient.notification;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.util.Constant;
import am.gsoft.carserviceclient.util.Constant.Action;
import am.gsoft.carserviceclient.util.Constant.Extra;
import am.gsoft.carserviceclient.util.ToastUtils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public final class NotificationsController {

  private static final String TAG = NotificationsController.class.getSimpleName();

  private final Context context;
  private AlarmManager am;

  public NotificationsController(Context context) {
    this.context = context.getApplicationContext();
    this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }

  public void scheduleNotification(AppNotification notification) {
    if (!notification.isEnabled()) {
      return;
    }

//        removeUpcomingNotificationFromBar(alarm);
//        long ringAt = alarm.isSnoozed() ? alarm.snoozingUntil() : alarm.ringsAt();//

    long ringTempAt = notification.ringsAt();
//    Calendar calendar = Calendar.getInstance();
//    calendar.set(Calendar.YEAR, notification.getYear()+1);
//    calendar.set(Calendar.MONTH, notification.getMonth());
//    calendar.set(Calendar.DAY_OF_MONTH, notification.getDay());
//    calendar.set(Calendar.HOUR_OF_DAY, notification.getHour());
//    calendar.set(Calendar.MINUTE, notification.getMinute());
//    calendar.set(Calendar.SECOND, 0);
//    calendar.set(Calendar.MILLISECOND, 0);
    long endTime = notification.getLastTime();
//    long endTime =31_557_600_000L + ringTempAt;//365*24*60*60*1000;

    if (ringTempAt <= endTime) {
//        final PendingIntent alarmIntent = alarmIntent(alarm, false);
      final PendingIntent alarmIntent = notificationPendingIntent(notification);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        am.setExact(AlarmManager.RTC_WAKEUP, ringTempAt, alarmIntent);
//          am.setRepeating(AlarmManager.RTC_WAKEUP,ringTempAt,3 * 60000, alarmIntent);
      } else {
        am.set(AlarmManager.RTC_WAKEUP, ringTempAt, alarmIntent);
      }
    } else {
      final PendingIntent alarmIntent = notificationPendingIntent(notification);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        am.setExact(AlarmManager.RTC_WAKEUP, endTime, alarmIntent);
//          am.setRepeating(AlarmManager.RTC_WAKEUP,ringTempAt,3 * 60000, alarmIntent);
      } else {
        am.set(AlarmManager.RTC_WAKEUP, endTime, alarmIntent);
      }
//      AppExecutors.getInstance().mainThread().execute(new Runnable() {
//        @Override
//        public void run() {
//          ToastUtils.shortToast("Alarm Not Set !!!");
//        }
//      });
    }


//    if (showSnackbar) {
    AppExecutors.getInstance().mainThread().execute(new Runnable() {
      @Override
      public void run() {
        String message = context.getString(R.string.alarm_set_for, DurationUtils.toString(context, notification.ringsIn() , false/*abbreviate*/));
        ToastUtils.shortToast(message);
      }
    });

//    }

  }

  public void cancelNotification(AppNotification appNotification) {
    Log.d(TAG, "Cancelling alarm " + appNotification);

    PendingIntent pi = notificationPendingIntent(appNotification);
    if (pi != null) {
      am.cancel(pi);
      pi.cancel();
    }
    if(appNotification.getType()==AppNotification.TYPE_MONTHLY){
      removeUpcomingNotificationFromBar(appNotification);
    }

  }

  public void removeUpcomingNotificationFromBar(AppNotification notification) {
    Intent intent = new Intent(context, NotificationsReceiver.class)
        .setAction(Constant.Action.ACTION_CANCEL_NOTIFICATION)
        .putExtra(Extra.EXTRA_APP_NOTIFICATION_ID, notification.getId());
    context.sendBroadcast(intent);
  }

  private PendingIntent notificationPendingIntent(AppNotification notification) {
    Intent intent = new Intent(context, NotificationsReceiver.class)
        .setAction(Action.ACTION_SEND_MONTH_NOTIFICATION)
        .putExtra(Extra.EXTRA_APP_NOTIFICATION_ID, notification.getId());

    return PendingIntent.getBroadcast(context, notification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

//  public void setupMonthlyNotificationsAlarm(Oil newOil, Car currentCar) {
//
//    long date = newOil.getServiceDoneDate();
//
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(date);
//    int year = calendar.getAppNotification(Calendar.YEAR);
//    int month = calendar.getAppNotification(Calendar.MONTH); //+ 1; // Note: zero based!
//    int day = calendar.getAppNotification(Calendar.DAY_OF_MONTH);
//    int hour = calendar.getAppNotification(Calendar.HOUR_OF_DAY);
//    int minute = calendar.getAppNotification(Calendar.MINUTE) + 1;//+5;
//    int second = calendar.getAppNotification(Calendar.SECOND);
//    int millis = calendar.getAppNotification(Calendar.MILLISECOND);
//
////        ToastUtils.shortToast("hour:minute  " + hour + ":" + minute);
//
//    Alarm alarm = Alarm.builder()
//        .year(year)//(2018)
//        .month(month - 1)//(6-1)
//        .dayOfMonth(day)//(4)
//        .hour(hour)//(12hour)
//        .minutes(minute + 2)//(30minute)
//        .key(newOil.getKey())
//        .icon(currentCar.getIcon())
//        .carId(currentCar.getId())
//        .carBrand(currentCar.getCarBrand())
//        .brand(newOil.getBrand())
//        .type(newOil.getType())
//        .build();
////        alarm.setId(currentOil.getId());
//    alarm.setEnabled(true);
//
////    ToastUtils.shortToast("add alarm.getIntId(): " +alarm.getIntId());
//
////    mAsyncUpdateHandler.asyncInsert(alarm);
//
//  }
//
//  public void setupMonthlyNotificationsAlarmlong(long date,Alarm alarm) {
//
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(date);
//    int year = calendar.getAppNotification(Calendar.YEAR);
//    int month = calendar.getAppNotification(Calendar.MONTH)-1; //+ 1; // Note: zero based!
//    int day = calendar.getAppNotification(Calendar.DAY_OF_MONTH);
//    int hour = calendar.getAppNotification(Calendar.HOUR_OF_DAY);
//    int minute = calendar.getAppNotification(Calendar.MINUTE);//+5;
//    int second = calendar.getAppNotification(Calendar.SECOND);
//    int millis = calendar.getAppNotification(Calendar.MILLISECOND);
//
////        ToastUtils.shortToast("hour:minute  " + hour + ":" + minute);
//
//    Alarm newAlarm = Alarm.builder()
//        .year(year)//(2018)
//        .month(month)//(6-1)
//        .dayOfMonth(day)//(4)
//        .hour(hour)//(12hour)
//        .minutes(minute )//(30minute)
//        .key(alarm.key())
//        .icon(alarm.icon())
//        .carId(alarm.carId())
//        .carBrand(alarm.carBrand())
//        .brand(alarm.brand())
//        .type(alarm.type())
//        .build();
////        alarm.setId(currentOil.getId());
//    newAlarm.setEnabled(true);
//
////    ToastUtils.shortToast("add alarm.getIntId(): " +alarm.getIntId());
//
////    mAsyncUpdateHandler.asyncInsert(newAlarm);
//
//  }
//
//  public void setupMonthlyNotificationsAlarm(Oil newOil, Car currentCar) {
//
//    long date = newOil.getServiceDoneDate();
//
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(date);
//    int year = calendar.getAppNotification(Calendar.YEAR);
//    int month = calendar.getAppNotification(Calendar.MONTH); //+ 1; // Note: zero based!
//    int day = calendar.getAppNotification(Calendar.DAY_OF_MONTH);
//    int hour = calendar.getAppNotification(Calendar.HOUR_OF_DAY);
//    int minute = calendar.getAppNotification(Calendar.MINUTE) + 1;//+5;
//    int second = calendar.getAppNotification(Calendar.SECOND);
//    int millis = calendar.getAppNotification(Calendar.MILLISECOND);
//
////        ToastUtils.shortToast("hour:minute  " + hour + ":" + minute);
//
//    Alarm alarm = Alarm.builder()
//        .year(year)//(2018)
//        .month(month - 1)//(6-1)
//        .dayOfMonth(day)//(4)
//        .hour(hour)//(12hour)
//        .minutes(minute + 2)//(30minute)
//        .key(newOil.getKey())
//        .icon(currentCar.getIcon())
//        .carId(currentCar.getId())
//        .carBrand(currentCar.getCarBrand())
//        .brand(newOil.getBrand())
//        .type(newOil.getType())
//        .build();
////        alarm.setId(currentOil.getId());
//    alarm.setEnabled(true);
//
////    ToastUtils.shortToast("add alarm.getIntId(): " +alarm.getIntId());
//
////    mAsyncUpdateHandler.asyncInsert(alarm);
//
//
//  }
//
//  public void setupMonthlyNotificationsAlarmLong2(long date,Alarm alarm) {
//
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(date);
//    int year = calendar.getAppNotification(Calendar.YEAR);
//    int month = calendar.getAppNotification(Calendar.MONTH)-1; //+ 1; // Note: zero based!
//    int day = calendar.getAppNotification(Calendar.DAY_OF_MONTH);
//    int hour = calendar.getAppNotification(Calendar.HOUR_OF_DAY);
//    int minute = calendar.getAppNotification(Calendar.MINUTE);//+5;
//    int second = calendar.getAppNotification(Calendar.SECOND);
//    int millis = calendar.getAppNotification(Calendar.MILLISECOND);
//
////        ToastUtils.shortToast("hour:minute  " + hour + ":" + minute);
//
//    Alarm newAlarm = Alarm.builder()
//        .year(alarm.year())//(2018)
//        .month(alarm.month())//(6-1)
//        .dayOfMonth(alarm.dayOfMonth())//(4)
//        .hour(alarm.hour())//(12hour)
//        .minutes(alarm.minutes())//(30minute)
//        .key(alarm.key())
//        .icon(alarm.icon())
//        .carId(alarm.carId())
//        .carBrand(alarm.carBrand())
//        .brand(alarm.brand())
//        .type(alarm.type())
//        .build();
//    newAlarm.setEnabled(true);
//
//    newAlarm.setSnoozing(date);
////    ToastUtils.shortToast("add alarm.getIntId(): " +alarm.getIntId());
//
////    mAsyncUpdateHandler.asyncInsert(newAlarm);
//
//  }
//
//  public void updateAlarm(long id,long timeMils,Alarm alarm) {
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(timeMils);
//    int year = calendar.getAppNotification(Calendar.YEAR);
//    int month = calendar.getAppNotification(Calendar.MONTH); //+ 1; // Note: zero based!
//    int day = calendar.getAppNotification(Calendar.DAY_OF_MONTH);
//    int hour = calendar.getAppNotification(Calendar.HOUR_OF_DAY);
//    int minute = calendar.getAppNotification(Calendar.MINUTE) + 1;//+5;
//    int second = calendar.getAppNotification(Calendar.SECOND);
//    int millis = calendar.getAppNotification(Calendar.MILLISECOND);
//
//    Alarm newAlarm = Alarm.builder()
//        .year(year)//(2018)
//        .month(month)//(6-1)
//        .dayOfMonth(day)//(4)
//        .hour(hour)//(12hour)
//        .minutes(minute)//(30minute)
//        .key(alarm.key())
//        .icon(alarm.icon())
//        .carId(alarm.carId())
//        .carBrand(alarm.carBrand())
//        .brand(alarm.brand())
//        .type(alarm.type())
//        .build();
////        alarm.setId(currentOil.getId());
//    alarm.setEnabled(true);
//    if (alarm != null) {
////            ToastUtils.shortToast("delete alarm.getIntId(): " + alarm.key());
////      mAsyncUpdateHandler.asyncUpdate(id,newAlarm);
//    }
//  }

//  public void deletePreviusMonthlyNotificationAlarm(String oilKey) {
//    AlarmCursor cursor = new AlarmsTableManager(context).queryKeyAlarm(oilKey);
//    Alarm alarm = cursor.getItem();
//    if (alarm != null) {
////            ToastUtils.shortToast("delete alarm.getIntId(): " + alarm.key());
////      mAsyncUpdateHandler.asyncDelete(alarm);
//    }
//  }
//
//  public void deletePreviusMonthlyNotificationAlarm(Alarm alarm ) {
//    if (alarm != null) {
////            ToastUtils.shortToast("delete alarm.getIntId(): " + alarm.key());
////      mAsyncUpdateHandler.asyncDelete(alarm);
//    }
//  }
//
//  public void removeDataAndNotifications(List<Car> carList) {
//    new Thread() {
//      public void run() {
//        for (Car car : carList) {
//
//          Logger.d("testt", "car: " + car.toString());
//          List<Oil> oilList = App.getAppSharedHelper().getSaveOilListByCarKey(car.getKey());
//          Logger.d("testt", "oilList: " + oilList);
//
//          if (oilList != null && oilList.size() > 0) {
//            Oil oil = oilList.getAppNotification(oilList.size() - 1);
//            deletePreviusMonthlyNotificationAlarm(oil.getKey());
//
//          } else {
//            OilDataDbHelper
//                .getInstance()
//                .getCarOils(car.getKey(), new ResultListener<List<Oil>>() {
//                  @Override
//                  public void onSuccess(List<Oil> oils) {
//                    if (oils.size() > 0) {
//                      Oil oil = oils.getAppNotification(oils.size() - 1);
//                      Logger.d("testt", "oil: " + oil.toString());
//                      deletePreviusMonthlyNotificationAlarm(oil.getKey());
//                    }
//                  }
//
//                  @Override
//                  public void onFail(Exception e) {
//                    ToastUtils.shortToast("onFail !!");
//                  }
//                });
//          }
//        }
//      }
//    }.start();
//  }
}
