package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_ID;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_NOTIFICATION_MESSAGE_CAR_ID;
import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.notification.NotificationsIntentService;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.util.DateUtils;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class NextReminderActivity extends BaseActivity implements OnClickListener {

  private FrameLayout pickFlBtn;
  private TextView reminderTxtTv;
  private CardView reminderDateTxtCv;
  private LinearLayout emptyInfoLl;
  private ImageView carIconIv;
  private TextView carBrandTv;
  private TextView carModelTv;
  private TextView oilBrandTv;
  private TextView oilTypeTv;

  private NotificationsRepository mNotificationsRepository;
  private AppNotification mAppNotification;
  private Car mCar;
  private Oil mOil;
  private Calendar mSelectedDate = Calendar.getInstance();
  int count1 = 0;
  int count2 = 0;

  @Override
  protected int layoutResId() {
    return R.layout.activity_next_reminder;
  }

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
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();
    customizeActionBar();
    initFields();
    setListeners();
  }

  private void findViews() {
    pickFlBtn = findViewById(R.id.fl_pick_btn);
    reminderTxtTv = findViewById(R.id.tv_reminder_txt);
    reminderDateTxtCv = findViewById(R.id.cv_reminder_date_txt);
    emptyInfoLl = findViewById(R.id.ll_empty_info);

    carIconIv = findViewById(R.id.car_icon);
    carBrandTv = findViewById(R.id.tv_car_brand);
    carModelTv = findViewById(R.id.tv_model);
    oilBrandTv = findViewById(R.id.tv_item_oil_brand);
    oilTypeTv = findViewById(R.id.tv_item_oil_type);
  }

  private void customizeActionBar() {
    setActionBarUpButtonEnabled(true);
    setActionBarTitle("Next Reminder");
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
              Intent i = new Intent(NextReminderActivity.this, SplashActivity.class);
              i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(i);
              finish();
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
    pickFlBtn.setOnClickListener(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
//    openDatePicker();
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
        openDatePicker();
        break;
//      case R.id.btn_time:
////        showDatePickerDialog(to_dateListener, DATE_PICKER_TO);
//        break;
    }
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
        count1 = 0;
        count2 = 0;
      }
    });
    dpd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dpd1.show();
  }

  private void updateDateLabel() {
    emptyInfoLl.setVisibility(View.GONE);
    reminderDateTxtCv.setVisibility(View.VISIBLE);
    long t = mSelectedDate.getTimeInMillis();
    reminderTxtTv.setText(DateUtils.longToString(t, getDateFormat(DateType.DMY_HM)));
    if (mAppNotification != null) {
//      mAlarmController.updateAlarm(mAlarm.getId(),mSelectedDate.getTimeInMillis(),mAlarm);
//      mAlarmController.deletePreviusMonthlyNotificationAlarm(mAlarm);
//      mAlarmController.setupMonthlyNotificationsAlarmLong2(mSelectedDate.getTimeInMillis(),mAlarm);

//      long id = mAppNotification.getId();
//      long carId = mAppNotification.getCarId();
//      long oilId = mAppNotification.getOilId();
//
//
//
//      AppExecutors.getInstance().diskIO().execute(new Runnable() {
//        @Override
//        public void run() {
//          Car car=AppDatabase.getInstance(getApplicationContext()).mCarDao().get(carId);
//          Oil oil=AppDatabase.getInstance(getApplicationContext()).mOilDao().get(oilId);
      mNotificationsRepository.setReminderNotification(mCar, mOil, t);
//        }
//      });

    }
    count1 = 0;
    count2 = 0;
  }

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
    Intent i = new Intent(NextReminderActivity.this, SplashActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    i.putExtra(EXTRA_NOTIFICATION_MESSAGE_CAR_ID, mCar.getId());
    startActivity(i);
    finish();
    super.onBackPressed();
  }
}
