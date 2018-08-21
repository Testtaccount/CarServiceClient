package am.gsoft.carserviceclient.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootUpReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    // Note that this will be called when the device boots up, not when the app first launches.
    // We may have a lot of alarms to reschedule, so do this in the background using an IntentService.
//    context.startService(new Intent(context, OnBootUpNotificationSchedulerIntentService.class));


//    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);

    context.startService(new Intent(context, OnBootUpNotificationSchedulerIntentService.class));
  }
}



//public class BootReceiver extends BroadcastReceiver {
//  Context mContext;
//  private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
//  private List<Car> carList;
//
//  @Override
//  public void onReceive(Context context, Intent intent1) {
//    mContext = context;
//    String action = intent1.getAction();
//    if (action.equalsIgnoreCase(BOOT_ACTION)) {
//
//      //TODO: check if sign in complated or not?
//
//      carList = App.getAppSharedHelper().getCarListToSave();
//
//      for (Car c:carList){
//        Logger.NotificationActionsFragment("testt","car: "+c.toString());
//        List<Oil> oilList = App.getAppSharedHelper().getSaveOilListByCarKey(c.getKey());
//        Logger.NotificationActionsFragment("testt","oilList: " + oilList);
//
//        if (oilList != null && oilList.size()>0) {
//
//          Oil o = oilList.getAppNotification(oilList.size() - 1);
//          Logger.NotificationActionsFragment("testt","o: " + o.toString());
//
//          Intent intent = new Intent(context , NotificationIntentService.class);
//
//          intent.putExtra("getIcon", c.getIcon());
//          intent.putExtra("getCarBrand", c.getCarBrand());
//          intent.putExtra("getBrand", o.getBrand());
//          intent.putExtra("getType", o.getType());
//
//          PendingIntent pendingIntent = PendingIntent.getService(context, getNextNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
////      getTimeMilliSec("2018-05-16 16:24:30")
////    alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 30000,pendingIntent);  SystemClock.elapsedRealtime()
//          AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
//          alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  90000 + System.currentTimeMillis(), 90000, pendingIntent);
//        } else {
////      OilDataDbHelper.getInstance().setGetCarOilsResultListener(   );
//          OilDataDbHelper
//              .getInstance().getCarOils(c.getKey(), new FirebaseResultListener<List<Oil>>() {
//            @Override
//            public void onSuccess(List<Oil> oils) {
//              if (oils.size() > 0) {
//                Oil o = oils.getAppNotification(oils.size() - 1);
//                Logger.NotificationActionsFragment("testt","o: " + o.toString());
//
//                Intent intent = new Intent(context , NotificationIntentService.class);
//
//                intent.putExtra("getIcon", c.getIcon());
//                intent.putExtra("getCarBrand", c.getCarBrand());
//                intent.putExtra("getBrand", o.getBrand());
//                intent.putExtra("getType", o.getType());
//
//                PendingIntent pendingIntent = PendingIntent.getService(context, getNextNotifId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
////      getTimeMilliSec("2018-05-16 16:24:30")
////    alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 30000,pendingIntent);  SystemClock.elapsedRealtime()
//                AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  90000 + System.currentTimeMillis(), 90000, pendingIntent);
//              }
//            }
//
//            @Override
//            public void onFail(Exception e) {
//              ToastUtils.shortToast("onFail !!");
//            }
//          });
//
//        }
//
//      }
//
//    }
//  }
//}
