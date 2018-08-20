package am.gsoft.carserviceclient.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks {


  @SuppressLint("LongLogTag")
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    Log.d("ActivityLifecycleHandler", "onActivityCreated " + activity.getClass().getSimpleName());
  }

  @SuppressLint("LongLogTag")
  public void onActivityStarted(Activity activity) {
    Log.d("ActivityLifecycleHandler", "onActivityStarted " + activity.getClass().getSimpleName());
  }

  @SuppressLint("LongLogTag")
  public void onActivityResumed(Activity activity) {
    Log.d("ActivityLifecycleHandler", "onActivityResumed " + activity.getClass().getSimpleName());
  }


  @SuppressLint("LongLogTag")
  public void onActivityPaused(Activity activity) {
    Log.d("ActivityLifecycleHandler", "onActivityPaused " + activity.getClass().getSimpleName());
  }

  @SuppressLint("LongLogTag")
  public void onActivityStopped(Activity activity) {
    Log.d("ActivityLifecycleHandler", "onActivityStopped " + activity.getClass().getSimpleName());
  }

  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @SuppressLint("LongLogTag")
  public void onActivityDestroyed(Activity activity) {
    Log.d("ActivityLifecycleHandler", "onActivityDestroyed " + activity.getClass().getSimpleName());
  }
}