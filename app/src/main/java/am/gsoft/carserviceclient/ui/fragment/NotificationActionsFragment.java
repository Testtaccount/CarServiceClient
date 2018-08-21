package am.gsoft.carserviceclient.ui.fragment;


import am.gsoft.carserviceclient.R;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class NotificationActionsFragment extends BaseFragment implements OnClickListener {

  public static final String TAG = NotificationActionsFragment.class.getSimpleName();

  private FrameLayout pickFlBtn;
  private FrameLayout updateFlBtn;
  private Bundle mArgumentData;
  private OnNotificationActionsFragmentInteractionListener mListener;

  public NotificationActionsFragment() {
    // Required empty public constructor
  }

  public static NotificationActionsFragment newInstance() {
    return new NotificationActionsFragment();
  }

  public static NotificationActionsFragment newInstance(Bundle args) {
    NotificationActionsFragment fragment = new NotificationActionsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notification_actions, container, false);
    customizeActionBar();
    pickFlBtn = view.findViewById(R.id.fl_pick_btn);
    updateFlBtn = view.findViewById(R.id.fl_update_btn);

    pickFlBtn.setOnClickListener(this);
    updateFlBtn.setOnClickListener(this);
    return view;
  }


  private void customizeActionBar() {
//    setActionBarTitle(getResources().getString(R.string.app_name));
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_pick_btn:
        if (mListener != null) {
          mListener.onCalendarClick();
        }
        break;
      case R.id.fl_update_btn:
        if (mListener != null) {
          mListener.onMileageClick();
        }
        break;
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnNotificationActionsFragmentInteractionListener) {
      mListener = (OnNotificationActionsFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnNotificationActionsFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnNotificationActionsFragmentInteractionListener {

    void onCalendarClick();

    void onMileageClick();
  }

}