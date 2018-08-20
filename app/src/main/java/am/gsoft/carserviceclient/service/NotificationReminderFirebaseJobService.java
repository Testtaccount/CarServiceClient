package am.gsoft.carserviceclient.service;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.ui.activity.SplashActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NotificationReminderFirebaseJobService extends JobService {

  @Override
  public boolean onStartJob(JobParameters job) {
    sendNotification("text text text");

    return false;
  }

  @Override
  public boolean onStopJob(JobParameters job) {
    return false;
  }

  private void sendNotification(String notificationDetails) {
    Log.d("testt", "sendNotification: " + notificationDetails);
    // Create an explicit content Intent that starts the main Activity.
    Intent notificationIntent = new Intent(App.getInstance(), SplashActivity.class);

    // Construct a task stack.
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getInstance());

    // Add the main Activity to the task stack as the parent.
    stackBuilder.addParentStack(SplashActivity.class);

    // Push the content Intent onto the stack.
    stackBuilder.addNextIntent(notificationIntent);

    // Get a PendingIntent containing the entire back stack.
    PendingIntent notificationPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    // Get a notification builder that's compatible with platform versions >= 4
    NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getInstance(),"1");

    // Define the notification settings.
    builder.setSmallIcon(R.drawable.icon_2)
        // In a real app, you may want to use a library like Volley
        // to decode the Bitmap.
        .setLargeIcon(BitmapFactory.decodeResource(App.getInstance().getResources(),
            R.drawable.bmw_logo))
        .setColor(Color.YELLOW)
        .setContentTitle(notificationDetails)
        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
        .setSound(null)
        .setChannelId("1")
        .setDefaults(
            Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
        .setContentText(App.getInstance().getString(R.string.text_welcome))
        .setContentIntent(notificationPendingIntent);

    // Dismiss notification once the user touches it.
    builder.setAutoCancel(true);

    // Get an instance of the Notification manager
    NotificationManager mNotificationManager =
        (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    // Issue the notification
    mNotificationManager.notify(0, builder.build());

  }

}

