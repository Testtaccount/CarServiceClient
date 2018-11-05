package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.manager.ShortcutManager.addPinnedShortcuts;

import am.gsoft.carserviceclient.R;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class ShortcutService extends Service {


  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        addPinnedShortcuts(getApplicationContext());
      }

    Notification notification = new NotificationCompat.Builder(this, "1")
        .setContentTitle("Adding Shortcut")
         .setSmallIcon( R.drawable.icon_2)
        .build();
    startForeground(1, notification);
    stopForeground(true);
    //do heavy work on a background thread
    //stopSelf();

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}
