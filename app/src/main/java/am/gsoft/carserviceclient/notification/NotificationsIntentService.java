package am.gsoft.carserviceclient.notification;

import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_SEND_MONTH_NOTIFICATION;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_ID;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.ResultListener;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.ui.activity.NotificationActionsActivity;
import am.gsoft.carserviceclient.ui.fragment.MileageFragment;
import am.gsoft.carserviceclient.ui.fragment.NextReminderFragment;
import am.gsoft.carserviceclient.util.Constant.Action;
import am.gsoft.carserviceclient.util.Constant.Extra;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationsIntentService extends IntentService {

  public static final String TAG = NotificationsIntentService.class.getSimpleName();

  private static final String ACTION_BAZ = "am.gsoft.carserviceclient.data.action.BAZ";


  public NotificationsIntentService() {
    super(NotificationsIntentService.class.getName());
  }

  public static void startSendMonthNotification(Context context,int id) {
    Intent intent = new Intent(context, NotificationsIntentService.class);
    intent.setAction(ACTION_SEND_MONTH_NOTIFICATION);
    intent.putExtra(Extra.EXTRA_APP_NOTIFICATION_ID,id);
    context.startService(intent);
  }

  public static void startCencelMonthNotification(Context context,int id) {
    Intent intent = new Intent(context, NotificationsIntentService.class);
    intent.setAction(Action.ACTION_CANCEL_NOTIFICATION);
    intent.putExtra(Extra.EXTRA_APP_NOTIFICATION_ID,id);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      int id = intent.getIntExtra(Extra.EXTRA_APP_NOTIFICATION_ID, -1);
      if (action != null) {
        switch (action){
          case ACTION_SEND_MONTH_NOTIFICATION: {
            handleActionSendMonthNotification(id);
            break;
          }
          case Action.ACTION_CANCEL_NOTIFICATION: {
            handleActionCancelMonthNotification(id);
            break;
          }
        }
      }

    }
  }

  private void handleActionSendMonthNotification(int id) {
    WakeLock.acquire(getApplicationContext());
    NotificationsRepository repository = InjectorUtils.provideNotificationRepository(getApplicationContext());
    NotificationsController controller = new NotificationsController(getApplicationContext());

    repository.getAppNotification(id, new ResultListener<AppNotification>() {
      @Override
      public void onLoad(AppNotification notification) {
        int id = notification.getId();
        long carId = notification.getCarId();
        long oilId = notification.getOilId();


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
          @Override
          public void run() {
            Car car=AppDatabase.getInstance(getApplicationContext()).mCarDao().get(carId);
            Oil oil=AppDatabase.getInstance(getApplicationContext()).mOilDao().get(oilId);

            String text = notification.getNote();

            switch (notification.getType()) {
              case AppNotification.TYPE_MONTHLY:
                displayNotification(id, car, oil, text);
                if (notification.ringsAt() <= notification.getLastTime()) {
                  controller.scheduleNotification(notification);
                }else {
                  controller.cancelNotification(notification);
                  repository.disableAppNotification(notification);
                }
                break;
              case AppNotification.TYPE_REMIND:
                displayNotification(id, car, oil,text);
                controller.cancelNotification(notification);
                repository.disableAppNotification(notification);
                break;
              case AppNotification.TYPE_MILEAGE:
                displayNotification(id, car, oil,text);
                controller.cancelNotification(notification);
                repository.disableAppNotification(notification);
                break;
            }

          }
        });



      }

      @Override
      public void onFail(String e) {

      }
    });

  }

  private void handleActionCancelMonthNotification(int id) {
        final NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(TAG, id);

  }

  private void displayNotification(int id, Car car, Oil oil,String text) {
    final NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        sendNotificationNew(nm, id,car,oil,text);
      } else {
        sendNotification(nm, id, car, oil,text);
      }
//            else
//              if (ACTION_CANCEL_NOTIFICATION.equals(intent.getAction())) {
//              nm.cancel(TAG, (int) id);
//            } else if (ACTION_DISMISS_NOW.equals(intent.getAction())) {
//              new AlarmController(context).cancelNotification(alarm);
//            }




  }

  private void sendNotification(NotificationManager nm, int id, Car car, Oil oil,
      String noteText) {

    Context context=getApplicationContext();

    Intent notificationIntent = new Intent(context, NotificationActionsActivity.class);
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//    notificationIntent.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, car.getId());
    notificationIntent.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(NotificationActionsActivity.class);
    stackBuilder.addNextIntent(notificationIntent);

    PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

    //////////////////////

    Intent notificationIntent1 = new Intent(context, NextReminderFragment.class);
    notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    notificationIntent1.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);

    TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
    stackBuilder1.addParentStack(NextReminderFragment.class);
    stackBuilder1.addNextIntent(notificationIntent1);

    PendingIntent notificationPendingIntent1 = stackBuilder1.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

    //////////////////////

    Intent notificationIntent2 = new Intent(context, MileageFragment.class);
    notificationIntent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    notificationIntent2.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);
