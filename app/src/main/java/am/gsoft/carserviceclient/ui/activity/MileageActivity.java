package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_ID;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_CAR_ID;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.notification.NotificationsIntentService;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.util.AppUtil;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class MileageActivity extends BaseActivity implements OnClickListener {

  private final long BEFORE_END_KM_MAX = 1000;
  private final long BEFORE_END_KM_MIN = 500;
  private final long DAYS_OF_MONTH = 30;
  private final long DAY_TO_MILLISECONDS = 86400000L;

  private LinearLayout root;
  private LinearLayout mLayout;
  private TextInputLayout serviceDoneKmTil;
  private EditText newMileageEt;
  private TextView km1Tv;
  private FrameLayout updateFlBtn;
  private ImageView carIconIv;
  private TextView carBrandTv;
  private TextView carModelTv;
  private TextView oilBrandTv;
  private TextView oilTypeTv;

  private NotificationsRepository mNotificationsRepository;
  private AppNotification mAppNotification;
  private Car mCar;
  private Oil mOil;
  private boolean keyboardActive;

  @Override
  protected int layoutResId() {
    return R.layout.activity_mileage;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();
    customizeActionBar();
    initFields();
    setListeners();
//    CardView cv = findViewById(R.id.cv_update_mileage);
//    AdjustingViewGlobalLayoutListener listen = new AdjustingViewGlobalLayoutListener(mLayout);
//    root.getViewTreeObserver().addOnGlobalLayoutListener(listen);

  }

  private void findViews() {
    root = findViewById(R.id.root_update_mileage);
//    mLayout = findViewById(R.id.wraper);

    serviceDoneKmTil = (TextInputLayout) findViewById(R.id.til_updated_km);
    newMileageEt = findViewById(R.id.et_new_mileage);
    km1Tv = (TextView) findViewById(R.id.tv_km);
    updateFlBtn = findViewById(R.id.fl_update_btn);

    carIconIv = findViewById(R.id.car_icon);
    carBrandTv = findViewById(R.id.tv_car_brand);
    carModelTv = findViewById(R.id.tv_model);
    oilBrandTv = findViewById(R.id.tv_item_oil_brand);
    oilTypeTv = findViewById(R.id.tv_item_oil_type);

  }

  private void customizeActionBar() {
    setActionBarUpButtonEnabled(true);
    setActionBarTitle("Update Mileage");
  }

  private void initFields() {
     mNotificationsRepository = InjectorUtils.provideNotificationRepository(getApplicationContext());
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      if (extras.containsKey(EXTRA_NOTIFICATION_MESSAGE_ID)) {
        // extract the extra-data in the Notification

        int id = extras.getInt(EXTRA_NOTIFICATION_MESSAGE_ID);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
          @Override
          public void run() {
            mAppNotification = AppDatabase.getInstance(getApplicationContext())
                .mAppNotificationDao().getAppNotification(id);
            if (mAppNotification != null) {
              long id = mAppNotification.getId();

              mCar = AppDatabase.getInstance(getApplicationContext()).mCarDao()
                  .get(mAppNotification.getCarId());
              mOil = AppDatabase.getInstance(getApplicationContext()).mOilDao()
                  .get(mAppNotification.getOilId());
              carIconIv.setImageDrawable(ContextCompat.getDrawable(App.getInstance(),
                  mCar.getIcon()));//setBackgroundResource(carBrandsList.getAppNotification(position).getIcon());
              carBrandTv.setText(mCar.getCarBrand());
              carModelTv.setText(mCar.getModel());
              oilBrandTv.setText(mOil.getBrand());
              oilTypeTv.setText(mOil.getType());
            } else {
              carIconIv.setVisibility(View.GONE);
              carBrandTv.setVisibility(View.GONE);
              carModelTv.setVisibility(View.GONE);
              oilBrandTv.setVisibility(View.GONE);
              oilTypeTv.setVisibility(View.GONE);
              Intent i = new Intent(MileageActivity.this, SplashActivity.class);
              i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(i);
              finish();
            }

          }
        });

      }
    }

    if (mCar != null) {
      km1Tv.setText(mCar.getDistanceUnit());
//      List<Oil> mOils = App.getAppSharedHelper().getSaveOilListByCarKey(mCar.getKey());
//      if (mOils != null) {
//        for (int i = 0; i < mOils.size(); i++) {
//          if (mAlarm.key().equals(mOils.get(i).getKey())) {
//            mOil = mOils.get(i);
//          }
//        }
//      }
    }

    if (mOil != null) {

    }
  }

  private void setListeners() {
    updateFlBtn.setOnClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    // clear the notification area when the app is opened
    NotificationManager notificationManager = (NotificationManager) getSystemService(
        Context.NOTIFICATION_SERVICE);
    if (mAppNotification != null) {
      notificationManager.cancel(NotificationsIntentService.TAG, mAppNotification.getId());
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_update_btn:
        AppUtil.closeKeyboard(this);
        update();
        break;
    }
  }

  private void update() {
//    ToastUtils.shortToast("Oil" + mOil);
    if (!checkInputs()) {
      return;
    }

    long enteredValue = Long.parseLong(newMileageEt.getText().toString());
    long recomendedKm = mOil.getRecomendedKm();//85

//    if (recomendedKm > BEFORE_END_KM_MAX) {
//      beforeEndKm = 1000;
//    }else if (BEFORE_END_KM_MIN < recomendedKm && recomendedKm < BEFORE_END_KM_MAX) {
//      beforeEndKm = 500;
//    } else
    long beforeEndKm;

    long driven = enteredValue - mOil.getServiceDoneKm();
    if (driven >= recomendedKm) {
      ToastUtils.shortToast("Time To Change Oil");
      return;
    }

    if (recomendedKm <= BEFORE_END_KM_MIN) {
      beforeEndKm = recomendedKm / 2;
    } else if (recomendedKm <= BEFORE_END_KM_MAX) {
      beforeEndKm = 500;
    } else {
      beforeEndKm = 1000;
    }

//    long sum = App.getAppSharedHelper().getPreviusDrivenSum(mCar.getId(),mOil.getId());

    long td = System.currentTimeMillis() - mOil.getServiceDoneDate();
    long days = TimeUnit.MILLISECONDS.toDays(td);

    long daysBefore = 10;
    if (driven > 0 && days > 0) {
//      sum += driven

      long mid = driven / days;

      daysBefore = beforeEndKm / mid;

    }

    long d = ((recomendedKm * DAYS_OF_MONTH) / driven) - daysBefore;

    long t = System.currentTimeMillis() + d * DAY_TO_MILLISECONDS;

//    if(driven>0) {
//      sum += driven;
//
//      daysBefore = ((DAYS_OF_MONTH * beforeEndKm) / driven)* DAY_TO_MILLISECONDS;
//    }

//    if (500 < diff && diff <= 1999) {
//      if (mAlarm != null) {
////        mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
////        mAlarmController.setupMonthlyNotificationsAlarmLong2(t, mAlarm);
//      }
//    } else if (0 < diff && diff <= 500) {
//      if (mAlarm != null) {
////        mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
////        mAlarmController.setupMonthlyNotificationsAlarmLong2(t, mAlarm);
//      }
//    } else if (diff <= 0) {
//      if (mAlarm != null) {
////        mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
////        mAlarmController.setupMonthlyNotificationsAlarmLong2(t, mAlarm);
//      }
//    } else {

//      long t = System.currentTimeMillis()+((recomendedKm /sum) * DAYS_OF_MONTH * DAY_TO_MILLISECONDS-daysBefore);

    setNotification(t);

//    }

//    App.getAppSharedHelper().setPreviusDrivenSum(mCar.getId(),mOil.getId(),sum);
    serviceDoneKmTil.setErrorEnabled(false);
  }

  private void setNotification(long t) {

//    if (mAlarm != null) {
////      mAlarmController.updateAlarm(mAlarm.getId(),mSelectedDate.getTimeInMillis(),mAlarm);
//      mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
//      mAlarmController.setupMonthlyNotificationsAlarmLong2(t, mAlarm);
//    }

    if (mAppNotification != null) {
//      mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
      mNotificationsRepository.setMileageNotification(mCar, mOil, t);
    }
  }

//  private long calcNextTime(long recomemdKM, long enteredValue) {
//
//
//  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
//        finish();
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    Intent i = new Intent(MileageActivity.this, SplashActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    i.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, mCar.getId());
    startActivity(i);
    finish();
    super.onBackPressed();
  }

  private boolean checkInputs() {
    DialogManager.getInstance().showPreloader(this);

    if (TextUtils.isEmpty(newMileageEt.getText().toString())) {
      serviceDoneKmTil.setErrorEnabled(true);
      serviceDoneKmTil.setError("Enter yor driven data");
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (Long.valueOf(newMileageEt.getText().toString()) <= mOil.getServiceDoneKm()) {
      serviceDoneKmTil.setErrorEnabled(true);
      serviceDoneKmTil.setError("Current "+mCar.getDistanceUnit()+" must be more than the previous ("+mOil.getServiceDoneKm()+") service "+mCar.getDistanceUnit()+" !");
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    DialogManager.getInstance().dismissPreloader(this.getClass());
    return true;
  }
}
