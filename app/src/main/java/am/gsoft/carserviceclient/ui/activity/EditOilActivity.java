package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.AppUtil.getDouble;
import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;
import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_EDIT_OIL_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_CAR_KEY;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_OIL;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_FIRST_OIL;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_INDEX_OF_CURRENT_OIL;
import static am.gsoft.carserviceclient.util.DateUtils.getDateDifferenceInDDMMYYYY;
import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;
import static am.gsoft.carserviceclient.util.DateUtils.longToDate;
import static am.gsoft.carserviceclient.util.DateUtils.longToString;
import static android.Manifest.permission.WRITE_CONTACTS;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.viewmodel.EditOilActivityViewModel;
import am.gsoft.carserviceclient.data.viewmodel.ViewModelFactory;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.phone.CountryInfo;
import am.gsoft.carserviceclient.phone.CountryListSpinner;
import am.gsoft.carserviceclient.phone.PhoneNumber;
import am.gsoft.carserviceclient.phone.PhoneNumberUtils;
import am.gsoft.carserviceclient.ui.activity.main.MainActivity;
import am.gsoft.carserviceclient.ui.dialog.EditNotesDialogFragment;
import am.gsoft.carserviceclient.ui.dialog.EditNotesDialogFragment.EditNotesDialogListener;
import am.gsoft.carserviceclient.util.AppUtil;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import am.gsoft.carserviceclient.util.Logger;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class EditOilActivity extends BaseActivity implements View.OnClickListener,
    EditNotesDialogListener, Spinner.OnItemSelectedListener, EditText.OnEditorActionListener {

  private static final String TAG = EditOilActivity.class.getSimpleName();
  private static final int PERMISSION_REQUEST_CODE = 200;


  private final int DATE_PICKER_TO = 0;
  private final int DATE_PICKER_FROM = 1;

  private CoordinatorLayout root;
  private AppBarLayout mAppBarLayout;
  private NestedScrollView nestedScrollView;
//  private MaskEditText oilCompanyIdEt;

  private CountryListSpinner mCountryListSpinner;
  private EditText mPhoneEditText;
  private TextView mErrorEditText;

  private TextView oilServiveDoneDateTv;
  private TextView oilServiveNextDateTv;
  private Spinner oilBrandSp;
  private Spinner oilTypeSp;
  private EditText oilVolumeEt;
  private TextView km1Tv;
  private TextView km2Tv;
  private TextView km3Tv;
  private TextView km4Tv;
  private TextView midManualTv;
  //  private Spinner oilVolumeSp;
///  private TextView serviceDoneKmTv;
  private AutoCompleteTextView oilBrandEt;
  private EditText phoneNameEt;
  private EditText serviceDoneKmEt;
  private EditText nextServiceKmEt;
  private EditText recomendedKmEt;
  private EditText middleMonthKmEt;
  //  private EditText notesEt;
  private TextView notesTv;
  //  private TextInputLayout serviceCompanyIdTil;
//  private TextInputLayout serviceDoneKmTil;
//  private TextInputLayout nextServiceKmTil;
//  private TextInputLayout recomendedKmTil;
//  private TextInputLayout middleMonthKmTil;
  private TextInputLayout oilCompanyPhoneTil;
  private Switch midMonthSwitch;
  private TableRow midMonthTr;
  private Switch phoneNameSwitch;
  private TableRow phoneNameTr;
  private int indexBrand;
  private int indexType;
  private int indexOfCurrentOil;
  //  int indexOfCurrentCar;
  private long serviceDoneDate;
  private long serviceNextDate;
  //  private TextView cancelTvBtn;
//  private TextView saveOilTvBtn;
  private FloatingActionButton saveOilChangesFab;
  private CardView notesCv;
  private ImageView imgI1, imgI2, imgI3, imgI4;
  private ImageView editNotesImg;
  private DatePickerDialog.OnDateSetListener from_dateListener;
  private DatePickerDialog.OnDateSetListener to_dateListener;
  private NotificationsRepository mNotificationsRepository;
//  private AppRepository mAppRepository;
private EditOilActivityViewModel mViewModel;

  private Car currentCar;
  private Oil firstOil;
  private Oil currentOil;

  private String[] oilBrands;
  private String[] oilTypes;
  private String[] oilVolumes = {"0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"};


  private long c = 0;
  private long r = 0;
  private long n = 0;
  private long middleMonth = 0;

  private TextWatcher t1;
  private TextWatcher t2;
  private TextWatcher t3;
  private TextWatcher t4;


  @Override
  protected int layoutResId() {
    return R.layout.activity_edit_oil;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(EditOilActivityViewModel.class);

    findViews();
    customizeActionBar();
    initFields();
    setListeners();

//    AdjustingViewGlobalLayoutListener listen = new AdjustingViewGlobalLayoutListener(ll);
//    root.getViewTreeObserver().addOnGlobalLayoutListener(listen);

  }

  private void findViews() {
//    fabRevealLayoutMain = (FABRevealLayout) findViewById(R.id.fab_reveal_layout_main);
    root = (CoordinatorLayout) findViewById(R.id.root_create_new_oil);
//    oilCompanyIdEt = findViewById(R.id.et_oil_company_id);

    mCountryListSpinner = findViewById(R.id.country_list);
    mPhoneEditText = findViewById(R.id.phone_number);
    mErrorEditText = findViewById(R.id.phone_number_error);

    mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);
    nestedScrollView = findViewById(R.id.nscw);

    oilServiveDoneDateTv = (TextView) findViewById(R.id.tv_oil_service_done_date);
    oilServiveNextDateTv = (TextView) findViewById(R.id.tv_oil_service_next_date);
    oilBrandSp = (Spinner) findViewById(R.id.spinner_oil_brand);
    oilTypeSp = (Spinner) findViewById(R.id.spinner_oil_type);
    oilVolumeEt = (EditText) findViewById(R.id.et_oil_volume);
//    oilVolumeSp=(Spinner) findViewById(R.id.spinner_oil_volume);
//    serviceDoneKmTv=(TextView) findViewById(R.id.et_service_done_km);
    oilBrandEt = (AutoCompleteTextView) findViewById(R.id.et_oil_brand);
    phoneNameEt = (EditText) findViewById(R.id.et_phone_name);
    serviceDoneKmEt = (EditText) findViewById(R.id.et_service_done_km);
    nextServiceKmEt = (EditText) findViewById(R.id.et_next_service_km);
    recomendedKmEt = (EditText) findViewById(R.id.et_recomended_km);
    middleMonthKmEt = (EditText) findViewById(R.id.et_middle_month_km);
//    notesEt =(EditText) findViewById(R.id.et_notes);

    notesCv = (CardView) findViewById(R.id.cv_notes);
    notesTv = (TextView) findViewById(R.id.tv_notes);

    imgI1 = (ImageView) findViewById(R.id.img_i1);
    imgI2 = (ImageView) findViewById(R.id.img_i2);
    imgI3 = (ImageView) findViewById(R.id.img_i3);
    imgI4 = (ImageView) findViewById(R.id.img_i4);
    editNotesImg = (ImageView) findViewById(R.id.img_edit_notes);

    km1Tv = (TextView) findViewById(R.id.tv_km1);
    km2Tv = (TextView) findViewById(R.id.tv_km);
    km3Tv = (TextView) findViewById(R.id.tv_km3);
    km4Tv = (TextView) findViewById(R.id.tv_km4);
    midManualTv = (TextView) findViewById(R.id.tv_mid_manual);

    oilCompanyPhoneTil = (TextInputLayout) findViewById(R.id.til_oil_company_id);
//    serviceCompanyIdTil = (TextInputLayout) findViewById(R.id.til_oil_company_id);
//    serviceDoneKmTil = (TextInputLayout) findViewById(R.id.til_service_done_km);
//    nextServiceKmTil = (TextInputLayout) findViewById(R.id.til_next_service_km);
//    recomendedKmTil = (TextInputLayout) findViewById(R.id.til_recomended_km);
//    middleMonthKmTil = (TextInputLayout) findViewById(R.id.til_middle_month_km);
//    cancelTvBtn=(TextView) findViewById(R.id.tv_cancel_as_btn);
//    saveOilTvBtn=(TextView) findViewById(R.id.tv_save_oil_as_btn);

    saveOilChangesFab = (FloatingActionButton) findViewById(R.id.fab_save_oil_changes);
    midMonthSwitch = (Switch) findViewById(R.id.switch_mid_month);
    phoneNameSwitch = (Switch) findViewById(R.id.switch_phone_name);
    midMonthTr = (TableRow) findViewById(R.id.tr_middle_month);
    phoneNameTr = (TableRow) findViewById(R.id.tr_phone_name);

  }

  private void customizeActionBar() {
//    initActionBar();
//    showActionBarIcon();
    setActionBarUpButtonEnabled(true);
    mAppBarLayout.setExpanded(false);
//    setActionBarTitle(getString(R.string.app_name));
  }

  private void initFields() {
//    mAppRepository = InjectorUtils.provideRepository(getApplicationContext());
    mNotificationsRepository = InjectorUtils.provideNotificationRepository(getApplicationContext());

    currentCar = getIntent().getParcelableExtra(EXTRA_CURRENT_CAR_KEY);
    firstOil = getIntent().getParcelableExtra(EXTRA_FIRST_OIL);
    currentOil = getIntent().getParcelableExtra(EXTRA_CURRENT_OIL);
    indexOfCurrentOil = getIntent().getIntExtra(EXTRA_INDEX_OF_CURRENT_OIL, -1);
//    indexOfCurrentCar =getIntent().getIntExtra("indexOfCurrentCar",0);

    oilBrands = getResources().getStringArray(R.array.oil_brands);
    oilTypes = getResources().getStringArray(R.array.oil_types);

    indexBrand = getSpinnerPosition(oilBrands, currentOil.getBrand());
    indexType = getSpinnerPosition(oilTypes, currentOil.getType());

//    Toast.makeText(this, currentCar.getCarBrand(), Toast.LENGTH_SHORT).show();
    ArrayAdapter<String> oilBrandsSpinnerAdapter = new ArrayAdapter<String>(App.getInstance(),
        R.layout.spinner_item_none, oilBrands) {
      @Override
      public boolean isEnabled(int position) {
        if (position == 0) {
          // Disable the second item from Spinner
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

        if (position == indexBrand) {
          tv.setBackgroundColor(
              App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
        } else {
          tv.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
      }
    };

    ArrayAdapter<String> oilTypesSpinnerAdapter = new ArrayAdapter<String>(App.getInstance(),
        R.layout.spinner_item, oilTypes) {
      @Override
      public boolean isEnabled(int position) {
        if (position == 0) {
          // Disable the second item from Spinner
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

        if (position == indexType) {
          tv.setBackgroundColor(
              App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
        } else {
          tv.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
      }
    };
//    ArrayAdapter<String> oilVolumesSpinnerAdapter = new ArrayAdapter<String>(App.getInstance(),  R.layout.spinner_item,
//        oilVolumes);
    oilBrandsSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    oilTypesSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//    oilVolumesSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    oilBrandSp.setAdapter(oilBrandsSpinnerAdapter);
    oilTypeSp.setAdapter(oilTypesSpinnerAdapter);
//    oilVolumeSp.setAdapter(oilVolumesSpinnerAdapter);
//    oilServiveDoneDateTv.setText(currentOil.getServiceDoneDate());
//    oilServiveNextDateTv.setText(getNextYear());
//    TextInputLayout t=findViewById(R.id.til_service_done_km);
////    t.setErrorEnabled(true);
//    serviceDoneKmEt.setHint("0");
//    t.setError(currentOil!=null?" ":" ");


    switch (currentCar.getDistanceUnit()){
      case "Km":
        km1Tv.setText(getString(R.string.km));
        km2Tv.setText(getString(R.string.km));
        km3Tv.setText(getString(R.string.km));
        km4Tv.setText(getString(R.string.km));
        midManualTv.setText(getString(R.string.middle_month_set_manually_km));
        break;
      case "Mil":
        km1Tv.setText(getString(R.string.mil));
        km2Tv.setText(getString(R.string.mil));
        km3Tv.setText(getString(R.string.mil));
        km4Tv.setText(getString(R.string.mil));
        midManualTv.setText(getString(R.string.middle_month_set_manually_mil));
        break;
    }

    String[] strings = getResources().getStringArray(R.array.oil_brands);
    String[] oilBrands = Arrays.copyOfRange(strings, 1, strings.length);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, oilBrands);
    oilBrandEt.setAdapter(adapter);

    midMonthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          midMonthTr.setVisibility(View.VISIBLE);
        } else {
//          middleMonthKmEt.setText("");
          midMonthTr.setVisibility(View.GONE);
        }
      }
    });

    phoneNameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!checkPermission()) {

          requestPermission();

          phoneNameSwitch.setChecked(false);

        } else {
          if (isChecked) {
//            phoneNameTr.setVisibility(View.VISIBLE);
          } else {
//            phoneNameTr.setVisibility(View.GONE);
          }
        }
      }
    });

    t1 = new TextWatcher() {
      //      boolean _ignore = false;
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (serviceDoneKmEt.getText().toString().matches("^0")) {
          // Not allowed
          ToastUtils.shortToast("not allowed");
          serviceDoneKmEt.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
//        if (_ignore)
//          return;

//        _ignore = true; // prevent infinite loop

        recomendedKmEt.removeTextChangedListener(t2);
        nextServiceKmEt.removeTextChangedListener(t3);
        String string = String.valueOf(s).equals("") ? "0" : String.valueOf(s);
        c = Long.parseLong(string);

        /////////////////////ed///////////
        if (r >= 0) {
          n = (c + r) < 0 ? 0 : (c + r);
          nextServiceKmEt.setText(n == 0 ? "" : String.valueOf(n));
        }
        /////////////ed//////////////

//        n = (carCount + r) < 0 ? 0 : (carCount + r);
//        r = (n - carCount) < 0 ? 0 : (n - carCount);

//        recomendedKmEt.setText(r == 0 ? "" : String.valueOf(r));
//        nextServiceKmEt.setText(n == 0 ? "" : String.valueOf(n));
        recomendedKmEt.addTextChangedListener(t2);
        nextServiceKmEt.addTextChangedListener(t3);

//        _ignore = false; // release, so the TextWatcher start to listen again.
      }
    };

    t2 = new TextWatcher() {


      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (recomendedKmEt.getText().toString().matches("^0")) {
          // Not allowed
          ToastUtils.shortToast("not allowed");
          recomendedKmEt.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        serviceDoneKmEt.removeTextChangedListener(t1);
        nextServiceKmEt.removeTextChangedListener(t3);
        String string = String.valueOf(s).equals("") ? "0" : String.valueOf(s);
        r = Long.parseLong(string);

        n = (c + r) < 0 ? 0 : (c + r);

        if (r >= 0) {
          nextServiceKmEt.setText(n == 0 ? "" : String.valueOf(n));
        } else {
          nextServiceKmEt.setText("");
        }
//        carCount = (n - r) < 0 ? 0 : (n - r);
//        serviceDoneKmEt.setText(carCount == 0 ? "" : String.valueOf(carCount));
        serviceDoneKmEt.addTextChangedListener(t1);
        nextServiceKmEt.addTextChangedListener(t3);

      }
    };

    t3 = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (nextServiceKmEt.getText().toString().matches("^0")) {
          // Not allowed
          ToastUtils.shortToast("not allowed");
          nextServiceKmEt.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        serviceDoneKmEt.removeTextChangedListener(t1);
        recomendedKmEt.removeTextChangedListener(t2);
        String string = String.valueOf(s).equals("") ? "0" : String.valueOf(s);
        n = Long.parseLong(string);

        r = (n - c) < 0 ? 0 : (n - c);
//        carCount = (n - r) < 0 ? 0 : (n - r);

        if (n > 0) {
          recomendedKmEt.setText(r == 0 ? "" : String.valueOf(r));
        } else {
          recomendedKmEt.setText("");
        }

//        serviceDoneKmEt.setText(carCount == 0 ? "" : String.valueOf(carCount));
        serviceDoneKmEt.addTextChangedListener(t1);
        recomendedKmEt.addTextChangedListener(t2);
      }
    };

    t4 = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (middleMonthKmEt.getText().toString().matches("^0")) {
          // Not allowed
//          ToastUtils.shortToast("not allowed");
          middleMonthKmEt.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };

    serviceDoneKmEt.addTextChangedListener(t1);
    recomendedKmEt.addTextChangedListener(t2);
    nextServiceKmEt.addTextChangedListener(t3);
    middleMonthKmEt.addTextChangedListener(t4);

