package am.gsoft.carserviceclient.util.manager;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.ui.activity.SplashActivity;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
 import android.graphics.drawable.Icon;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

public final class ShortcutManager {

//  /**
//   * android O 添加桌面快捷方式
//   */
//  @RequiresApi(api = Build.VERSION_CODES.O)
//  public static void addShortCut(Context context) {
//    android.content.pm.ShortcutManager shortcutManager =
//        context.getSystemService(android.content.pm.ShortcutManager.class);
//    if (shortcutManager.isRequestPinShortcutSupported()) {
//      Intent shortcutInfoIntent = new Intent(context, SplashActivity.class);
//      shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错
//
//      ShortcutInfo info = new ShortcutInfo.Builder(context, "The only id")
//          .setIcon(Icon.createWithResource(context, R.drawable.icon_2))
//          .setShortLabel(context.getString(R.string.app_name))
//          .setIntent(shortcutInfoIntent)
//          .build();
//
//      PendingIntent shortcutCallbackIntent = PendingIntent
//          .getBroadcast(context, 0, new Intent(context, OnBootUpReceiver.class),
//              PendingIntent.FLAG_UPDATE_CURRENT);
//
//      shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
//    }
//
//  }
//
//  /**
//   * 使用ShortcutManagerCompat添加桌面快捷方式
//   */
//  public static void addShortCutCompact(Context context) {
//    if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
//      Intent shortcutInfoIntent = new Intent(context, SplashActivity.class);
//      shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错
//
//      ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "The only id")
//          .setIcon(IconCompat.createWithResource(context, R.drawable.icon_2))
//          .setShortLabel("Short Label")
//          .setIntent(shortcutInfoIntent)
//          .build();
//
//      //当添加快捷方式的确认弹框弹出来时，将被回调
//      PendingIntent shortcutCallbackIntent = PendingIntent
//          .getBroadcast(context, 0, new Intent(context, OnBootUpReceiver.class),
//              PendingIntent.FLAG_UPDATE_CURRENT);
//      ShortcutManagerCompat
//          .requestPinShortcut(context, info, shortcutCallbackIntent.getIntentSender());
//    }
//  }


  /**
   * Android 7.1及以下 添加桌面
   *
   * @param context
   */
  public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

  public  static void addShortcutBelowAndroidN(Context context) {
    Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

    // 不允许重复创建，不是根据快捷方式的名字判断重复的
    addShortcutIntent.putExtra("duplicate", false);

    addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Shortcut Name");

    //图标
    addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        Intent.ShortcutIconResource.fromContext(context, R.drawable.icon_2));

    // 设置关联程序
    Intent launcherIntent = new Intent();
    launcherIntent.setClass(context, SplashActivity.class);
    addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

    // 发送广播
    context.sendBroadcast(addShortcutIntent);
  }

  @TargetApi(VERSION_CODES.O)
  @RequiresApi(api = VERSION_CODES.O)
  public static void addPinnedShortcuts(Context context) {
    android.content.pm.ShortcutManager mShortcutManager = context.getSystemService(android.content.pm.ShortcutManager.class);
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      if (mShortcutManager != null && mShortcutManager.isRequestPinShortcutSupported()) {

        Intent shortcutInfoIntent = new Intent(context, SplashActivity.class);
        shortcutInfoIntent.setAction(Intent.ACTION_VIEW);

        ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(context.getApplicationContext(),"The only id")
            .setIcon(Icon.createWithResource(context, R.drawable.icon_2))
            .setShortLabel(context.getString(R.string.app_name))
            .setIntent(shortcutInfoIntent)
            .build();

        Intent pinnedShortcutCallbackIntent = mShortcutManager.createShortcutResultIntent(pinShortcutInfo);

//Get notified when a shortcut is pinned successfully//

        PendingIntent successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, 0);
        mShortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
      }
    }
  }



  public static void addShortcutIcon(Context context) {
    //shorcutIntent object
    Intent shortcutIntent = new Intent(context, SplashActivity.class);

    shortcutIntent.setAction(Intent.ACTION_MAIN);
    //shortcutIntent is added with addIntent
    Intent addIntent = new Intent();
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        Intent.ShortcutIconResource.fromContext(context, R.drawable.icon_2));
    addIntent.putExtra("duplicate", false);
    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    // finally broadcast the new Intent
    context.sendBroadcast(addIntent);
  }
}
