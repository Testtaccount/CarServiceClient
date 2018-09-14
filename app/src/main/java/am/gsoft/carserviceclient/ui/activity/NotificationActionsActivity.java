package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_CAR_KEY;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_ID;
import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.notification.NotificationsIntentService;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.ui.fragment.MileageFragment;
import am.gsoft.carserviceclient.ui.fragment.NextReminderFragment;
import am.gsoft.carserviceclient.ui.fragment.NotificationActionsFragment;
import am.gsoft.carserviceclient.util.DateUtils;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import am.gsoft.carserviceclient.util.manager.FragmentTransactionManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NotificationActionsActivity extends BaseActivity implements View.OnClickListener,
    NotificationActionsFragment.OnNotificationActionsFragmentInteractionListener,
    NextReminderFragment.OnNextReminderFragmentInteractionListener,
    MileageFragment.OnMileageFragmentInteractionListener {

  private static final String TAG = NotificationActionsActivity.class.getSimpleName();
  private final long BEFORE_END_KM_MAX = 1000;
  private final long BEFORE_END_KM_MIN = 500;
  private final long DAYS_OF_MONTH = 30;
  private final long DAY_TO_MILLISECONDS = 86400000L;

  private ImageView carIconIv;
  private TextView carBrandTv;
  private TextView carModelTv;
  private TextView oilBrandTv;
  private TextView oilTypeTv;

  private NotificationsRepository mNotificationsRepository;
  private AppNotification mAppNotification;
  private Car mCar;
  private Oil mOil;
  private boolean appBarIsExpanded = true;

  private Calendar mSelectedDate = Calendar.getInstance();
  int count1 = 0;
  int count2 = 0;

  DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
        int dayOfMonth) {
      if (count1 % 2 == 0) {
        count1++;

        mSelectedDate.set(Calendar.YEAR, year);
        mSelectedDate.set(Calendar.MONTH, monthOfYear);
        mSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        openTimePicker();
      }
    }

  };

  TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      if (count2 % 2 == 0) {
        count2++;

        view.setTag("TAGGED");
        mSelectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mSelectedDate.set(Calendar.MINUTE, minute);

        updateDateLabel();
      }
    }
  };

  @Override
  protected int layoutResId() {
    return R.layout.activity_notification_actions;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();
    customizeActionBar();
    initFields();
    setListeners();

    openScreen(NotificationActionsFragment.newInstance(), false);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  private void findViews() {

    carIconIv = findViewById(R.id.car_icon);
    carBrandTv = findViewById(R.id.tv_car_brand);
    carModelTv = findViewById(R.id.tv_model);
    oilBrandTv = findViewById(R.id.tv_item_oil_brand);
    oilTypeTv = findViewById(R.id.tv_item_oil_type);
  }

  private void customizeActionBar() {
    setActionBarUpButtonEnabled(true);
    setActionBarTitle(getResources().getString(R.string.app_name));
  }

  private void initFields() {
//    mAlarmController = new AlarmController(this);
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
                  .get(mAppNotification.getCarKey());
              mOil = AppDatabase.getInstance(getApplicationContext()).mOilDao()
                  .get(mAppNotification.getOilKey());

              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  carIconIv.setImageDrawable(ContextCompat.getDrawable(App.getInstance(),
                      mCar.getIcon()));//setBackgroundResource(carBrandsList.getAppNotification(position).getIcon());
                  carBrandTv.setText(mCar.getCarBrand());
                  carModelTv.setText(mCar.getModel());
                  oilBrandTv.setText(mOil.getBrand());
                  oilTypeTv.setText(mOil.getType());
                }
              });

            } else {
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  carIconIv.setVisibility(View.GONE);
                  carBrandTv.setVisibility(View.GONE);
                  carModelTv.setVisibility(View.GONE);
                  oilBrandTv.setVisibility(View.GONE);
                  oilTypeTv.setVisibility(View.GONE);
                  Intent i = new Intent(NotificationActionsActivity.this, SplashActivity.class);
                  i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  startActivity(i);
                  finish();
                }
              });

            }

          }
        });

//        AlarmCursor cursor = new AlarmsTableManager(this).queryItem(id);
//        mAlarm = cursor.getItem();