//    oilCompanyIdEt.addTextChangedListener(new TextWatcher() {
//      @Override
//      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//      }
//
//      @Override
//      public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//      }
//
//      @Override
//      public void afterTextChanged(Editable s) {
//        validateEditText(s);
//      }
//    });

//    oilCompanyIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//      @Override
//      public void onFocusChange(View v, boolean hasFocus) {
//        if (!hasFocus) {
//          validateEditText(((EditText) v).getText());
//        }
//      }
//    });

    setOilToUi(currentOil);

//    notesEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
//    notesEt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

//    new MultiTextWatcher()
//        .registerEditText(recomendedKmEt)
//        .setCallback(new TextWatcherWithInstance() {
//          @Override
//          public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {
//            // TODO: Do some thing with editText
//          }
//
//          @Override
//          public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
//            // TODO: Do some thing with editText
//          }
//
//          @Override
//          public void afterTextChanged(EditText editText, Editable editable) {
//            // TODO: Do some thing with editText
//          }
//        });

  }

  private void setListeners() {

    oilBrandSp.setOnItemSelectedListener(this);
    oilTypeSp.setOnItemSelectedListener(this);

    mPhoneEditText.setOnClickListener(this);
    oilServiveDoneDateTv.setOnClickListener(this);
    oilServiveNextDateTv.setOnClickListener(this);

//    serviceDoneKmTv.setOnClickListener(this);

    from_dateListener = new OnDateSetListener() {
      public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
        Calendar cal = Calendar.getInstance();
        cal.set(arg1, arg2, arg3, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND));
        serviceDoneDate = cal.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(cal.getTime());
        oilServiveDoneDateTv.setText(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(arg1 + 1, arg2, arg3, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND));
        if (cal2.getTimeInMillis() < System.currentTimeMillis()) {
          serviceNextDate = System.currentTimeMillis();
          cal2.setTimeInMillis(System.currentTimeMillis());
        } else {
          serviceNextDate = cal2.getTimeInMillis();
        }
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        String date2 = dateFormat2.format(cal2.getTime());
        oilServiveNextDateTv.setText(date2);
      }
    };

    to_dateListener = new OnDateSetListener() {
      public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
        Calendar cal = Calendar.getInstance();
        cal.set(arg1, arg2, arg3, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND));
        serviceNextDate = cal.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(cal.getTime());
        oilServiveNextDateTv.setText(date);
      }
    };

