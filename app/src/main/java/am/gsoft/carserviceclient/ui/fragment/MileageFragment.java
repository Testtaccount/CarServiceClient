package am.gsoft.carserviceclient.ui.fragment;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.util.AppUtil;
import am.gsoft.carserviceclient.util.Constant;
import am.gsoft.carserviceclient.util.Constant.Argument;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MileageFragment extends BaseFragment implements OnClickListener {

  public static final String TAG = MileageFragment.class.getSimpleName();

  private CardView reminderDateTxtCv;
  private CardView inputMileageCv;
  private FrameLayout setMileageFlBtn;
  private FrameLayout updateMileageFlBtn;
  private TextInputLayout serviceDoneKmTil;
  private EditText newMileageEt;
  private TextView km1Tv;
  private TextView reminderTxtTv;


  private Car mCar;
  //  private Oil mOil;
  private OnMileageFragmentInteractionListener mListener;

  public static MileageFragment newInstance() {
    return new MileageFragment();
  }

  public static MileageFragment newInstance(Car car) {
    Bundle args = new Bundle();
    args.putParcelable(Constant.Argument.ARGUMENT_CAR, car);
//    args.putParcelable(Constant.Argument.ARGUMENT_OIL, oil);
    MileageFragment fragment = new MileageFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_mileage, container, false);
    findViews(view);
    getData();
    initFields();
    setListeners();
    customizeActionBar();

    return view;
  }

  private void findViews(View view) {
//    root = view.findViewById(R.id.root_update_mileage);
//    mLayout = findViewById(R.id.wraper);

    serviceDoneKmTil = (TextInputLayout) view.findViewById(R.id.til_updated_km);
    newMileageEt = view.findViewById(R.id.et_new_mileage);
    km1Tv = (TextView) view.findViewById(R.id.tv_km);
    setMileageFlBtn = view.findViewById(R.id.fl_set_mileage_btn);
    updateMileageFlBtn = view.findViewById(R.id.fl_update_mileage_btn);
    reminderTxtTv = view.findViewById(R.id.tv_reminder_txt);
    reminderDateTxtCv = view.findViewById(R.id.cv_reminder_date_txt);
    inputMileageCv = view.findViewById(R.id.cv_input_mileage);
  }

  private void customizeActionBar() {
    setActionBarTitle("Set Mileage");
  }

  private void initFields() {
    if (mCar != null) {
      km1Tv.setText(mCar.getDistanceUnit());
    }
  }

  public void getData() {
    if (getArguments() != null) {
      mCar = getArguments().getParcelable(Argument.ARGUMENT_CAR);
//      mOil = getArguments().getParcelable(Argument.ARGUMENT_OIL);
    }
  }

  private void setListeners() {
    setMileageFlBtn.setOnClickListener(this);
    updateMileageFlBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_set_mileage_btn:
        AppUtil.closeKeyboard(getActivity());
        if (mListener != null) {
          mListener.onSetMileageClick(newMileageEt.getText().toString());
        }
        break;
      case R.id.fl_update_mileage_btn:
        inputMileageCv.setVisibility(View.VISIBLE);
        setMileageFlBtn.setVisibility(View.VISIBLE);
        reminderTxtTv.setText("");
        reminderDateTxtCv.setVisibility(View.GONE);
        updateMileageFlBtn.setVisibility(View.GONE);
        serviceDoneKmTil.setErrorEnabled(false);
        break;
    }
  }

  public void setError(String error) {
    serviceDoneKmTil.setErrorEnabled(true);
    serviceDoneKmTil.setError(error);
  }

  public void setReminderText(String text) {
    inputMileageCv.setVisibility(View.GONE);
    setMileageFlBtn.setVisibility(View.GONE);
    reminderTxtTv.setText(text);
    reminderDateTxtCv.setVisibility(View.VISIBLE);
    updateMileageFlBtn.setVisibility(View.VISIBLE);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnMileageFragmentInteractionListener) {
      mListener = (OnMileageFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnMileageFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnMileageFragmentInteractionListener {

    void onSetMileageClick(String value);

    void onUpdateMileageClick();
  }

}
