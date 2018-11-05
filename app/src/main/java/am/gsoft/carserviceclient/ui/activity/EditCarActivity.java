package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_EDIT_CAR_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_GARAGE_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_CAR_KEY;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.viewmodel.EditCarActivityViewModel;
import am.gsoft.carserviceclient.data.viewmodel.ViewModelFactory;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.ui.adapter.BrandsSpinnerItem;
import am.gsoft.carserviceclient.ui.adapter.CarBrandsSpinnerAdapter;
import am.gsoft.carserviceclient.ui.dialog.CarBrandsDialogFragment;
import am.gsoft.carserviceclient.ui.dialog.CarBrandsDialogFragment.BrandsDialogListener;
import am.gsoft.carserviceclient.util.AppUtil;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import java.util.ArrayList;

public class EditCarActivity extends BaseActivity implements View.OnClickListener,
    Spinner.OnItemSelectedListener, BrandsDialogListener {

  private static final String TAG = EditCarActivity.class.getSimpleName();

  private AppBarLayout mAppBarLayout;
  private NestedScrollView nestedScrollView;
  private FloatingActionButton saveCarFab;
  private EditCarActivityViewModel mViewModel;
//  private AppRepository repository;

  private Spinner carBrandsSpinner;
  private Spinner carYearsSpinner;
  private Spinner carDencityUnitSpinner;
  private String[] carYears;
  private String[] carDencityUnits = new String[2];
  private CarBrandsSpinnerAdapter brandsSpinnerAdapter;
  private ImageView colorPickerImg;
  private int backgroundColor = -256;
  private RelativeLayout colorBackgroundRl;
  private EditText modelEt;
  private EditText numbersEt;
  private EditText vinCodeEt;
  private FrameLayout deleteCarBtn;
  private int colorId;
  private int index;
  private String[] names;
  private TypedArray icons;
  private ArrayList<BrandsSpinnerItem> items;

  private String mCarKey;
  private long DEFAULT_CAR_ID = -1L;
  private Car currentCar;

  @Override
  protected int layoutResId() {
    return R.layout.activity_edit_car;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(EditCarActivityViewModel.class);

    findViews();
    customizeActionBar();
    setListeners();
    initFields();

    Intent intent = getIntent();
    if (intent != null && intent.hasExtra(EXTRA_CURRENT_CAR_KEY)) {

      mCarKey = intent.getStringExtra(EXTRA_CURRENT_CAR_KEY);

      mViewModel.getCar(mCarKey).observe(this, new Observer<Car>() {
        @Override
        public void onChanged(@Nullable Car car) {
          if (car != null) {
            populateUI(car);
          }
        }
      });

//      AppExecutors.getInstance().diskIO().execute(new Runnable() {
//        @Override
//        public void run() {
//          final Car car = AppDatabase.getInstance(getApplicationContext()).mCarDao().gget(mCarKey);
//
//          runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//              populateUI(car);
//            }
//          });
//        }
//      });

    }
  }

  private void findViews() {
    mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);
    nestedScrollView = findViewById(R.id.nscw);
    carBrandsSpinner = findViewById(R.id.spinner_car_brands);
    carYearsSpinner = findViewById(R.id.spinner_car_years);
    carDencityUnitSpinner = findViewById(R.id.spinner_car_dencity_unit);
    colorPickerImg = findViewById(R.id.img_color_picker);
    colorBackgroundRl = findViewById(R.id.rl_color_picker_background);
    modelEt = findViewById(R.id.et_model);
    numbersEt = findViewById(R.id.et_numbers);
    vinCodeEt = findViewById(R.id.et_vin_code);
    deleteCarBtn = findViewById(R.id.fl_delete_car_btn);
    deleteCarBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          v.performClick();
        }
      }
    });

//    fabRevealLayoutMain = (FABRevealLayout) findViewById(R.id.fab_reveal_layout_create_new_car);