//    cancelTvBtn.setOnClickListener(this);
//    saveOilTvBtn.setOnClickListener(this);
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

//    oilCompanyIdEt.setOnFocusChangeListener(new OnFocusChangeListener() {
//      @Override
//      public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
////          Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
//
//
//
//          if (!checkPermission()) {
//
//            requestPermission();
//
//          }
//
//        } else {
////          Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
//        }
//      }
//    });

    // actionDone handle
    mPhoneEditText.setOnEditorActionListener(this);
    oilVolumeEt.setOnEditorActionListener(this);
    oilBrandEt.setOnEditorActionListener(this);
    serviceDoneKmEt.setOnEditorActionListener(this);
    nextServiceKmEt.setOnEditorActionListener(this);
    recomendedKmEt.setOnEditorActionListener(this);
    middleMonthKmEt.setOnEditorActionListener(this);
//    notesEt.setOnEditorActionListener(this);

    imgI1.setOnClickListener(this);
    imgI2.setOnClickListener(this);
    imgI3.setOnClickListener(this);
    imgI4.setOnClickListener(this);
    editNotesImg.setOnClickListener(this);

    //edittexti scrolli hamar
    findViewById(R.id.scroll).setOnTouchListener(new View.OnTouchListener() {
      public boolean onTouch(View p_v, MotionEvent p_event) {
        // this will disallow the touch request for parent scroll on touch of child view
        p_v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
      }
    });

    saveOilChangesFab.bringToFront();
    saveOilChangesFab.setOnClickListener(this);
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

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_oil_service_done_date:
        showDatePickerDialog(from_dateListener, DATE_PICKER_FROM);
        break;
      case R.id.tv_oil_service_next_date:
        showDatePickerDialog(to_dateListener, DATE_PICKER_TO);
        break;
