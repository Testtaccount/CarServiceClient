package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_CREATE_NEW_CAR_ACTIVITY;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_GARAGE_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_MAIN_ACTIVITY_INTENT;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.ResultListener;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.ui.activity.main.MainActivity;
import am.gsoft.carserviceclient.ui.adapter.BrandsSpinnerItem;
import am.gsoft.carserviceclient.ui.adapter.CarBrandsSpinnerAdapter;
import am.gsoft.carserviceclient.ui.dialog.CarBrandsDialogFragment;
import am.gsoft.carserviceclient.ui.dialog.CarBrandsDialogFragment.BrandsDialogListener;
import am.gsoft.carserviceclient.util.AppUtil;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import java.util.ArrayList;

public class CreateNewCarActivity extends BaseActivity implements View.OnClickListener,
    Spinner.OnItemSelectedListener, BrandsDialogListener {

  private static final String TAG = CreateNewCarActivity.class.getSimpleName();

  private AppBarLayout mAppBarLayout;
  private NestedScrollView nestedScrollView;
  private EditText modelEt;
  private EditText numbersEt;
  private EditText vinCodeEt;
  private RelativeLayout colorBackgroundRl;
  private Spinner carBrandsSpinner;
  private Spinner carYearsSpinner;
  private Spinner carDencityUnitSpinner;
  private FloatingActionButton saveCarFab;
  private CarBrandsSpinnerAdapter brandsSpinnerAdapter;
  private String[] carYears;
  private String[] carDencityUnits = {"Km", "Mil"};

  private ImageView colorPickerImg;
  private int colorId;
  private int backgroundColor = -256;
  private boolean doubleBackToExitPressedOnce = false;
  private int index = 0;

  private String[] names;
  private TypedArray icons;
  private ArrayList<BrandsSpinnerItem> items;
  private AppRepository repository;
  int c;

  @Override
  protected int layoutResId() {
    return R.layout.activity_create_new_car;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    findViews();
    customizeActionBar();
    setListeners();
    initFields();

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

    saveCarFab = (FloatingActionButton) findViewById(R.id.fab_save_car_btn);
  }

  private void customizeActionBar() {
//    initActionBar();
//    showActionBarIcon();
    setActionBarUpButtonEnabled(true);
    mAppBarLayout.setExpanded(false);
//    setActionBarTitle("Add New Car");
  }

  private void initFields() {
    repository = InjectorUtils.provideRepository(getApplicationContext());
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
      @Override
      public void run() {
        c = AppDatabase.getInstance(getApplicationContext()).mCarDao().getCount();

      }
    });
    items = new ArrayList<>();
    getBrandsList();
    carYears = getResources().getStringArray(R.array.years);
