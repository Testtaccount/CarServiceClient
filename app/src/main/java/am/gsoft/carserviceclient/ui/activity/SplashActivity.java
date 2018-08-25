package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_MAIN_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_CAR_ID;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.ui.activity.main.MainActivity;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.VersionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected int layoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VersionUtils.isAfter21() ) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
        // if user isn't authorized then suggest him to authorize
        if (!appSharedHelper.isSavedRememberMe()) {
            startLandingActivity();
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                  int  c = AppDatabase.getInstance(getApplicationContext()).mCarDao().getCount();
                    if(c!=0) {

                        Intent intent = getIntent();
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            if (extras.containsKey(EXTRA_NOTIFICATION_MESSAGE_CAR_ID)) {
                                // extract the extra-data in the Notification
                                long carId = extras.getLong(EXTRA_NOTIFICATION_MESSAGE_CAR_ID);

//                    int p = CarDataDbHalper.getInstance().getCarPosition(carId);
                                appSharedHelper.saveSpinnerPosition(carId);
                            }
                        }
                        startMainActivity();
                    }else {
                      runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          ToastUtils.shortToast(R.string.msg_no_cars);
                        }
                      });
                        Intent intent = new Intent(SplashActivity.this, CreateNewCarActivity.class);
                        intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });

        }

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void startLandingActivity() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
        finish();
    }

}