package am.gsoft.carserviceclient.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import java.util.concurrent.TimeUnit;

public class MyFirebaseJobDispatcher {

  private static final int REMINDER_INTERVAL_MINUTES = 5;
  private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
  private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

  private static final String SEND_NOTIFICATION_JOB = "SEND_NOTIFICATION_JOB";

  public static void startNotificationJob(@NonNull final Context context) {
    Driver driver = new GooglePlayDriver(context);
    FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

    Job job = firebaseJobDispatcher
        .newJobBuilder()
        .setService(NotificationReminderFirebaseJobService.class)
        .setTag(SEND_NOTIFICATION_JOB)
        .setRecurring(true)
        .setLifetime(Lifetime.FOREVER)
//        .setTrigger(Trigger.executionWindow(
//            REMINDER_INTERVAL_SECONDS,
//            REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
//        ))
        .setTrigger(Trigger.executionWindow(0, 2))
        .setReplaceCurrent(true)
        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
        .setConstraints(Constraint.ON_ANY_NETWORK)
        .build();

    firebaseJobDispatcher.schedule(job);
  }

  public static void stopSendKidLocationJob(Context context) {

    Driver driver = new GooglePlayDriver(context);
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
    dispatcher.cancel(SEND_NOTIFICATION_JOB);
    Log.d("testt", "$$$ SendKidLocationJobService background job stopped");
  }

}
