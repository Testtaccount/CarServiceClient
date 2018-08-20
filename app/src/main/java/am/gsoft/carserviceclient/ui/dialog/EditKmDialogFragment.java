package am.gsoft.carserviceclient.ui.dialog;

import am.gsoft.carserviceclient.R;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditKmDialogFragment extends DialogFragment implements View.OnClickListener, OnEditorActionListener {

  private EditText mEditText;
  private Button setBtn;
  private Button cancelBtn;
  private String initKm;
  private FrameLayout inc0Btn,decr0Btn;
  private FrameLayout inc1Btn,decr1Btn;
  private FrameLayout inc2Btn,decr2Btn;
  private FrameLayout inc3Btn,decr3Btn;
  private FrameLayout inc4Btn,decr4Btn;
  private FrameLayout inc5Btn,decr5Btn;

  private TextView val0Tv;
  private TextView val1Tv;
  private TextView val2Tv;
  private TextView val3Tv;
  private TextView val4Tv;
  private TextView val5Tv;

  public EditKmDialogFragment() {
    // Empty constructor is required for DialogFragment
    // Make sure not to add arguments to the constructor
    // Use `newInstance` instead as shown below
  }

  public static EditKmDialogFragment newInstance(String title,String initKm) {
    EditKmDialogFragment frag = new EditKmDialogFragment();
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("initKm", initKm);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.fragment_enter_km, container,false);
    findViews(view);
    initFields();
    setListeners();

    return view;
  }


  private void  findViews(View view){
// Get field from view
    mEditText = (EditText) view.findViewById(R.id.txt_your_name);
    setBtn = (Button) view.findViewById(R.id.btn_set);
    cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

    inc0Btn = (FrameLayout) view.findViewById(R.id.inc_0);
    inc1Btn = (FrameLayout) view.findViewById(R.id.inc_1);
    inc2Btn = (FrameLayout) view.findViewById(R.id.inc_2);
    inc3Btn = (FrameLayout) view.findViewById(R.id.inc_3);
    inc4Btn = (FrameLayout) view.findViewById(R.id.inc_4);
    inc5Btn = (FrameLayout) view.findViewById(R.id.inc_5);

    decr0Btn = (FrameLayout) view.findViewById(R.id.decr_0);
    decr1Btn = (FrameLayout) view.findViewById(R.id.decr_1);
    decr2Btn = (FrameLayout) view.findViewById(R.id.decr_2);
    decr3Btn = (FrameLayout) view.findViewById(R.id.decr_3);
    decr4Btn = (FrameLayout) view.findViewById(R.id.decr_4);
    decr5Btn = (FrameLayout) view.findViewById(R.id.decr_5);

    val0Tv=(TextView)view.findViewById(R.id.val_0);
    val1Tv=(TextView)view.findViewById(R.id.val_1);
    val2Tv=(TextView)view.findViewById(R.id.val_2);
    val3Tv=(TextView)view.findViewById(R.id.val_3);
    val4Tv=(TextView)view.findViewById(R.id.val_4);
    val5Tv=(TextView)view.findViewById(R.id.val_5);


    // Fetch arguments from bundle and set title
    String title = getArguments().getString("title", "KM");
    getDialog().setTitle(title);

    initKm = getArguments().getString("initKm", "000000");
    getDialog().setTitle(title);

    // Show soft keyboard automatically and request focus to field
    mEditText.requestFocus();
    mEditText.setOnEditorActionListener(this);

    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  private void initFields(){
    getDialog().setCanceledOnTouchOutside(false);
    char[] n=new char[6];
    n=initKm.toCharArray();
    val0Tv.setText(String.valueOf(n[0]));
    val1Tv.setText(String.valueOf(n[1]));
    val2Tv.setText(String.valueOf(n[2]));
    val3Tv.setText(String.valueOf(n[3]));
    val4Tv.setText(String.valueOf(n[4]));
    val5Tv.setText(String.valueOf(n[5]));
  }

  private void setListeners() {
    setBtn.setOnClickListener(this);
    cancelBtn.setOnClickListener(this);

    inc0Btn.setOnClickListener(this);
    inc1Btn.setOnClickListener(this);
    inc2Btn.setOnClickListener(this);
    inc3Btn.setOnClickListener(this);
    inc4Btn.setOnClickListener(this);
    inc5Btn.setOnClickListener(this);

    decr0Btn.setOnClickListener(this);
    decr1Btn.setOnClickListener(this);
    decr2Btn.setOnClickListener(this);
    decr3Btn.setOnClickListener(this);
    decr4Btn.setOnClickListener(this);
    decr5Btn.setOnClickListener(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }


  private int increment(int i) {
    int m = ++i;
    return m >= 10 ? 9 : m;
  }

  private int decrement(int i) {
    int m = --i;
    return m < 0 ? 0 : m;
  }





  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (EditorInfo.IME_ACTION_DONE == actionId) {
      // Return input text back to activity through the implemented listener
      EditKmDialogListener listener = (EditKmDialogListener) getActivity();
      listener.onFinishEditDialog(mEditText.getText().toString());
      // Close the dialog and return back to the parent activity
      dismiss();
      return true;
    }
    return false;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_set:
        set();
        break;
        case R.id.btn_cancel:
        dismiss();
        break;
      case R.id.inc_0:
        int i0=Integer.parseInt(val0Tv.getText().toString());
        val0Tv.setText(""+increment(i0));
        break;
      case R.id.decr_0:
        int j0=Integer.parseInt(val0Tv.getText().toString());
        val0Tv.setText(""+decrement(j0));
        break;
      case R.id.inc_1:
        int i1=Integer.parseInt(val1Tv.getText().toString());
        val1Tv.setText(""+increment(i1));
        break;
      case R.id.decr_1:
        int j1=Integer.parseInt(val1Tv.getText().toString());
        val1Tv.setText(""+decrement(j1));
        break;
      case R.id.inc_2:
        int i2=Integer.parseInt(val2Tv.getText().toString());
        val2Tv.setText(""+increment(i2));
        break;
      case R.id.decr_2:
        int j2=Integer.parseInt(val2Tv.getText().toString());
        val2Tv.setText(""+decrement(j2));
        break;
      case R.id.inc_3:
        int i3=Integer.parseInt(val3Tv.getText().toString());
        val3Tv.setText(""+increment(i3));
        break;
      case R.id.decr_3:
        int j3=Integer.parseInt(val3Tv.getText().toString());
        val3Tv.setText(""+decrement(j3));
        break;
      case R.id.inc_4:
        int i4=Integer.parseInt(val4Tv.getText().toString());
        val4Tv.setText(""+increment(i4));
        break;
      case R.id.decr_4:
        int j4=Integer.parseInt(val4Tv.getText().toString());
        val4Tv.setText(""+decrement(j4));
        break;
      case R.id.inc_5:
        int i5=Integer.parseInt(val5Tv.getText().toString());
        val5Tv.setText(""+increment(i5));
        break;
      case R.id.decr_5:
        int j5=Integer.parseInt(val5Tv.getText().toString());
        val5Tv.setText(""+decrement(j5));
        break;
    }
  }

  private void set() {
    EditKmDialogListener listener = (EditKmDialogListener) getActivity();
    listener.onFinishEditDialog(toOneString());
    // Close the dialog and return back to the parent activity
    dismiss();
  }

  private String toOneString() {
    StringBuffer sb = new StringBuffer();
    sb.append(val0Tv.getText().toString());
    sb.append(val1Tv.getText().toString());
    sb.append(val2Tv.getText().toString());
    sb.append(val3Tv.getText().toString());
    sb.append(val4Tv.getText().toString());
    sb.append(val5Tv.getText().toString());
    return sb.toString();
  }

  // 1. Defines the listener interface with a method passing back data result.
  public interface EditKmDialogListener {
    void onFinishEditDialog(String inputText);
  }

  public void onResume() {
    super.onResume();

    Window window = getDialog().getWindow();
    Point size = new Point();

    Display display = window.getWindowManager().getDefaultDisplay();
    display.getSize(size);

    int width = size.x;

    window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
    window.setGravity(Gravity.CENTER);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()){
      @Override
      public void onBackPressed() {
        //do your stuff
      }
    };
  }

  //Removing the TitleBar from the Dialog
//  @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState) {
//    Dialog dialog = super.onCreateDialog(savedInstanceState);
//
//    // request a window without the title
//    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//    return dialog;
//  }

}
