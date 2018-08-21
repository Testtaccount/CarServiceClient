package am.gsoft.carserviceclient.ui.fragment;

import am.gsoft.carserviceclient.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NextReminderFragment extends BaseFragment implements OnClickListener {

  public static final String TAG = NextReminderFragment.class.getSimpleName();

  private FrameLayout updateCalendarBtnFl;
  private TextView reminderTxtTv;
  private CardView reminderDateTxtCv;
  private LinearLayout emptyInfoLl;

  private OnNextReminderFragmentInteractionListener mListener;

  public static NextReminderFragment newInstance() {
    return new NextReminderFragment();
  }

  public static NextReminderFragment newInstance(Bundle args) {
    NextReminderFragment fragment = new NextReminderFragment();
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
    View view = inflater.inflate(R.layout.fragment_next_reminder, container, false);
    findViews(view);
//    openDatePicker();
    initFields();
    setListeners();
    getData();
    customizeActionBar();

    return view;
  }

  private void findViews(View view) {
    updateCalendarBtnFl = view.findViewById(R.id.fl_update_calendar_btn);
    reminderTxtTv = view.findViewById(R.id.tv_reminder_txt);
    reminderDateTxtCv = view.findViewById(R.id.cv_reminder_date_txt);
    emptyInfoLl = view.findViewById(R.id.ll_empty_info);
  }

  private void customizeActionBar() {
    setActionBarTitle("Select Next Date");
  }

  private void initFields() {

  }

  private void setListeners() {
    updateCalendarBtnFl.setOnClickListener(this);
  }

  public void getData() {
    if (getArguments() != null) {
//      oils = getArguments().getParcelableArrayList(Argument.ARGUMENT_OIL_HISTORY_LIST);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_update_calendar_btn:
        if (mListener != null) {
          mListener.onUpdateCalendarClick();
        }
        break;
    }
  }

  public void setReminderText(String text) {
    reminderTxtTv.setText(text);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnNextReminderFragmentInteractionListener) {
      mListener = (OnNextReminderFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnNextReminderFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnNextReminderFragmentInteractionListener {

    void onUpdateCalendarClick();

  }
}
