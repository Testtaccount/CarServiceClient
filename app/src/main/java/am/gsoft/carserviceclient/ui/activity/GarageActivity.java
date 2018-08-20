package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_CREATE_NEW_CAR_ACTIVITY;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_EDIT_CAR_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_GARAGE_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_CAR;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.ui.activity.base.BaseActivity;
import am.gsoft.carserviceclient.ui.activity.main.MainActivity;
import am.gsoft.carserviceclient.ui.adapter.MyCarSpinnerAdapter;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class GarageActivity extends BaseActivity implements OnClickListener,Spinner.OnItemSelectedListener {

  private static final String TAG = GarageActivity.class.getSimpleName();

  public static final int CREATE_CAR_REQUEST = 100;
  private Spinner spinner;
  private TextView carBrandTv;
  private TextView modelTv;
  private TextView yearTv;
  private TextView numbersTv;
  private TextView vinCodeTv;
  private LinearLayout addCarBtnLl;
  private LinearLayout editCarBtnLl;
  private AppBarLayout appBarLayout;
  private NestedScrollView nestedScrollView;
  private LinearLayout contentLl;
  private RelativeLayout progressRl;
  private Space spaceView;
  private ProgressBar progressBar;

  private LiveData<List<Car>> mListLiveData;
  private List<Car> mCarList;
  private Car currentCar;
  private MyCarSpinnerAdapter myCarSpinnerAdapter;
  private boolean showProgress;

  private AppRepository mRepository;

  @Override
  protected int layoutResId() {
    return R.layout.activity_garage;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();
    customizeActionBar();
    initFields();

    mListLiveData = AppDatabase.getInstance(getApplicationContext()).mCarDao().getAll();
    mListLiveData.observe(this, new Observer<List<Car>>() {
      @Override
      public void onChanged(@Nullable List<Car> cars) {
        if (cars == null || cars.size() == 0) {
          ToastUtils.shortToast("Yo Don't Have Cars");
          Intent intent = new Intent(GarageActivity.this, CreateNewCarActivity.class);
          intent.setAction(ACTION_GARAGE_ACTIVITY_INTENT);
          startActivity(intent);
          finish();
          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
          mCarList =cars;
          initSpinner(cars);
          setListeners();
          spinner.setSelection(getPosition(cars,(long)appSharedHelper.getSpinnerPosition()), true);
          if (getIntent() != null && getIntent().getAction() != null) {
            if (getIntent().getAction().equals(ACTION_EDIT_CAR_ACTIVITY_INTENT) || getIntent()
                .getAction().equals(ACTION_CREATE_NEW_CAR_ACTIVITY)) {
              appBarLayout.setExpanded(false);
//          scrollToView(nestedScrollView, spaceView);
              focusOnView(nestedScrollView, spaceView);
            }
          }
        }
      }
    });

  }

  private void findViews() {
    appBarLayout=findViewById(R.id.appBar);
    nestedScrollView = findViewById(R.id.nscw);
    spaceView = findViewById(R.id.space);
    contentLl = (LinearLayout) findViewById(R.id.ll_content);
    progressRl = (RelativeLayout) findViewById(R.id.rl_progress);
    spinner = (Spinner) findViewById(R.id.spinner);
    addCarBtnLl = (LinearLayout) findViewById(R.id.ll_add_car_btn);
    editCarBtnLl = (LinearLayout) findViewById(R.id.ll_edit_car_btn);
    carBrandTv=(TextView)findViewById(R.id.tv_car_brand);
    modelTv=(TextView)findViewById(R.id.tv_model);
    yearTv=(TextView)findViewById(R.id.tv_year);
    numbersTv=(TextView)findViewById(R.id.tv_numbers);
    vinCodeTv=(TextView)findViewById(R.id.tv_vin_code);
    progressBar=(ProgressBar)findViewById(R.id.progress_bar);

  }

  private void customizeActionBar() {
    setActionBarTitle("My Garage");
    setActionBarUpButtonEnabled(true);
    appBarLayout.setExpanded(false);
  }

  private void initFields() {
    contentLl.setVisibility(View.GONE);
    progressRl.setVisibility(View.VISIBLE);

    showProgress =false;
    if (progressBar != null) {
      progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent2), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    mCarList = new ArrayList<>();
    currentCar=null;

  }

  private void setListeners() {
    addCarBtnLl.setOnClickListener(this);
    editCarBtnLl.setOnClickListener(this);
    addCarBtnLl.setFocusable(false);
    editCarBtnLl.setFocusable(false);
    spinner.setOnItemSelectedListener(this);
  }

  private void initSpinner(List<Car> carList) {
//    items= new ArrayList<Car>();
    myCarSpinnerAdapter=new MyCarSpinnerAdapter(this, carList);//user.getUserCarList());
    spinner.setAdapter(myCarSpinnerAdapter);
//    new OnItemSelectedListener(){
//      @Override
//      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//
//      }
//
//      @Override
//      public void onNothingSelected(AdapterView<?> adapterView){
//        //nothing
////        spinner.setSelection(appSharedHelper.getSpinnerPosition(),true);
//
//      }
//    });
  }

  private int getPosition(List<Car> cars, long spinnerPosition) {
    int position = 0;
    for (int i = 0; i < cars.size(); i++) {
      if (cars.get(i).getId() ==  spinnerPosition) {
        position = i;
        break;  // uncomment to getAppNotification the first instance
      }
    }
    return position;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ll_add_car_btn:
        Intent intent = new Intent(GarageActivity.this, CreateNewCarActivity.class);
        intent.setAction(ACTION_GARAGE_ACTIVITY_INTENT);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.down_slide_enter, R.anim.up_slide_exit);
        break;
      case R.id.ll_edit_car_btn:
        Intent intent1 = new Intent(GarageActivity.this, EditCarActivity.class);
        intent1.putExtra(EXTRA_CURRENT_CAR,currentCar.getId());
        startActivity(intent1);
        finish();
        overridePendingTransition(R.anim.down_slide_enter, R.anim.up_slide_exit);
        break;
//      case R.id.fab:
//        break;
    }
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
    Intent i = new Intent( GarageActivity.this,MainActivity.class);
    startActivity(i);
    finish();
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    super.onBackPressed();
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//    Toast.makeText(this, ((Car) parent.getItemAtPosition(position)).getCarBrand(), Toast.LENGTH_SHORT).show();

//    if (showProgress) {
      contentLl.setVisibility(View.GONE);
      progressRl.setVisibility(View.VISIBLE);
//    }

    this.currentCar=(Car) parent.getItemAtPosition(position);
    myCarSpinnerAdapter.setSelection(position);
    appSharedHelper.saveSpinnerPosition(currentCar.getId());

    Car car = (Car) parent.getItemAtPosition(position);
    setCarToUi(car);

//    if (showProgress) {
      new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
//          DialogUtil.getInstance().dismissDialog();
          contentLl.setVisibility(View.VISIBLE);
          progressRl.setVisibility(View.GONE);
        }

      }, 500);
//    }
  }

  private void setCarToUi(Car car) {
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
    carBrandTv.setText(car.getCarBrand());
    modelTv.setText(car.getModel());
    yearTv.setText(car.getYear());
    numbersTv.setText(car.getNumbers());
    vinCodeTv.setText(car.getVinCode());
//        showProgress =true;
//      }
//    }, showProgress ?750:0);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

}