//    brandsSpinnerAdapter=new CarBrandsSpinnerAdapter(this,items,R.color.color_0099FF);
    ArrayAdapter<String> carYearsSpinnerAdapter = new ArrayAdapter<String>(App.getInstance(), R.layout.spinner_item, carYears) {
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
          tv.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
        } else {
          tv.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
      }
    };
    carYearsSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//    carBrandsSpinner.setAdapter(brandsSpinnerAdapter);
    carYearsSpinner.setAdapter(carYearsSpinnerAdapter);

    ArrayAdapter<String> carDencityUnitAdapter = new ArrayAdapter<String>(App.getInstance(), R.layout.spinner_item, carDencityUnits) {
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


//    items = new ArrayList<>();

//    colorPickerImg.setBackgroundColor(Color.WHITE);

//    carBrandsSpinner.setSelection(5);
//    modelEt.setText("Test");
//    carYearsSpinner.setSelection(5);
//    numbersEt.setText("QQ");
  }

  private void setListeners() {
//    cancelLlBtn.setOnClickListener(this);
//    saveCarLlBtn.setOnClickListener(this);
    colorBackgroundRl.setOnClickListener(this);
    saveCarFab.setOnClickListener(this);

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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    mMenu = menu;
    hideMenuOption(R.id.action_done);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab_save_car_btn:
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(this);
        if (checkInputs()) {
          saveCarToUserCarsList();
        }
        break;
      case R.id.rl_color_picker_background:
        ColorChooserDialog dialog = new ColorChooserDialog(this);
        dialog.setTitle(R.string.title);
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
//        ColorPicker colorPicker = new ColorPicker(this);
//        colorPicker.show();
//        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
//          @Override
//          public void onChooseColor(int position, int color) {
//
//          }
//
//          @Override
//          public void onCancel() {
//            // put code
//          }
//        });

        break;
//      case R.id.asdasd:
//
////        showCarBrandsDialogFragment();
//
//        break;
    }
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
    private CreateNewCarActivity activity;

    public GetBrandsListAsyncTask(CreateNewCarActivity activity) {
      this.activity = activity;
    }

    @Override
    protected ArrayList<BrandsSpinnerItem> doInBackground(Void... voids) {

      names = getResources().getStringArray(R.array.car_brands);
      icons = getResources().obtainTypedArray(R.array.car_barands_icons);

      for (int i = 1; i < names.length; i++) {
        items.add(new BrandsSpinnerItem(icons.getResourceId(i, 0), names[i]));
      }

      BrandsSpinnerItem b = new BrandsSpinnerItem(icons.getResourceId(0, 0), names[0]);
//      icons.recycle();
      adapterList.add(0, b);
      adapterList.addAll(items);

      return items;
    }

    @Override
    protected void onPostExecute(ArrayList<BrandsSpinnerItem> brandsSpinnerItems) {
      super.onPostExecute(brandsSpinnerItems);
      activity.setList(brandsSpinnerItems);
      brandsSpinnerAdapter = new CarBrandsSpinnerAdapter(CreateNewCarActivity.this, adapterList,
          R.color.colorAccent2);
      carBrandsSpinner.setAdapter(brandsSpinnerAdapter);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_done:
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(this);
        if (checkInputs()) {
          saveCarToUserCarsList();
        }
        return true;
      case android.R.id.home:
//        finish();
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {


    int animIdEnter = R.anim.slide_in_left;
    int animIdExit = R.anim.slide_out_righ;

    if (c != 0) {
      Intent intent = null;
      if (getIntent().getAction().equals(ACTION_MAIN_ACTIVITY_INTENT)) {
        intent = new Intent(CreateNewCarActivity.this, MainActivity.class);
        animIdEnter = R.anim.slide_in_left;
        animIdExit = R.anim.slide_out_righ;
      } else if (getIntent().getAction().equals(ACTION_GARAGE_ACTIVITY_INTENT)) {
        intent = new Intent(CreateNewCarActivity.this, GarageActivity.class);
        animIdEnter = R.anim.up_slide_enter;
        animIdExit = R.anim.down_slide_exit;
      }
      intent.setAction(ACTION_CREATE_NEW_CAR_ACTIVITY);
//    intent.putExtra(ACTION_CREATE_NEW_CAR_ACTIVITY, car);
      startActivity(intent);
      finish();
      overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);

      super.onBackPressed();
    } else {
      if (doubleBackToExitPressedOnce) {
        finish();

      }

      this.doubleBackToExitPressedOnce = true;
      if (appSharedHelper.getCarListToSave() != null
          && appSharedHelper.getCarListToSave().size() != 0) {
        ToastUtils.shortToast("Please click BACK again to exit");
      } else {
        ToastUtils.shortToast("Add new car, or click BACK again to exit");
      }

      new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
          doubleBackToExitPressedOnce = false;
        }
      }, 2000);
    }



  }

  private Car createNewCar() {
    Car car = new Car();
//    long id = IdGenerator.getId();
//    car.setKey(user.getKey() + "_" + id);
//    car.setId(id);
    BrandsSpinnerItem brandsSpinnerItem = (BrandsSpinnerItem) carBrandsSpinner.getSelectedItem();
    car.setIcon(brandsSpinnerItem.getIcon());//2130837697
    car.setColor(backgroundColor);//setColor(Color.WHITE);//-256
    car.setCarBrand(brandsSpinnerItem.getCarBrand());//setCarBrand(getString(R.string.none));
    car.setModel(modelEt.getText().toString());//setModel("X5");
    car.setYear(carYearsSpinner.getSelectedItem().toString());//setYear("1999");
    car.setNumbers(numbersEt.getText().toString());//setNumbers("00 oo 000");
    car.setVinCode(vinCodeEt.getText().toString().length() == 0 ? "-" : vinCodeEt.getText().toString());//setVinCode("fjksdhfjkdsfkjdskjfk");
    car.setDistanceUnit(carDencityUnitSpinner.getSelectedItem().toString());//setVinCode("fjksdhfjkdsfkjdskjfk");
//    car.setOil(new Oil());
    return car;
  }

  private void showCarBrandsDialogFragment() {
    FragmentManager fm = getSupportFragmentManager();
    CarBrandsDialogFragment dialogFragment = CarBrandsDialogFragment.newInstance(items);
    dialogFragment.show(fm, "fragment_car_brands");
  }

  private void saveCarToUserCarsList() {
    Car car = createNewCar();
//    List<Car> cars=user.getUserCarList();
//    cars.add(car);
//    user.setUserCars(cars);
//    AppSession.getSession().updateUser(user);

//    CarDataDbHalper.getInstance().setResultListener();

    repository.saveCar(car, new ResultListener<Car>() {
      @Override
      public void onLoad(Car car) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if(car!=null){
              ToastUtils.shortToast("Car created!");
              Intent intent = null;
              if (getIntent().getAction().equals(ACTION_MAIN_ACTIVITY_INTENT)) {
                intent = new Intent(CreateNewCarActivity.this, MainActivity.class);
              } else if (getIntent().getAction().equals(ACTION_GARAGE_ACTIVITY_INTENT)) {
                intent = new Intent(CreateNewCarActivity.this, GarageActivity.class);
              }

//    intent.putExtra(ACTION_CREATE_NEW_CAR_ACTIVITY, car);
//        intent.setAction(ACTION_CREATE_NEW_CAR_ACTIVITY);
              startActivity(intent);
              finish();
              if (getIntent().getAction().equals(ACTION_MAIN_ACTIVITY_INTENT)) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
              } else if (getIntent().getAction().equals(ACTION_GARAGE_ACTIVITY_INTENT)) {
                overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
              }
            }else {
              DialogManager.getInstance().dismissPreloader(this.getClass());
              ToastUtils.shortToast("Car not created!");
            }
          }
        });



      }

      @Override
      public void onFail(String e) {
        ToastUtils.shortToast("Car not created!");
      }
    });
