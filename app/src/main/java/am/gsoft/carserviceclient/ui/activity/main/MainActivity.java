package am.gsoft.carserviceclient.ui.activity.main;

import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_CREATE_NEW_OIL_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_EDIT_OIL_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_OIL_HISTORY_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_CAR;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_OIL;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_OIL_LIST;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_FIRST_OIL;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_INDEX_OF_CURRENT_OIL;
import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;
import static am.gsoft.carserviceclient.util.DateUtils.longToString;
import static android.Manifest.permission.CALL_PHONE;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.firebase.FirebaseAuthHelper;
import am.gsoft.carserviceclient.phone.PhoneNumber;
import am.gsoft.carserviceclient.phone.PhoneNumberUtils;
import am.gsoft.carserviceclient.ui.activity.CreateNewOilActivity;
import am.gsoft.carserviceclient.ui.activity.EditOilActivity;
import am.gsoft.carserviceclient.ui.activity.GarageActivity;
import am.gsoft.carserviceclient.ui.activity.OilHistoryActivity;
import am.gsoft.carserviceclient.ui.activity.SplashActivity;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.ui.adapter.MyCarSpinnerAdapter;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import am.gsoft.carserviceclient.util.Logger;
import am.gsoft.carserviceclient.util.VersionUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import am.gsoft.carserviceclient.util.manager.FragmentTransactionManager;
import am.gsoft.carserviceclient.util.manager.SnackBarManager;
import android.Manifest.permission;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {

  private static final String TAG = MainActivity.class.getSimpleName();
  private static final int REQUEST_READ_PHONE_STATE = 7788;

  private TextView oilCompanyIdTv;
  private TextView oilServiveDoneDateTv;
  private TextView oilServiveNextDateTv;
  private TextView oilBrandTv;
  private TextView oilTypeTv;
  private TextView oilVolumeTv;
  private TextView serviceDoneKmTv;
  private TextView nextServiceKmTv;
  private TextView recomendedKmTv;
  private TextView middleMonthKmTv;
  private TextView notesTextTv;
  private TextView km1Tv;
  private TextView km2Tv;
  private TextView km3Tv;
  private TextView km4Tv;
  private AppBarLayout appBarLayout;
  private NestedScrollView nestedScrollView;
  private Space spaceView;
  private Spinner spinner;
  private LinearLayout newOilBtnLl;
  private LinearLayout editOilBtnLl;
  private LinearLayout oilHistoryBtnLl;
  private FrameLayout garageActionBtnFl;
  private LinearLayout contentLl;
  private RelativeLayout progressRl;
  private TextView changeOilTxtTv;
  private TextView garageActionTxtTv;
  private LinearLayout emptyInfoLl;
  private LinearLayout mainContentLl;
  private ImageView callBtnIv;
  private CardView oilInfoCv;
  private CardView kmInfoCv;
  private CardView dateInfoCv;

  private LiveData<List<Car>> mCarLiveData;
  private List<Car> mCarList;
  private LiveData<List<Oil>> mOilLiveData;
  private List<Oil> mOilList;

  private MyCarSpinnerAdapter myCarSpinnerAdapter;
  private Car currentCar;
  private Oil currentOil;
  private Oil firstOil;
  private boolean showProgress;


  @Override
  protected int layoutResId() {
    return R.layout.activity_main;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    findViews();
    customizeActionBar();
    initFields();

    mCarLiveData = AppDatabase.getInstance(getApplicationContext()).mCarDao().getAll();
    mCarLiveData.observe(this, new Observer<List<Car>>() {
      @Override
      public void onChanged(@Nullable List<Car> cars) {
        if (cars != null && cars.size() != 0) {
          mCarList = cars;
          initSpinner(cars);
          setListeners();
          spinner.setSelection(getPosition(cars, appSharedHelper.getSpinnerPosition()), true);

//          progressRl.setVisibility(View.GONE);
//          contentLl.setVisibility(View.VISIBLE);

          if (getIntent() != null && getIntent().getAction() != null) {
            if (getIntent().getAction().equals(ACTION_OIL_HISTORY_ACTIVITY_INTENT)
                || getIntent().getAction().equals(ACTION_CREATE_NEW_OIL_ACTIVITY_INTENT)
                || getIntent().getAction().equals(ACTION_EDIT_OIL_ACTIVITY_INTENT)) {
//          appBarLayout.setExpanded(false);
//          scrollToView(nestedScrollView, spaceView);
//          focusOnView(nestedScrollView, spaceView);

            }
          }
        } else {

//          ToastUtils.shortToast("Yo Don't Have Cars");
//          Intent intent = new Intent(MainActivity.this, SplashActivity.class);
//          intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//          startActivity(intent);
//          finish();
//          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
      }
    });

//    LiveData<List<Oil>> m=AppDatabase.getInstance(getApplicationContext()).mOilDao().getAll();
//    m.observeForever(new Observer<List<Oil>>() {
//      @Override
//      public void onChanged(@Nullable List<Oil> oils) {
//        ToastUtils.shortToast(""+oils.size());
//
//      }
//    });

  }

  private void findViews() {
    appBarLayout = findViewById(R.id.appBar);
    nestedScrollView = findViewById(R.id.nscw);
    spaceView = findViewById(R.id.space);
    spinner = (Spinner) findViewById(R.id.spinner);
    newOilBtnLl = (LinearLayout) findViewById(R.id.ll_new_oil_btn);
    editOilBtnLl = (LinearLayout) findViewById(R.id.ll_edit_oil_btn);
    oilHistoryBtnLl = (LinearLayout) findViewById(R.id.ll_oil_history_btn);
    garageActionBtnFl = (FrameLayout) findViewById(R.id.fl_garage_action_btn);
    contentLl = (LinearLayout) findViewById(R.id.ll_content);
    progressRl = (RelativeLayout) findViewById(R.id.rl_progress);

    changeOilTxtTv = (TextView) findViewById(R.id.tv_new_oil_txt);
    garageActionTxtTv = (TextView) findViewById(R.id.tv_garage_action_txt);

    oilCompanyIdTv = (TextView) findViewById(R.id.tv_oil_company_id);
    oilServiveDoneDateTv = (TextView) findViewById(R.id.tv_oil_service_done_date);
    oilServiveNextDateTv = (TextView) findViewById(R.id.tv_oil_service_next_date);
    oilBrandTv = (TextView) findViewById(R.id.tv_oil_brand);
    oilTypeTv = (TextView) findViewById(R.id.tv_oil_type);
    oilVolumeTv = (TextView) findViewById(R.id.et_oil_volume);
    serviceDoneKmTv = (TextView) findViewById(R.id.tv_service_done_km);
    nextServiceKmTv = (TextView) findViewById(R.id.tv_next_service_km);
    recomendedKmTv = (TextView) findViewById(R.id.tv_recomended_km);
    middleMonthKmTv = (TextView) findViewById(R.id.tv_middle_month_km);
    notesTextTv = (TextView) findViewById(R.id.tv_notes);
    mainContentLl = (LinearLayout) findViewById(R.id.ll_main_content);
    callBtnIv = (ImageView) findViewById(R.id.iv_call_btn);
    km1Tv = (TextView) findViewById(R.id.tv_km1);
    km2Tv = (TextView) findViewById(R.id.tv_km);
    km3Tv = (TextView) findViewById(R.id.tv_km3);
    km4Tv = (TextView) findViewById(R.id.tv_km4);

    emptyInfoLl = (LinearLayout) findViewById(R.id.ll_empty_info);
    oilInfoCv = (CardView) findViewById(R.id.cv_oil_info);
    kmInfoCv = (CardView) findViewById(R.id.cv_km_info);
    dateInfoCv = (CardView) findViewById(R.id.cv_date_info);

  }

  private void customizeActionBar() {
//     setActionBarTitle(getString(R.string.app_name));
  }

  private void initFields() {
    contentLl.setVisibility(View.GONE);
    progressRl.setVisibility(View.VISIBLE);

    showProgress = false;
    //appSharedHelper.isEnablePushNotifications();
//    mCarLiveData = appSharedHelper.getCarListToSave();
    mCarList = new ArrayList<>();
    mOilList = new ArrayList<>();
//    garageActionBtnFl.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    setTextViewDrawableColor(changeOilTxtTv, R.color.white);
    setTextViewDrawableColor(garageActionTxtTv, R.color.white);

    setTextViewDrawableColor(oilServiveDoneDateTv, R.color.black);
    setTextViewDrawableColor(oilServiveNextDateTv, R.color.black);

    int[] attrs = new int[]{R.attr.selectableItemBackground};
    TypedArray typedArray = this.obtainStyledAttributes(attrs);
    int backgroundResource = typedArray.getResourceId(0, 0);
    newOilBtnLl.setBackgroundResource(backgroundResource);
    editOilBtnLl.setBackgroundResource(backgroundResource);
    oilHistoryBtnLl.setBackgroundResource(backgroundResource);
//    garageActionBtnFl.setBackgroundResource(backgroundResource);
    typedArray.recycle();

  }

  private void setListeners() {
    newOilBtnLl.setOnClickListener(this);
    editOilBtnLl.setOnClickListener(this);
    oilHistoryBtnLl.setOnClickListener(this);
    garageActionBtnFl.setOnClickListener(this);
    callBtnIv.setOnClickListener(this);
    spinner.setOnItemSelectedListener(this);

    findViewById(R.id.scroll).setOnTouchListener(new View.OnTouchListener() {
      public boolean onTouch(View p_v, MotionEvent p_event) {
        // this will disallow the touch request for parent scroll on touch of child view
        p_v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
      }
    });

  }

  private int getPosition(List<Car> cars, long carId) {
    int position = 0;
    for (int i = 0; i < cars.size(); i++) {
      if (cars.get(i).getId() == carId) {
        position = i;
        break;
      }
    }
    return position;
  }

  @Override
  public void onResume() {
    super.onResume();
    // clear the notification area when the app is opened
//    NotificationUtils.clearNotifications(getApplicationContext());
  }

  @Override
  protected void onPause() {
    super.onPause();
//    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  private void initSpinner(List<Car> cars) {
    myCarSpinnerAdapter = new MyCarSpinnerAdapter(this, cars);
    spinner.setAdapter(myCarSpinnerAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_garage_action_btn: {
        Intent i = new Intent(MainActivity.this, GarageActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        break;
      }
      case R.id.ll_new_oil_btn: {
        Intent i = new Intent(MainActivity.this, CreateNewOilActivity.class);
        i.putExtra(EXTRA_CURRENT_CAR, currentCar);
        i.putExtra(EXTRA_FIRST_OIL, firstOil);
        i.putExtra(EXTRA_CURRENT_OIL, currentOil);
//        i.putExtra("indexOfCurrentCar", mCarList.indexOf(currentCar));// chack list size
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.down_slide_enter, R.anim.up_slide_exit);

        break;
      }
      case R.id.ll_edit_oil_btn: {
        Intent i = new Intent(MainActivity.this, EditOilActivity.class);
        i.putExtra(EXTRA_CURRENT_CAR, currentCar);
        i.putExtra(EXTRA_FIRST_OIL, firstOil);
        i.putExtra(EXTRA_CURRENT_OIL, currentOil);
        i.putExtra(EXTRA_INDEX_OF_CURRENT_OIL, mOilList.indexOf(currentOil));
//        i.putExtra("indexOfCurrentCar", mCarLiveData.indexOf(currentCar));// chack list size
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.down_slide_enter, R.anim.up_slide_exit);
        break;
      }
      case R.id.ll_oil_history_btn: {
        Intent i = new Intent(MainActivity.this, OilHistoryActivity.class);
        i.putParcelableArrayListExtra(EXTRA_CURRENT_OIL_LIST, (ArrayList<Oil>) mOilList);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.down_slide_enter, R.anim.up_slide_exit);
        break;
      }
      case R.id.iv_call_btn: {
        showCallDialog();
      }
      break;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    mMenu = menu;
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        return true;
      case R.id.action_log_out:
        if (checkNetworkAvailableWithError()) {
          showLogOutDialog();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showLogOutDialog() {

    new Builder(this)
        .title(R.string.dlg_logout_title)
        .positiveText(R.string.dlg_yes)
        .negativeText(R.string.dlg_cancel)
        .positiveColor(getResources().getColor(R.color.color_484848))
        .negativeColor(getResources().getColor(R.color.colorAccent))
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            DialogManager.getInstance().showPreloader(MainActivity.this);
            logOut();
          }
        })
        .onNegative(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .show();

  }

  private void logOut() {
//    AppSession.getSession().closeAndClear();
    appSharedHelper.saveSavedRememberMe(false);
    new FirebaseAuthHelper().logout();
    InjectorUtils.provideRepository(getApplicationContext()).removeDataAndNotifications(mCarList);
    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//    if (showProgress) {
    contentLl.setVisibility(View.GONE);
    progressRl.setVisibility(View.VISIBLE);
//    }
    this.currentCar = (Car) parent.getItemAtPosition(position);
    myCarSpinnerAdapter.setSelection(position);

    mOilLiveData = AppDatabase.getInstance(getApplicationContext()).mOilDao()
        .getAllByCarId(currentCar.getId());
    mOilLiveData.observe(this, new Observer<List<Oil>>() {
      @Override
      public void onChanged(@Nullable List<Oil> oilList) {

//        mOilLiveData = appSharedHelper.getSaveOilListByCarKey(currentCar.getKey());

        if (oilList != null) {
          mOilList = oilList;
          if (oilList.size() == 0) {
            currentOil = new Oil();
//                firstOil=currentOil;
            setOilToUi(currentOil, true);
          } else {
            currentOil = oilList.get(oilList.size() - 1);
            firstOil = oilList.get(0);
            setOilToUi(currentOil, false);
          }
        } else {
          mOilList.clear();
//          OilDataDbHelper.getInstance()
//              .getCarOils(currentCar.getKey(), new ResultListener<List<Oil>>() {
//                @Override
//                public void onSuccess(List<Oil> oils) {
//                  if (oils.size() == 0) {
//                    currentOil = new Oil();
////                            firstOil=currentOil;
//                    setOilToUi(currentOil, true);
//                  } else {
//                    mOilLiveData = oils;
//                    currentOil = mOilLiveData.getAppNotification(mOilLiveData.size() - 1);
//                    firstOil = mOilLiveData.getAppNotification(0);
//                    setOilToUi(currentOil, false);
//                  }
//                }
//
//                @Override
//                public void onFail(Exception e) {
//                  ToastUtils.shortToast("onFail !!");
//                }
//              });

        }

//    hideProgress();
//    DialogUtil.getInstance().dismissDialog();
        appSharedHelper.saveSpinnerPosition(currentCar.getId());

//        if (showProgress) {
        new Handler().postDelayed(new Runnable() {

          @Override
          public void run() {
//          DialogUtil.getInstance().dismissDialog();
            contentLl.setVisibility(View.VISIBLE);
            progressRl.setVisibility(View.GONE);
          }

        }, 500);
//        }
      }
    });

  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  private void setOilToUi(Oil uiOil, boolean isOilListEmpty) {
    if (isOilListEmpty) {
      editOilBtnLl.setVisibility(View.GONE);
      oilHistoryBtnLl.setVisibility(View.GONE);

      emptyInfoLl.setVisibility(View.VISIBLE);
      oilInfoCv.setVisibility(View.GONE);
      kmInfoCv.setVisibility(View.GONE);
      dateInfoCv.setVisibility(View.GONE);
    } else {
      editOilBtnLl.setVisibility(View.VISIBLE);
      oilHistoryBtnLl.setVisibility(View.VISIBLE);

      emptyInfoLl.setVisibility(View.GONE);
      oilInfoCv.setVisibility(View.VISIBLE);
      kmInfoCv.setVisibility(View.VISIBLE);
      dateInfoCv.setVisibility(View.VISIBLE);
    }

//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {

    oilBrandTv.setText(uiOil.getBrand());
    oilTypeTv.setText(uiOil.getType());
    oilVolumeTv.setText(String.valueOf(uiOil.getVolume()));
    serviceDoneKmTv.setText(String.valueOf(uiOil.getServiceDoneKm()));
    nextServiceKmTv.setText(String.valueOf(uiOil.getServiceNextKm()));
    recomendedKmTv.setText(String.valueOf(uiOil.getRecomendedKm()));
    middleMonthKmTv.setText(String.valueOf(uiOil.getMiddleMonthKm()));
    oilServiveDoneDateTv.setText(uiOil.getServiceDoneDate() == 0 ? "-"
        : longToString(uiOil.getServiceDoneDate(), getDateFormat(DateType.DMY)));
    oilServiveNextDateTv.setText(uiOil.getServiceNextDate() == 0 ? "-"
        : longToString(uiOil.getServiceNextDate(), getDateFormat(DateType.DMY)));
    PhoneNumber phoneNumber = PhoneNumberUtils.getPhoneNumber(uiOil.getServiceCompanyId());

    if (!uiOil.getServiceCompanyId().equals("-") && PhoneNumber.isCountryValid(phoneNumber)
        && PhoneNumber.isValid(phoneNumber)) {
      findViewById(R.id.cv_company_id).setVisibility(View.VISIBLE);
//          oilCompanyIdTv.setText(uiOil.getServiceCompanyId());
      oilCompanyIdTv.setText(
          String.format("+%s %s", phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber()));
    } else {
      findViewById(R.id.cv_company_id).setVisibility(View.GONE);
    }

    String notesText = appSharedHelper.getOilNotes(currentCar.getKey(), uiOil.getKey());
    Logger.d("testt", "id: " + currentCar.getId() + "_" + uiOil.getId());
    Logger.d("testt", "notesText: " + notesText);

    if (notesText != null && !notesText.equals("")) {
      findViewById(R.id.cv_notes).setVisibility(View.VISIBLE);
      notesTextTv.setText(notesText);
    } else if (notesText == null || notesText.equals("")) {
      findViewById(R.id.cv_notes).setVisibility(View.GONE);
    }

    km1Tv.setText(currentCar.getDistanceUnit());
    km2Tv.setText(currentCar.getDistanceUnit());
    km3Tv.setText(currentCar.getDistanceUnit());
    km4Tv.setText(currentCar.getDistanceUnit());

//        showProgress = true;
//      }
//    }, showProgress ? 1500 : 0);

  }

  private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
//    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

    return result
        == PackageManager.PERMISSION_GRANTED;//&& result1 == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {

    ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, REQUEST_READ_PHONE_STATE);

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_READ_PHONE_STATE:
        if (grantResults.length > 0) {

          boolean writeContactsAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

          if (!writeContactsAccepted) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(CALL_PHONE)) {

                return;
              } else {
                SnackBarManager
                    .showWithAction(this, findViewById(R.id.root_main_activity),
                        getString(R.string.permission_denided),
                        Snackbar.LENGTH_LONG, getString(R.string.snake_bar_action_msg_allow), new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                            if (VersionUtils.isAfter23()) {
                              Intent intent = new Intent();
                              intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                              Uri uri = Uri.fromParts("package", getPackageName(), null);
                              intent.setData(uri);
                              startActivity(intent);
//                          finish();
                            }
                          }
                        });
              }
            }

          } else {
            if (currentOil != null) {
              Intent callIntent = new Intent(Intent.ACTION_CALL);
              callIntent.setData(Uri.parse("tel:" + currentOil.getServiceCompanyId()));
              if (ActivityCompat.checkSelfPermission(this, permission.CALL_PHONE)
                  != PackageManager.PERMISSION_GRANTED) {
                return;
              }
              startActivity(callIntent);
            }
//            phoneNameSwitch.setChecked(true);
          }
        }

        break;
    }
  }


  private void showCallDialog() {
    new MaterialDialog.Builder(this)
        .title(R.string.dlg_make_call_title)
        .neutralText(R.string.dlg_cancel)
        .negativeText(R.string.dlg_call_via)
        .positiveText(R.string.dlg_call)
        .neutralColor(getResources().getColor(R.color.colorAccent))
        .negativeColor(getResources().getColor(R.color.color_484848))
        .positiveColor(getResources().getColor(R.color.color_484848))
        .onNeutral(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .onNegative(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (currentOil != null) {
              startActivity(
                  new Intent(Intent.ACTION_DIAL,
                      Uri.parse("tel:" + currentOil.getServiceCompanyId())));
            }
          }
        })
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (!checkPermission()) {
              requestPermission();
            } else {
              if (currentOil != null) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + currentOil.getServiceCompanyId()));
                startActivity(callIntent);
              }
            }
          }
        })

        .show();
  }


  private void openScreen(Fragment fragment, boolean addToBackStack) {
    FragmentTransactionManager.displayFragment(
        getSupportFragmentManager(),
        fragment,
        R.id.fl_main_container,
        addToBackStack
    );
  }

}