//    notificationIntent2.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, car.getId());

    TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
    stackBuilder2.addParentStack(MileageFragment.class);
    stackBuilder2.addNextIntent(notificationIntent2);

    PendingIntent notificationPendingIntent2 = stackBuilder2.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);



    Notification note = new NotificationCompat.Builder(context)
//        .setStyle(new NotificationCompat.BigTextStyle()
//            .bigText(noteText==null?"Oil -  " + oil.getBrand() + " " + oil.getType():noteText ))
        .setSmallIcon(R.drawable.icon_2)
        .setLargeIcon(BitmapFactory
            .decodeResource(App.getInstance().getApplicationContext().getResources(), car.getIcon()))
        .setColor(Color.YELLOW)
        .setPriority(Notification.PRIORITY_HIGH)
        .setContentTitle("Check your " + car.getCarBrand() + " engine oil")
        .setContentText(noteText)//        .setContentText("Oil -  " + oilBrand + " " + oilType)
        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
        .setSound(null)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(notificationPendingIntent)
////        .addAction(R.drawable.ic_update_black_24dp,"Remind",notificationPendingIntent1)
////        .addAction(R.drawable.ic_mode_edit_black_24dp,"Mileage",notificationPendingIntent2)
//        .addAction(R.drawable.ic_update_white_24dp,"Remind",notificationPendingIntent1)
//        .addAction(R.drawable.ic_mode_edit_white_24dp,"Mileage",notificationPendingIntent2)
        .build();
    nm.notify(TAG,  id, note);
  }

  private void sendNotificationNew(NotificationManager nm, int id, Car car,Oil oil,String noteText ) {

    Context context=getApplicationContext();

    Intent notificationIntent = new Intent(context, NotificationActionsActivity.class);
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//    notificationIntent.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, car.getId());
    notificationIntent.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(NotificationActionsActivity.class);
    stackBuilder.addNextIntent(notificationIntent);

    PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

    //////////////////////

    Intent notificationIntent1 = new Intent(context, NextReminderFragment.class);
    notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    notificationIntent1.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);

    TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
    stackBuilder1.addParentStack(NextReminderFragment.class);
    stackBuilder1.addNextIntent(notificationIntent1);

    PendingIntent notificationPendingIntent1 = stackBuilder1.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

    //////////////////////

    Intent notificationIntent2 = new Intent(context, MileageFragment.class);
    notificationIntent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    notificationIntent2.putExtra(EXTRA_NOTIFICATION_MESSAGE_ID, id);
//    notificationIntent2.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, car.getId());

    TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
    stackBuilder2.addParentStack(MileageFragment.class);
    stackBuilder2.addNextIntent(notificationIntent2);

    PendingIntent notificationPendingIntent2 = stackBuilder2.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);



    Notification note = new NotificationCompat.Builder(context,"1")
//        .setStyle(new NotificationCompat.BigTextStyle()
//            .bigText(noteText==null?"Oil -  " + oil.getBrand() + " " + oil.getType():noteText ))
        .setSmallIcon(R.drawable.icon_2)
        .setLargeIcon(BitmapFactory
            .decodeResource(App.getInstance().getApplicationContext().getResources(), car.getIcon()))
        .setColor(Color.YELLOW)
        .setPriority(Notification.PRIORITY_HIGH)
        .setContentTitle("Check your " + car.getCarBrand() + " engine oil")
        .setContentText(noteText)//        .setContentText("Oil -  " + oilBrand + " " + oilType)
        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
        .setSound(null)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(notificationPendingIntent)
////        .addAction(R.drawable.ic_update_black_24dp,"Remind",notificationPendingIntent1)
////        .addAction(R.drawable.ic_mode_edit_black_24dp,"Mileage",notificationPendingIntent2)
//        .addAction(R.drawable.ic_update_white_24dp,"Remind",notificationPendingIntent1)
//        .addAction(R.drawable.ic_mode_edit_white_24dp,"Mileage",notificationPendingIntent2)
        .build();
    nm.notify(TAG,  id, note);

  }

}