//    CarDataDbHalper.getInstance().saveCar(user.getKey(), car, new ResultListener<Car>() {
//      @Override
//      public void onSuccess(Car obj) {


//      }

//      @Override
//      public void onFail(Exception e) {
//        DialogManager.getInstance().dismissPreloader(this.getClass());
//        ToastUtils.shortToast("Car not created!");
//      }
//    });

  }

  private boolean checkInputs() {

    if (carBrandsSpinner.getSelectedItemPosition() == 0) {
      focusOnView(nestedScrollView, carBrandsSpinner);
      ((TextView) carBrandsSpinner.getChildAt(0)).setError("Not Selected");
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (TextUtils.isEmpty(modelEt.getText())) {
      focusOnView(nestedScrollView, modelEt);
      modelEt.setError("Car Model Can Not Be Empty!");
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (carYearsSpinner.getSelectedItemPosition() == 0) {
      focusOnView(nestedScrollView, carYearsSpinner);
      ((TextView) carYearsSpinner.getChildAt(0)).setError("Not Selected");
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (TextUtils.isEmpty(numbersEt.getText())) {
      focusOnView(nestedScrollView, numbersEt);
      numbersEt.setError("Car Numbers Can Not Be Empty!");
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
//        setTextViewDrawableColor(convertView,R.color.color_0099FF);
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
        this.index = position;
        break;
      default:
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

}