//    cancelLlBtn=(LinearLayout)findViewById(R.id.ll_cancel_btn);
//    saveCarLlBtn=(LinearLayout)findViewById(R.id.ll_save_car_btn);
    saveCarFab = (FloatingActionButton) findViewById(R.id.fab_save_edited_car_btn);
  }

  private void customizeActionBar() {
//    initActionBar();
//    showActionBarIcon();
    setActionBarUpButtonEnabled(true);
    mAppBarLayout.setExpanded(false);
//    setActionBarTitle(getString(R.string.app_name));
  }

  private void initFields() {
//    repository = InjectorUtils.provideRepository(getApplicationContext());
    items = new ArrayList<>();
    carDencityUnits[0] = getString(R.string.km);
    carDencityUnits[1] = getString(R.string.mil);
  }

  private void setListeners() {
//    cancelLlBtn.setOnClickListener(this);
//    saveCarLlBtn.setOnClickListener(this);
    colorBackgroundRl.setOnClickListener(this);
    saveCarFab.setOnClickListener(this);
    deleteCarBtn.setOnClickListener(this);

//    configureFABReveal(fabRevealLayoutMain);

//    carBrandsSpinner.setOnItemSelectedListener(this);
    carYearsSpinner.setOnItemSelectedListener(this);
    carDencityUnitSpinner.setOnItemSelectedListener(this);

    carBrandsSpinner.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        showCarBrandsDialogFragment();
        return false;
      }
    });

    mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      boolean isShow = false;
      int scrollRange = -1;

      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
          scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
          isShow = true;
          showMenuOption(R.id.action_done);
          findViewById(R.id.v_shadow).setVisibility(View.INVISIBLE);
        } else if (isShow) {
          isShow = false;
          hideMenuOption(R.id.action_done);
          findViewById(R.id.v_shadow).setVisibility(View.VISIBLE);
        }
      }
    });

  }

  private void populateUI(Car currentCar) {
    this.currentCar = currentCar;
    getBrandsList();
    carYears = getResources().getStringArray(R.array.years);
    index = getYearSpinnerPosition(currentCar.getYear());
//    brandsSpinnerAdapter=new CarBrandsSpinnerAdapter(this,setBrandsList(),R.color.color_d32f2f);
    ArrayAdapter<String> carYearsSpinnerAdapter = new ArrayAdapter<String>(App.getInstance(),
        R.layout.spinner_item, carYears) {
      @Override
      public boolean isEnabled(int position) {
        if (position == 0) {
          // Disable the first item from Spinner
          return false;
        } else {
          return true;
        }
      }


      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        setTextViewDrawableColor(tv, android.R.color.transparent);
        if (position == 0) {
          // Set the disable item text color
          tv.setTextColor(Color.GRAY);
        } else {
          tv.setTextColor(Color.BLACK);
        }

        if (position == index) {
          tv.setBackgroundColor(
              App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
        } else {
          tv.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
      }
    };
    carYearsSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//    carBrandsSpinner.setAdapter(brandsSpinnerAdapter);
    carYearsSpinner.setAdapter(carYearsSpinnerAdapter);

//    carBrandsSpinner.setSelection(getBrandsSpinnerPosition(currentCar.getCarBrand()),true);
    carYearsSpinner.setSelection(getYearSpinnerPosition(currentCar.getYear()), true);

    ArrayAdapter<String> carDencityUnitAdapter = new ArrayAdapter<String>(App.getInstance(),
        R.layout.spinner_item, carDencityUnits) {
//      @Override
//      public boolean isEnabled(int position) {
//        if (position == 0) {
//          // Disable the first item from Spinner
//          return false;
//        } else {
//          return true;
//        }
//      }

      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        setTextViewDrawableColor(tv, android.R.color.transparent);
//        if (position == 0) {
//          // Set the disable item text color
//          tv.setTextColor(Color.GRAY);
//        } else {
        tv.setTextColor(Color.BLACK);
//        }

//        if (position == index) {
//          tv.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_E5E5E5));
//        } else {
//          tv.setBackgroundColor(Color.TRANSPARENT);
//        }
        return view;
      }
    };
    carDencityUnitAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    carDencityUnitSpinner.setAdapter(carDencityUnitAdapter);

    if (currentCar.getDistanceUnit().equals("Km")) {
      carDencityUnitSpinner.setSelection(0, true);
    } else {
      carDencityUnitSpinner.setSelection(1, true);
    }

    modelEt.setText(currentCar.getModel());
    numbersEt.setText(currentCar.getNumbers());
    vinCodeEt.setText(currentCar.getVinCode().equals("-") ? "" : currentCar.getVinCode());
//    vinCodeEt.setText(currentCar.getVinCode().equals("-")?"":currentCar.getVinCode());

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab_save_edited_car_btn:
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(this);
        if (checkInputs()) {
          saveEditedCarToUserCarsList();
        }
        break;
      case R.id.fl_delete_car_btn:
        if (checkNetworkAvailableWithError()) {
          showDeleteDialog();
        }
        break;
      case R.id.rl_color_picker_background:
        ColorChooserDialog dialog = new ColorChooserDialog(this);
        dialog.setTitle("Choose color");
        dialog.setColorListener(new ColorListener() {
          @Override
          public void OnColorClick(View v, int color) {
            //do whatever you want to with the values
            colorBackgroundRl.setBackgroundColor(color);
            backgroundColor = color;
          }
        });
        //customize the dialog however you want
        dialog.show();

        break;
    }
  }

  private void showCarBrandsDialogFragment() {
    FragmentManager fm = getSupportFragmentManager();
    CarBrandsDialogFragment dialogFragment = CarBrandsDialogFragment.newInstance(items);
    dialogFragment.show(fm, "fragment_car_brands");
  }

  private void showDeleteDialog() {

    new Builder(this)
        .title(R.string.dlg_delete_car_title)
        .positiveText(R.string.dlg_delete)
        .negativeText(R.string.dlg_cancel)
        .positiveColor(getResources().getColor(R.color.color_484848))
        .negativeColor(getResources().getColor(R.color.colorAccent2))
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            DialogManager.getInstance().showPreloader(EditCarActivity.this);
            deleteCar();
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

  private void getBrandsList() {
    new GetBrandsListAsyncTask(this).execute();
  }

  public void setList(ArrayList<BrandsSpinnerItem> list) {
    this.items = list;
  }

  @Override
  public void onBrandSelected(int i) {
    carBrandsSpinner.setSelection(i + 1);
  }

  class GetBrandsListAsyncTask extends AsyncTask<Void, Void, ArrayList<BrandsSpinnerItem>> {

    ArrayList<BrandsSpinnerItem> items = new ArrayList<>();
    ArrayList<BrandsSpinnerItem> adapterList = new ArrayList<>();
    private EditCarActivity activity;

    public GetBrandsListAsyncTask(EditCarActivity activity) {
      this.activity = activity;
    }

    @Override
    protected ArrayList<BrandsSpinnerItem> doInBackground(Void... voids) {

      Resources resources = getResources();
      names = resources.getStringArray(R.array.car_brands);
      icons = resources.obtainTypedArray(R.array.car_barands_icons);

      for (int i = 1; i < names.length; i++) {
        int ii = icons.getResourceId(i, 0);
        String name = resources.getResourceEntryName(ii);

        items.add(new BrandsSpinnerItem(name, names[i]));
      }

      BrandsSpinnerItem b = new BrandsSpinnerItem(resources.getResourceEntryName(icons.getResourceId(0, 0)), names[0]);
//      icons.recycle();
      adapterList.add(0, b);
      adapterList.addAll(items);

      return items;
    }

    @Override
    protected void onPostExecute(ArrayList<BrandsSpinnerItem> brandsSpinnerItems) {
      super.onPostExecute(brandsSpinnerItems);
      activity.setList(brandsSpinnerItems);
      brandsSpinnerAdapter = new CarBrandsSpinnerAdapter(EditCarActivity.this, adapterList,
          R.color.colorAccent2);
      carBrandsSpinner.setAdapter(brandsSpinnerAdapter);
      carBrandsSpinner.setSelection(getBrandsSpinnerPosition(currentCar.getCarBrand()), true);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    mMenu = menu;
    hideMenuOption(R.id.action_done);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_done:
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(EditCarActivity.this);
        if (checkInputs()) {
          saveEditedCarToUserCarsList();
        }
        return true;
      case android.R.id.home:
//        finish();
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

//  private void hideMenuOption(int id) {
//    MenuItem item = menu.findItem(id);
//    item.setVisible(false);
//  }
//
//  private void showMenuOption(int id) {
//    MenuItem item = menu.findItem(id);
//    item.setVisible(true);
//  }

  @Override
  public void onBackPressed() {
    Intent i = new Intent(EditCarActivity.this, GarageActivity.class);
    i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
    startActivity(i);
    finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);

    super.onBackPressed();
  }

//  private List<BrandsSpinnerItem> setBrandsList() {
//
//    brands = getResources().getStringArray(R.array.car_brands);
//    TypedArray icons = getResources().obtainTypedArray(R.array.car_barands_icons);
//    List<BrandsSpinnerItem> items = new ArrayList<>();
//    for (int i = 0; i < brands.length; i++) {
//      items.add(new BrandsSpinnerItem(icons.getResourceId(i, 0),brands[i]));
//    }
//    icons.recycle();
//    this.items=items;
//    return items;
//
//  }

  private int getBrandsSpinnerPosition(String carBrand) {
    int index = -1;
    for (int i = 0; i < names.length; i++) {
      if (names[i].equals(carBrand)) {
        index = i;
        break;
      }
    }
    return index;
  }

  private int getYearSpinnerPosition(String carYear) {

    int index = -1;
    for (int i = 0; i < carYears.length; i++) {
      if (carYears[i].equals(carYear)) {
        index = i;
        break;
      }
    }
    return index;
  }

  private int getDencityUnitySpinnerPosition(String du) {

    int index = -1;
    for (int i = 0; i < carDencityUnits.length; i++) {
      if (carDencityUnits[i].equals(du)) {
        index = i;
        break;
      }
    }
    return index;
  }

  private Car getEditedCar() {
//    Car car=new Car();
//    car.setKey(currentCar.getKey());
//    car.setId(currentCar.getId());

    BrandsSpinnerItem brandsSpinnerItem = (BrandsSpinnerItem) carBrandsSpinner.getSelectedItem();
    currentCar.setIcon(brandsSpinnerItem.getIcon());;//2130837697
    currentCar.setColor(backgroundColor);//setLanguage(Color.WHITE);//-256
    currentCar.setCarBrand(brandsSpinnerItem.getCarBrand());//setCarBrand(getString(R.string.none));
    currentCar.setModel(modelEt.getText().toString());//setModel("X5");
    currentCar.setYear(carYearsSpinner.getSelectedItem().toString());//setYear("1999");
    currentCar.setNumbers(numbersEt.getText().toString());//setNumbers("00 oo 000");
    currentCar.setVinCode(vinCodeEt.getText().toString().length() == 0 ? "-"
        : vinCodeEt.getText().toString());//setVinCode("fjksdhfjkdsfkjdskjfk");
    switch (carDencityUnitSpinner.getSelectedItemPosition()) {
      case 0:
        currentCar.setDistanceUnit("Km");
        break;
      case 1:
        currentCar.setDistanceUnit("Mil");
        break;
    }
//    currentCar.setDistanceUnit(carDencityUnitSpinner.getSelectedItem().toString());
//    car.setOil(new Oil());
    return currentCar;
  }

  private void saveEditedCarToUserCarsList() {
    Car editedCar = getEditedCar();
//    List<Car> cars=user.getUserCarList();
//    cars.add(car);
//    user.setUserCars(cars);
//    AppSession.getSession().updateUser(user);

//    CarDataDbHalper.getInstance().setResultListener();

    mViewModel.editCar(editedCar);
    ToastUtils.shortToast(R.string.msg_car_edited);
    Intent i = new Intent(EditCarActivity.this, GarageActivity.class);
//        i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
    startActivity(i);
    finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//    repository.editCar(editedCar, new ResultListener<Car>() {
//      @Override
//      public void onLoad(Car car) {
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            if (car != null) {
//              ToastUtils.shortToast(R.string.msg_car_edited);
//              Intent i = new Intent(EditCarActivity.this, GarageActivity.class);
////        i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
//              startActivity(i);
//              finish();
////    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
//              overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//            } else {
//              DialogManager.getInstance().dismissPreloader(this.getClass());
//              ToastUtils.shortToast(R.string.msg_car_not_edited);
//            }
//          }
//
//        });

//      }
////
////      @Override
////      public void onFail(String e) {
////        ToastUtils.shortToast(R.string.msg_car_not_edited);
////      }
////    });

//    CarDataDbHalper.getInstance().editCar(user.getKey(),editedCar,new ResultListener<Car>() {
//      @Override
//      public void onSuccess(Car obj) {
//        ToastUtils.shortToast("Car edited!");
//        Intent i = new Intent( EditCarActivity.this,GarageActivity.class);
////        i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
//        startActivity(i);
//        finish();
////    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
//        overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//
//      }
//
//      @Override
//      public void onFail(Exception e) {
//        DialogManager.getInstance().dismissPreloader(this.getClass());
//        ToastUtils.shortToast("Car not edited!");
//      }
//    });

  }

  private void deleteCar() {
    mViewModel.deleteCar(currentCar);
    NotificationsRepository notificationsRepository = InjectorUtils.provideNotificationRepository(getApplicationContext());
    notificationsRepository.deleteAllNotificationsByCarKey(currentCar.getKey());

    ToastUtils.shortToast(R.string.msg_car_deleted);

    mViewModel.getCarsCont().observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(@Nullable Integer integer) {
        mViewModel.getCarsCont().removeObserver(this);

        if (integer != null && integer != 0) {

          Intent i = new Intent(EditCarActivity.this, GarageActivity.class);
//        i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
          i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(i);
          finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
          overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);

        } else {
          ToastUtils.shortToast("Yo Don't Have Cars");
          Intent intent = new Intent(EditCarActivity.this, CreateNewCarActivity.class);
          intent.setAction(ACTION_GARAGE_ACTIVITY_INTENT);
          startActivity(intent);
          finish();
          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

      }
    });

//    repository.deleteCar(currentCar, new ResultListener<Car>() {
//      @Override
//      public void onLoad(Car car) {
//        //        getAlarmManager().cancel(PendingIntent    .getService(EditCarActivity.this, (int)(currentOil.getId()),new Intent(getApplicationContext(), NotificationIntentService.class) ,PendingIntent.FLAG_UPDATE_CURRENT));
//
//        NotificationsRepository notificationsRepository = InjectorUtils
//            .provideNotificationRepository(getApplicationContext());
//        notificationsRepository.deleteAllNotificationsByCarKey(currentCar.getKey());
//
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//
////            if(car!=null){
//
////              Logger.NotificationActionsFragment("testt", "car: " + car.toString());
////              List<Oil> oilList = appSharedHelper.getSaveOilListByCarKey(currentCar.getKey());
////              Logger.NotificationActionsFragment("testt", "oilList: " + oilList);
//
////              if (oilList != null && oilList.size() > 0) {
////                Oil oil = oilList.getAppNotification(oilList.size() - 1);
////               / mAlarmController.deletePreviusMonthlyNotificationAlarm(oil.getKey());
////              }
////              else {
////                OilDataDbHelper.getInstance() .getOils(currentCar.getKey(), new ResultListener<List<Oil>>() {
////                      @Override
////                      public void onSuccess(List<Oil> oils) {
////                        if (oils.size() > 0) {
////                          Oil oil = oils.getAppNotification(oils.size() - 1);
////                          Logger.NotificationActionsFragment("testt", "oil: " + oil.toString());
////                          mAlarmController.deletePreviusMonthlyNotificationAlarm(oil.getKey());
////                        }
////                      }
////
////                      @Override
////                      public void onFail(Exception e) {
////                        ToastUtils.shortToast("onFail !!");
////                      }
////                    });
////              }
//
//            ToastUtils.shortToast(R.string.msg_car_deleted);
//            Intent i = new Intent(EditCarActivity.this, GarageActivity.class);
////        i.setAction(ACTION_EDIT_CAR_ACTIVITY_INTENT);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
////    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
//            overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//
////            }else {
////              DialogManager.getInstance().dismissPreloader(this.getClass());
////              ToastUtils.shortToast("Car not deleted!");
////            }
//
//          }
//        });
//      }
//
//      @Override
//      public void onFail(String e) {
//        ToastUtils.shortToast(R.string.msg_car_not_deleted);
//      }
//    });

  }

  private boolean checkInputs() {

    if (carBrandsSpinner.getSelectedItemPosition() == 0) {
      focusOnView(nestedScrollView, carBrandsSpinner);
      ((TextView) carBrandsSpinner.getChildAt(0))
          .setError(getString(R.string.err_msg_not_selected));
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (TextUtils.isEmpty(modelEt.getText())) {
      focusOnView(nestedScrollView, modelEt);
      modelEt.setError(getString(R.string.err_msg_empty));
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (carYearsSpinner.getSelectedItemPosition() == 0) {
      focusOnView(nestedScrollView, carYearsSpinner);
      ((TextView) carYearsSpinner.getChildAt(0)).setError(getString(R.string.err_msg_not_selected));
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (TextUtils.isEmpty(numbersEt.getText())) {
      focusOnView(nestedScrollView, numbersEt);
      numbersEt.setError(getString(R.string.err_msg_empty));
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    return true;
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch (parent.getId()) {
      case R.id.spinner_car_brands:
//        TextView convertView = (TextView) view.findViewById(R.id.tv_item_car_brand);
////    Drawable[] compoundDrawables = convertView.getCompoundDrawables();
////    Drawable rightCompoundDrawable = compoundDrawables[2];
//        setTextViewDrawableColor(convertView,R.color.color_d32f2f);
        brandsSpinnerAdapter.setSelection(position);
        break;
      case R.id.spinner_car_years:
        TextView convertView1 = (TextView) view.findViewById(R.id.spinner_item_id_tv);
//    Drawable[] compoundDrawables = convertView.getCompoundDrawables();
//    Drawable rightCompoundDrawable = compoundDrawables[2];
        setTextViewDrawableColor(convertView1, R.color.colorAccent2);
        this.index = position;
        break;
      case R.id.spinner_car_dencity_unit:
        TextView convertView2 = (TextView) view.findViewById(R.id.spinner_item_id_tv);
//    Drawable[] compoundDrawables = convertView.getCompoundDrawables();
//    Drawable rightCompoundDrawable = compoundDrawables[2];
        setTextViewDrawableColor(convertView2, R.color.colorAccent2);

        break;
      default:
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

}

