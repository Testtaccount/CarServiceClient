package am.gsoft.carserviceclient.ui.dialog;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.util.AppUtil;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditNotesDialogFragment extends DialogFragment implements View.OnClickListener, OnEditorActionListener {

  private EditText notesTextEt;
  private Button setBtn;
  private Button cancelBtn;
  private String initText;



  public EditNotesDialogFragment() {
    // Empty constructor is required for DialogFragment
    // Make sure not to add arguments to the constructor
    // Use `newInstance` instead as shown below
  }

  public static EditNotesDialogFragment newInstance(String title,String initText) {

    EditNotesDialogFragment frag = new EditNotesDialogFragment();

    //Set Arguments here if needed for dialog auto recreation on screen rotation
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("initText", initText);
    frag.setArguments(args);
//    frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//    frag.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    final Context contextThemeWrapper = new ContextThemeWrapper(getActivity().getApplicationContext(), R.style.DialogNoBorder);
//    LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
    View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.fragment_edit_notes, container,false);


    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    findViews(view);
    initFields();
    setListeners();
    AppUtil.showKeyboard(getActivity());
    return view;
  }


  private void  findViews(View view){
// Get field from view
    notesTextEt = (EditText) view.findViewById(R.id.et_notes_text);
    setBtn = (Button) view.findViewById(R.id.btn_set);
    cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

    // Fetch arguments from bundle and set title
    String title = getArguments().getString("title", "Notes");
//    getDialog().setTitle(title);

    initText = getArguments().getString("initText", "");
//    getDialog().setTitle(title);


  }

  private void initFields(){
    notesTextEt.setText(initText);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setListeners() {
    getDialog().setCanceledOnTouchOutside(false);
    notesTextEt.setOnEditorActionListener(this);

    // EditText scroll
    notesTextEt.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        // TODO Auto-generated method stub
        if (view.getId() == R.id.et_notes_text) {
          view.getParent().requestDisallowInterceptTouchEvent(true);
          switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
              view.getParent().requestDisallowInterceptTouchEvent(false);
              break;
          }
        }
        return false;
      }
    });

    setBtn.setOnClickListener(this);
    cancelBtn.setOnClickListener(this);
  }


  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (EditorInfo.IME_ACTION_DONE == actionId) {
      // Return input text back to activity through the implemented listener
      // Check if no view has focus:
      hideKayboarde();

      EditNotesDialogListener listener = (EditNotesDialogListener) getActivity();
      listener.onFinishEditDialog(true,notesTextEt.getText().toString());
      // Close the dialog and return back to the parent activity
      dismiss();
      return true;
    }
    return false;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_set:
        set();
        break;
      case R.id.btn_cancel:
        cancel();
        break;
      default:
    }
  }

  private void set() {
// Check if no view has focus:
    hideKayboarde();
    EditNotesDialogListener listener = (EditNotesDialogListener) getActivity();
    listener.onFinishEditDialog(true,notesTextEt.getText().toString());
    // Close the dialog and return back to the parent activity
    dismiss();
  }

  private void cancel() {
    hideKayboarde();
    EditNotesDialogListener listener = (EditNotesDialogListener) getActivity();
    listener.onFinishEditDialog(false,notesTextEt.getText().toString());
    // Close the dialog and return back to the parent activity
    dismiss();
  }

  private void hideKayboarde() {
    // Check if no view has focus:
    View view = getDialog().getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }


  public void onResume() {
    super.onResume();

    Window window = getDialog().getWindow();
    Point size = new Point();

    Display display = window.getWindowManager().getDefaultDisplay();
    display.getSize(size);

    int width = size.x;

//    window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
    window.setLayout(LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    window.setGravity(Gravity.CENTER);

  }

//  @Override
//  public void onResume() {
//    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//    params.width = LayoutParams.MATCH_PARENT;
//    params.height = LayoutParams.WRAP_CONTENT;
//    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//    super.onResume();
//  }





  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()){
      @Override
      public void onBackPressed() {
        //do your stuff
      }
    };
  }

  //  Removing the TitleBar from the Dialog
//  @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState) {
//    Dialog dialog = super.onCreateDialog(savedInstanceState);
//
//    // request a window without the title
//    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//
//    return dialog;
//  }

  // 1. Defines the listener interface with a method passing back data result.
  public interface EditNotesDialogListener {
    void onFinishEditDialog(boolean action,String inputText);
  }

//
//  @Override
//  public void onResume()
//  {
//    super.onResume();
//    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
//  }
//
//  @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState) {
//    final Dialog dialog = super.onCreateDialog(savedInstanceState);
//    dialog.requestWindowFeature(DialogFragment.STYLE_NO_TITLE);
//    View view = View.inflate(getActivity(), R.layout.fragment_edit_notes, null);
//    dialog.setContentView(view);
//    return dialog;
//  }

}