//        mAlarmController.deletePreviusMonthlyNotificationAlarm(id);
      }
    }
  }

  private void setListeners() {

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);

  }

  @Override
  public void onBackPressed() {
    int count = getSupportFragmentManager().getBackStackEntryCount();

    if (count == 0) {
      Intent i = new Intent(NotificationActionsActivity.this, SplashActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      i.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_KEY, mCar.getKey());
      startActivity(i);
      finish();
      super.onBackPressed();
    } else {
      customizeActionBar();
      getSupportFragmentManager().popBackStack();
    }
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
      case R.id.fl_pick_btn:
        break;
      case R.id.fl_update_btn:
        break;
      default:
    }
  }

  private void openScreen(Fragment fragment, boolean mustAddToBackStack) {
    FragmentTransactionManager.displayFragment(
        getSupportFragmentManager(),
        fragment,
        R.id.fl_activity_notifications_container,
        mustAddToBackStack

    );
  }

  private void openDatePicker() {
//    new DatePickerDialog(this, mOnDateSetListener, mSelectedDate.getAppNotification(Calendar.YEAR),
//        mSelectedDate.getAppNotification(Calendar.MONTH),
//        mSelectedDate.getAppNotification(Calendar.DAY_OF_MONTH)).show();
//
    DatePickerDialog dpd1 = new DatePickerDialog(this, R.style.Datepicker, mOnDateSetListener,
        mSelectedDate.get(Calendar.YEAR), mSelectedDate.get(Calendar.MONTH),
        mSelectedDate.get(Calendar.DAY_OF_MONTH));
//        dpd1.setTitle("Select the date");
    dpd1.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {
//        openScreen(NotificationActionsFragment.newInstance(),false);
        onBackPressed();
        count1 = 0;
        count2 = 0;
      }
    });
    dpd1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    dpd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dpd1.show();
  }

  private void openTimePicker() {
//    new TimePickerDialog(this, mOnTimeSetListener, mSelectedDate.getAppNotification(Calendar.HOUR_OF_DAY),
//        mSelectedDate.getAppNotification(Calendar.MINUTE), true).show();
    TimePickerDialog dpd1 = new TimePickerDialog(this, R.style.Datepicker, mOnTimeSetListener,
        mSelectedDate.get(Calendar.HOUR_OF_DAY),
        mSelectedDate.get(Calendar.MINUTE), true);
//        dpd1.setTitle("Select the date");
    dpd1.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {
//        openScreen(NotificationActionsFragment.newInstance(),false);
        onBackPressed();
        count1 = 0;
        count2 = 0;
      }
    });
    dpd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dpd1.show();
  }

  private void updateDateLabel() {
//    emptyInfoLl.setVisibility(View.GONE);
//    reminderDateTxtCv.setVisibility(View.VISIBLE);
    long t = mSelectedDate.getTimeInMillis();
    NextReminderFragment fragment = (NextReminderFragment) getSupportFragmentManager()
        .findFragmentByTag(NextReminderFragment.TAG);
    if (fragment != null) {
      fragment.setReminderText(DateUtils.longToString(t, getDateFormat(DateType.DMY_HM)));
    }
    setNotification(AppNotification.TYPE_REMIND, t);
    count1 = 0;
    count2 = 0;
  }

  private void updateMileage(String value) {
//    ToastUtils.shortToast("Oil" + mOil);
    if (!checkInputs(value)) {
      return;
    }

    long enteredValue = Long.parseLong(value);
    long recomendedKm = mOil.getRecomendedKm();//85

//    if (recomendedKm > BEFORE_END_KM_MAX) {
//      beforeEndKm = 1000;
//    }else if (BEFORE_END_KM_MIN < recomendedKm && recomendedKm < BEFORE_END_KM_MAX) {
//      beforeEndKm = 500;
//    } else
    long beforeEndKm;

    long driven = enteredValue - mOil.getServiceDoneKm();
    if (driven >= recomendedKm) {
      ToastUtils.shortToast(R.string.msg_time_to_change_oil);
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

      if (mid > 0) {
        daysBefore = beforeEndKm / mid;
      }
    }

    long d = ((recomendedKm * DAYS_OF_MONTH) / driven) - daysBefore;

    long t = System.currentTimeMillis() + d * DAY_TO_MILLISECONDS;

    MileageFragment fragment = (MileageFragment) getSupportFragmentManager()
        .findFragmentByTag(MileageFragment.TAG);
    if (fragment != null) {
      fragment.setReminderText(DateUtils.longToString(t, getDateFormat(DateType.DMY_HM)));
    }
    setNotification(AppNotification.TYPE_MILEAGE, t);

  }

  private boolean checkInputs(String enteredValue) {
    DialogManager.getInstance().showPreloader(this);
    MileageFragment fragment = (MileageFragment) getSupportFragmentManager()
        .findFragmentByTag(MileageFragment.TAG);

    if (TextUtils.isEmpty(enteredValue)) {
      if (fragment != null) {
        fragment.setError("Enter yor driven data");
      }
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (Long.valueOf(enteredValue) <= mOil.getServiceDoneKm()) {
      if (fragment != null) {
        fragment.setError(
            "Current " + mCar.getDistanceUnit() + " must be more than the previous (" + mOil
                .getServiceDoneKm() + ") service " + mCar.getDistanceUnit() + " !");
      }
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    DialogManager.getInstance().dismissPreloader(this.getClass());
    return true;
  }

  private void setNotification(int notificationType, long t) {
    if (mAppNotification != null) {
      switch (notificationType) {
        case AppNotification.TYPE_REMIND:
          mNotificationsRepository.setReminderNotification( mOil, t);
          break;
        case AppNotification.TYPE_MILEAGE:
          mNotificationsRepository.setMileageNotification( mOil, t);
          break;
      }
    }
  }

  @Override
  public void onUpdateCalendarClick() {
    openDatePicker();
  }

  @Override
  public void onSetMileageClick(String value) {
    updateMileage(value);
  }

  @Override
  public void onUpdateMileageClick() {

  }

  @Override
  public void onCalendarClick() {
    openDatePicker();
    openScreen(NextReminderFragment.newInstance(), true);
  }

  @Override
  public void onMileageClick() {
    openScreen(MileageFragment.newInstance(mCar), true);
  }
}