//      case R.id.tv_cancel_as_btn:
//        break;
      case R.id.fab_save_oil_changes:
//        if(new ValidationUtils(this).isKmDataValid(serviceDoneKmTil, nextServiceKmTil,recomendedKmTil,middleMonthKmTil,
//            serviceDoneKmEt.getText().toString(), nextServiceKmEt.getText().toString(),recomendedKmEt.getText().toString(),middleMonthKmEt.getText().toString(),currentOil)){
//          showProgress();
//          saveEditedOilData();
//        }
//        DialogUtil.getInstance().showLoading(EditOilActivity.this);
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(this);
        if (checkInputs()) {
          saveEditedOilData();
        }
        break;
      case R.id.et_service_done_km:

//        showEditDialog(serviceDoneKmTv.getText().toString());

        break;
      case R.id.img_edit_notes:
        showEditNotesDialog(notesTv.getText().toString());
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        notesCv.setVisibility(View.INVISIBLE);

        break;
      case R.id.img_i1:
        showInfoDialog(getString(R.string.dlg_info_content_for_current));
        break;
      case R.id.img_i2:
        showInfoDialog(getString(R.string.dlg_info_content_for_recommended));
        break;
      case R.id.img_i3:
        showInfoDialog(getString(R.string.dlg_info_content_for_next));
        break;
      case R.id.img_i4:
        showInfoDialog(getString(R.string.dlg_info_content_for_middle));
        break;
      default:
    }
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(EditOilActivity.this, MainActivity.class);
    intent.setAction(ACTION_EDIT_OIL_ACTIVITY_INTENT);
    startActivity(intent);
    finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
    super.onBackPressed();
  }

  private void setOilToUi(Oil uiOil) {
    PhoneNumber phoneNumber = PhoneNumberUtils.getPhoneNumber(uiOil.getServiceCompanyId());
    setPhoneNumber(phoneNumber);
    setCountryCode(phoneNumber);
//    oilCompanyIdEt.setText(uiOil.getServiceCompanyId());
    phoneNameEt.setText(uiOil.getServiceOwnerName());
    oilBrandEt.setText(uiOil.getBrand());
    oilBrandSp.setSelection(getSpinnerPosition(oilBrands, currentOil.getBrand()), true);
    oilTypeSp.setSelection(getSpinnerPosition(oilTypes, currentOil.getType()), true);
    oilTypeSp.setSelection(indexType);
    oilVolumeEt.setText(String.valueOf(uiOil.getVolume()));
    serviceDoneKmEt.setText(String.valueOf(uiOil.getServiceDoneKm()));
    nextServiceKmEt.setText(String.valueOf(uiOil.getServiceNextKm()));
    recomendedKmEt.setText(String.valueOf(uiOil.getRecomendedKm()));
    middleMonthKmEt.setText(String.valueOf(uiOil.getMiddleMonthKm()));
    oilServiveDoneDateTv.setText(uiOil.getServiceDoneDate() == 0 ? "-"
        : longToString(uiOil.getServiceDoneDate(), getDateFormat(DateType.DMY)));
    oilServiveNextDateTv.setText(uiOil.getServiceNextDate() == 0 ? "-"
        : longToString(uiOil.getServiceNextDate(), getDateFormat(DateType.DMY)));

    serviceDoneDate = uiOil.getServiceDoneDate();
    serviceNextDate = uiOil.getServiceNextDate();
    middleMonth = uiOil.getMiddleMonthKm();
    String notesText = appSharedHelper.getOilNotes(currentCar.getKey(), uiOil.getKey());
    notesTv.setText(notesText);
    setTextViewDrawableColor(oilServiveDoneDateTv, R.color.colorPrimary);
    setTextViewDrawableColor(oilServiveNextDateTv, R.color.colorPrimary);
  }

  private int getSpinnerPosition(String[] arr, String oilBrand) {
    int index = -1;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i].equals(oilBrand)) {
        index = i;
        break;
      }
    }
    return index;
  }

  // convert from bitmap to byte array
  public byte[] getBytesFromBitmap(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(CompressFormat.JPEG, 70, stream);
    return stream.toByteArray();
  }

  private void addPhoneNumberToContactList(String mobileNumber,
      String displayName) { //Permitioni harcy lucel ... +
//    final String displayName = getString(R.string.app_name);
//    final String mobileNumber = "+37477889900";
    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_icoil2);
    final byte[] photoByteArray = getBytesFromBitmap(largeIcon); // initalized elsewhere

    ArrayList<ContentProviderOperation> ops = new ArrayList<>();
    int rawContactID = ops.size();

    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "")
        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "")
        .build());

    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
        .withValue(ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
        //.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)
        .build());

    // Adding insert operation to operations list
    // to insert Mobile Number in the table ContactsContract.Data
    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
        .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
        .withValue(Phone.NUMBER, mobileNumber)
        .withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
        .build());

    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
        .withValue(ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photoByteArray)
        .build());

    Uri newContactUri = null;
    ContentProviderResult[] res = null;
    try {
      final ContentResolver contentResolver = getContentResolver();
      res = contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
      if (res != null && res.length > 0 && res[0] != null) {
        newContactUri = res[0].uri;
        Log.d(CreateNewOilActivity.class.getName(), "URI added contact:" + newContactUri);
//        Toast.makeText(this, "Successfully added " + displayName, Toast.LENGTH_LONG).show();
      } else {
        Log.e(CreateNewOilActivity.class.getName(), "Contact not added.");
      }
    } catch (NullPointerException | RemoteException | OperationApplicationException e) {
      Log.e(CreateNewOilActivity.class.getName(), e.getMessage(), e);
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
//        DialogUtil.getInstance().showLoading(EditOilActivity.this);
        AppUtil.closeKeyboard(this);
        DialogManager.getInstance().showPreloader(this);

        if (checkInputs()) {
          saveEditedOilData();
        }
        return true;
      case android.R.id.home:
//        finish();
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showEditNotesDialog(String notesText) {
    FragmentManager fm = getSupportFragmentManager();
    EditNotesDialogFragment editNotesDialogFragment = EditNotesDialogFragment
        .newInstance("Notes", notesText);
    editNotesDialogFragment.show(fm, "fragment_edit_notes");
  }

  private void saveEditedOilData() {
    Oil oil = getEditedOil();

    mViewModel.editOil(oil);
    if (phoneNameSwitch.isChecked()) {
      addPhoneNumberToContactList(oil.getServiceCompanyId(),
          phoneNameEt.getText().toString());
    }

    appSharedHelper.saveOilNotes(currentCar.getKey(), oil.getKey(), notesTv.getText().toString());
    setNotification(oil);



    appSharedHelper.saveOilNotes(currentCar.getKey(), oil.getKey(), notesTv.getText().toString());
    setNotification(oil);

    ToastUtils.shortToast(R.string.msg_oil_changed);
    Intent intent = new Intent(EditOilActivity.this, MainActivity.class);
//        intent.setAction(ACTION_EDIT_OIL_ACTIVITY_INTENT);
    startActivity(intent);
    finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//    mAppRepository.editOil(  oil, new ResultListener<Oil>() {
//      @Override
//      public void onLoad(Oil oil) {
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            if (oil != null) {
//
//              if (phoneNameSwitch.isChecked()) {
//                addPhoneNumberToContactList(oil.getServiceCompanyId(),
//                    phoneNameEt.getText().toString());
//              }
//
//              appSharedHelper.saveOilNotes(currentCar.getKey(), oil.getKey(), notesTv.getText().toString());
//              setNotification(oil);
//
//              ToastUtils.shortToast(R.string.msg_oil_changed);
//              Intent intent = new Intent(EditOilActivity.this, MainActivity.class);
////        intent.setAction(ACTION_EDIT_OIL_ACTIVITY_INTENT);
//              startActivity(intent);
//              finish();
////    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
//              overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//            } else {
//              DialogManager.getInstance().dismissPreloader(this.getClass());
//
//              ToastUtils.shortToast(R.string.msg_oil_not_changed);
//            }
//          }
//        });

//      }
//
//      @Override
//      public void onFail(String e) {
//        DialogManager.getInstance().dismissPreloader(this.getClass());
//
//        ToastUtils.shortToast(R.string.msg_oil_not_changed);
//      }
//
//    });

//    OilDataDbHelper.getInstance().editOil(currentCar.getKey(),indexOfCurrentOil,oil,new ResultListener<Oil>() {
//      @Override
//      public void onSuccess(Oil editedOil) {
//        setNotification(oil);
//
//        ToastUtils.shortToast("Oil changed!");
//        Intent intent = new Intent(EditOilActivity.this,MainActivity.class);
////        intent.setAction(ACTION_EDIT_OIL_ACTIVITY_INTENT);
//        startActivity(intent);
//        finish();
////    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
//        overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);
//      }
//
//      @Override
//      public void onFail(Exception e) {
////        DialogUtil.getInstance().dismissDialog();
//        DialogManager.getInstance().dismissPreloader(this.getClass());
//
//        ToastUtils.shortToast("Oil not changed!");
//      }
//    });

//    CarDataDbHalper.getInstance().saveCarOil(user.getKey(), currentCar);
  }

  private Oil getEditedOil() {
//    Oil oil=new Oil();
    currentOil.setKey(currentOil.getKey());
    currentOil.setCarKey(currentOil.getCarKey());
//    oil.setId(currentOil.getId());
    String phoneNumber = getPseudoValidPhoneNumber();
//    if (phoneNumber == null) {
//      mErrorEditText.setText(com.firebase.ui.auth.R.string.fui_invalid_phone_number);
//    }

//    oil.setServiceCompanyId(oilCompanyIdEt.getText().toString());
    currentOil.setServiceCompanyId(phoneNumber);
    currentOil.setServiceOwnerName(phoneNameEt.getText()==null?"":phoneNameEt.getText().toString());
    currentOil.setServiceDoneDate(serviceDoneDate);
    currentOil.setServiceNextDate(serviceNextDate);
    currentOil.setBrand(oilBrandEt.getText().toString());
    currentOil.setType(oilTypeSp.getSelectedItem().toString());
    currentOil.setVolume(getDouble(oilVolumeEt.getText().toString()));
    currentOil.setServiceDoneKm(Long.valueOf(serviceDoneKmEt.getText().toString()));
    currentOil.setServiceNextKm(Long.valueOf(nextServiceKmEt.getText().toString()));
    currentOil.setRecomendedKm(Long.valueOf(recomendedKmEt.getText().toString()));
    if (midMonthSwitch.isChecked()) {
      middleMonth = 0;
//    long m;

//      if((middleMonthKmEt.getText().toString()).equals("")){
//
//        m = firstOil != null ? calcMiddleMontKm() : 0;
//
//      }else {
      if (!TextUtils.isEmpty(middleMonthKmEt.getText().toString())) {
        middleMonth = Long.parseLong(middleMonthKmEt.getText().toString());
      }
//      oil.setMiddleMonthKm(middleMonth);
    } else {
      if (Long.valueOf(serviceDoneKmEt.getText().toString()) != currentOil.getServiceDoneKm()) {
        middleMonth = calcMiddleMontKm();
      }
    }
    currentOil.setMiddleMonthKm(middleMonth);
//    spinnerItem.setMiddleMonthKm(middleMonthKmEt.getText().toString().equals("")?0:Long.valueOf(middleMonthKmEt.getText().toString()));
    currentOil.setIsFilterChanged(0);

    return currentOil;
  }

  public void setNotification(Oil editedOil) {
//    mNotificationsRepository.deletPreviusMonthNotification(car,currentOil);
    mNotificationsRepository.setMonthlyNotification(currentOil, editedOil);
  }

  public static boolean infinity(double numer, double denom) {
    return Double.isInfinite(numer / denom);
  }

  private long calcMiddleMontKm() {

    double d = 0;

    if (firstOil == null) {
      return (long) d;
    }

    long kmDiff = kmDifference(firstOil.getServiceDoneKm(),
        Long.valueOf(serviceDoneKmEt.getText().toString()));
    long mDiff = getDateDifferenceInDDMMYYYY(longToDate(firstOil.getServiceDoneDate()),
        longToDate(serviceDoneDate));

    if (kmDiff > 0 && mDiff > 0) {
      d = (double) kmDiff / mDiff;
    }

//    if (firstOil.getServiceDoneKm() != Long.valueOf(serviceDoneKmEt.getText().toString())) {
//      try {
//        if (!infinity(kmDifference(firstOil.getServiceDoneKm(),
//            Long.valueOf(serviceDoneKmEt.getText().toString())),
//            getDateDifferenceInDDMMYYYY(longToDate(firstOil.getServiceDoneDate()),
//                stringToDate(oilServiveDoneDateTv.getText().toString(),
//                    getDateFormat(DateType.DMY))))) {
//
//          NotificationActionsFragment = (double) (kmDifference(firstOil.getServiceDoneKm(),
//              Long.valueOf(serviceDoneKmEt.getText().toString())))
//              / (getDateDifferenceInDDMMYYYY(longToDate(firstOil.getServiceDoneDate()),
//              stringToDate(oilServiveDoneDateTv.getText().toString(),
//                  getDateFormat(DateType.DMY))));
//        }
//      } catch (ParseException e) {
//        e.printStackTrace();
//      }
//    }

    Logger.d("testt", "double value: " + d);
    return (long) d;
  }

  private long kmDifference(long kmFirst, long kmEnd) {
    Logger.d("testt", "kmDifference: " + (kmEnd - kmFirst));
    return kmEnd - kmFirst < 0 ? 0 : kmEnd - kmFirst;
  }

  private void showDatePickerDialog(OnDateSetListener dateListener, int id) {
    Calendar now = Calendar.getInstance();
    now.setTimeInMillis(currentOil.getServiceDoneDate());

    int yy = now.get(Calendar.YEAR);
    int mm = now.get(Calendar.MONTH);
    int dd = now.get(Calendar.DAY_OF_MONTH);
    switch (id) {
      case DATE_PICKER_FROM: {
//        yy = now.getAppNotification(Calendar.YEAR);
//        mm = now.getAppNotification(Calendar.MONTH);
//        dd = now.getAppNotification(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd1 = new DatePickerDialog(this, R.style.Datepicker, dateListener, yy, mm,
            dd);
//        dpd1.setTitle("Select the date");
        dpd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dpd1.getDatePicker().setMinDate(firstOil.getServiceDoneDate());
        dpd1.show();
      }
      break;
      case DATE_PICKER_TO: {
        yy = now.get(Calendar.YEAR) + 1;
//        mm = now.getAppNotification(Calendar.MONTH);
//        dd = now.getAppNotification(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd1 = new DatePickerDialog(this, R.style.Datepicker, dateListener, yy, mm,
            dd);
//        dpd1.setTitle("Select the date");
        dpd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
        dpd1.show();
      }
      break;
    }

  }

  @Override
  public void onFinishEditDialog(boolean action, String inputText) {

    notesCv.setVisibility(View.VISIBLE);
//    notesCv.requestFocus();
    if (action) {
      notesTv.setText(inputText);

    }
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    TextView convertView = ((TextView) view.findViewById(R.id.spinner_item_id_tv));
    Drawable[] compoundDrawables = convertView.getCompoundDrawables();
    Drawable rightCompoundDrawable = compoundDrawables[2];
    setTextViewDrawableColor(convertView, R.color.colorAccent);
    switch (parent.getId()) {
      case R.id.spinner_oil_brand:
        if (position > 0) {
          oilBrandEt.setText(oilBrandSp.getSelectedItem().toString());
        }
        this.indexBrand = position;

        break;
      case R.id.spinner_oil_type:
        this.indexType = position;

//        if(position==0){
//          ((TextView)oilTypeSp.getChildAt(0)).setError("Not Selected");
//        }
        break;
      default:
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//    if(event!=null){
    if (actionId == EditorInfo.IME_ACTION_DONE) {
//        Toast.makeText(getApplicationContext(), "IME_ACTION_DONE", Toast.LENGTH_LONG).show();
      AppUtil.closeKeyboard(EditOilActivity.this);
//                oilCompanyIdEt.setFocusable(false);
//                oilCompanyIdEt.setFocusableInTouchMode(true);
      v.clearFocus();
      return true;
    }
    return false;

//    }
//      return false;
  }

  private void showInfoDialog(String content) {

    new MaterialDialog.Builder(this)
        .title(R.string.dlg_info_title)
        .content(content)
        .positiveText(R.string.dlg_ok)
        .positiveColor(getResources().getColor(R.color.colorAccent))
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .show();

  }

  private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
//    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

    return result
        == PackageManager.PERMISSION_GRANTED;//&& result1 == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {

    ActivityCompat.requestPermissions(this, new String[]{WRITE_CONTACTS}, PERMISSION_REQUEST_CODE);

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean writeContactsAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//          boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

          if (!writeContactsAccepted) {

//            Snackbar.make(root, "Permission Denied, You cannot write contacts.", Snackbar.LENGTH_LONG).show();

            mPhoneEditText.clearFocus();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(WRITE_CONTACTS)) {

//                showMessageOKCancel("You need to allow access the CONTACTS permission",
//                    // OK
//                    new DialogInterface.OnClickListener() {
//                      @Override
//                      public void onClick(DialogInterface dialog, int which) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                          requestPermissions(new String[]{WRITE_CONTACTS},
//                              PERMISSION_REQUEST_CODE);
//                        }
//                      }
//                    },
//                    // Cancel
//                    new DialogInterface.OnClickListener() {
//                      @Override
//                      public void onClick(DialogInterface dialog, int which) {
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                          Intent intent = new Intent();
////                          intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                          Uri uri = Uri.fromParts("package", getPackageName(), null);
////                          intent.setData(uri);
////                          startActivity(intent);
////                          finish();
//                        }
//                      }
//                    }
//                );
                return;
              } else {
                Snackbar snackbar = Snackbar
                    .make(root, R.string.permission_denided,
                        Snackbar.LENGTH_LONG)
                    .setAction(R.string.snake_bar_action_msg_allow, new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          Intent intent = new Intent();
                          intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                          Uri uri = Uri.fromParts("package", getPackageName(), null);
                          intent.setData(uri);
                          startActivity(intent);
//                          finish();
                        }
                      }
                    });
                snackbar.show();
              }
            }

          } else {
            phoneNameSwitch.setChecked(true);

          }
        }

        break;
    }
  }

  private void setPhoneNumber(PhoneNumber phoneNumber) {
    if (PhoneNumber.isValid(phoneNumber)) {
      mPhoneEditText.setText(phoneNumber.getPhoneNumber());
      mPhoneEditText.setSelection(phoneNumber.getPhoneNumber().length());
    }
  }

  private void setCountryCode(PhoneNumber phoneNumber) {
    if (PhoneNumber.isCountryValid(phoneNumber)) {
      mCountryListSpinner.setSelectedForCountry(new Locale("", phoneNumber.getCountryIso()),
          phoneNumber.getCountryCode());
    }
  }

  @Nullable
  private String getPseudoValidPhoneNumber() {
    final CountryInfo countryInfo = (CountryInfo) mCountryListSpinner.getTag();
    final String everythingElse = mPhoneEditText.getText().toString();

    if (TextUtils.isEmpty(everythingElse)) {
      return null;
    }

    return PhoneNumberUtils.formatPhoneNumber(everythingElse, countryInfo);
  }

  private boolean checkInputs() {

//    if (!checkPermission()) {
////      DialogUtil.getInstance().dismissDialog();
//      DialogManager.getInstance().dismissPreloader(this.getClass());
//      requestPermission();
//      return false;
//    }

//    if(!oilCompanyIdEt.getRawText().startsWith("374")){
//      focusOnView(nestedScrollView,oilCompanyIdEt);
//      oilCompanyIdEt.setError("Phone number must start with 374... !");
////      DialogUtil.getInstance().dismissDialog();
//      DialogManager.getInstance().dismissPreloader(this.getClass());
//
//      return false;
//    }

    if (TextUtils.isEmpty(oilBrandEt.getText())) {
      focusOnView(nestedScrollView, oilBrandEt);
      oilBrandEt.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (oilTypeSp.getSelectedItemPosition() == 0) {
      focusOnView(nestedScrollView, oilTypeSp);
      ((TextView) oilTypeSp.getChildAt(0)).setError(getString(R.string.err_msg_not_selected));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (TextUtils.isEmpty(oilServiveDoneDateTv.getText())) {
      focusOnView(nestedScrollView, oilServiveDoneDateTv);
      oilServiveDoneDateTv.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (TextUtils.isEmpty(oilServiveNextDateTv.getText())) {
      focusOnView(nestedScrollView, oilServiveNextDateTv);
      oilServiveNextDateTv.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (TextUtils.isEmpty(serviceDoneKmEt.getText())) {
      focusOnView(nestedScrollView, serviceDoneKmEt);
      serviceDoneKmEt.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (Long.valueOf(serviceDoneKmEt.getText().toString()) < currentOil.getServiceDoneKm()) {
      focusOnView(nestedScrollView, serviceDoneKmEt);
      serviceDoneKmEt.setError(getString(R.string.err_msg_must_be_more));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (TextUtils.isEmpty(nextServiceKmEt.getText())) {
      focusOnView(nestedScrollView, nextServiceKmEt);
      nextServiceKmEt.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    if (TextUtils.isEmpty(recomendedKmEt.getText())) {
      focusOnView(nestedScrollView, recomendedKmEt);
      recomendedKmEt.setError(getString(R.string.err_msg_empty));
//      DialogUtil.getInstance().dismissDialog();
      DialogManager.getInstance().dismissPreloader(this.getClass());

      return false;
    }

    String phoneNumber = getPseudoValidPhoneNumber();
    if (phoneNumber == null) {
      focusOnView(nestedScrollView, mPhoneEditText);
      mErrorEditText.setText(R.string.err_msg_enter_valid_phone_number);
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (mPhoneEditText.getText().toString().length() > 10) {
      focusOnView(nestedScrollView, mPhoneEditText);
      mErrorEditText.setText(getString(R.string.err_msg_empty));
      DialogManager.getInstance().dismissPreloader(this.getClass());
      return false;
    }

    if (phoneNameSwitch.isChecked()) {
      if (TextUtils.isEmpty(phoneNameEt.getText())) {
        focusOnView(nestedScrollView, phoneNameEt);
        phoneNameEt.setError(getString(R.string.err_msg_empty));
        DialogManager.getInstance().dismissPreloader(this.getClass());
        return false;
      }
    }

    return true;
  }

}
