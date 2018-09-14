package am.gsoft.carserviceclient.ui.dialog;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.ui.adapter.BrandsAdapter;
import am.gsoft.carserviceclient.ui.adapter.BrandsSpinnerItem;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import java.util.ArrayList;

public class CarBrandsDialogFragment extends DialogFragment implements BrandsAdapter.OnBrandItemClickListener {

  SearchView sv;
  EditText searchEditText;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager layoutManager;;
  private BrandsAdapter carBrandsDialogAdapter;
  private ArrayList<BrandsSpinnerItem> items;

  public CarBrandsDialogFragment() {
    // Empty constructor is required for DialogFragment
    // Make sure not to add arguments to the constructor
    // Use `newInstance` instead as shown below
  }

  public static CarBrandsDialogFragment newInstance(ArrayList<BrandsSpinnerItem> items) {
    CarBrandsDialogFragment frag = new CarBrandsDialogFragment();
    Bundle args = new Bundle();
    args.putSerializable("BrandsSpinnerItems",  items);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.fragment_car_brands_dialog, container,false);
    getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    findViews(view);
    initFields();
    setListeners();

    return view;
  }



  private void  findViews(View view){
// Get field from view

    // Fetch arguments from bundle and set title
    String title = getArguments().getString("title", getResources().getString(R.string.selectOne));
    getDialog().setTitle(title);

    sv =(SearchView) view.findViewById(R.id.search_brand);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.car_brands_recycler_view);

    int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
    TextView textView = (TextView) sv.findViewById(id);
    textView.setTextColor(Color.BLACK);
//    searchEditText.setTextColor(getResources().getColor(R.color.BLACK));
//    searchEditText.setHintTextColor(getResources().getColor(R.color.white));
    }

  private void initFields(){
//    getDialog().setCanceledOnTouchOutside(true);
    items = new ArrayList<>();
    items = (ArrayList<BrandsSpinnerItem>) getArguments().getSerializable("BrandsSpinnerItems");

    initRecyclerView();
  }

  private void setListeners() {
    //SEARCH

    sv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sv.setIconified(false);
      }
    });
    sv.setQueryHint("Search..");
    sv.setOnQueryTextListener(new OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String txt) {
        // TODO Auto-generated method stub
//        ToastUtils.shortToast(txt.toString());
        return false;
      }

      @Override
      public boolean onQueryTextChange(String txt) {
        // TODO Auto-generated method stub

//        carBrandsDialogAdapter.filter(txt);
        carBrandsDialogAdapter.getFilter().filter(txt);
        return false;
      }
    });
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  private void initRecyclerView() {
    // Set up the recycler view
    layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setHasFixedSize(true);
    carBrandsDialogAdapter = new BrandsAdapter(items, this);
//    carBrandsDialogAdapter.setHasStableIds(true);
    mRecyclerView.setAdapter(carBrandsDialogAdapter);
  }

  private void set(int i) {
    BrandsDialogListener listener = (BrandsDialogListener) getActivity();
    listener.onBrandSelected(i);
    // Close the dialog and return back to the parent activity
    dismiss();
  }

  public void onResume() {
    super.onResume();

    Window window = getDialog().getWindow();
    Point size = new Point();

    Display display = window.getWindowManager().getDefaultDisplay();
    display.getSize(size);

    int width = size.x;
    int height = size.y;

    window.setLayout((int) (width * 0.90),(int) (height * 0.90));
    window.setGravity(Gravity.CENTER);
  }

  private void hideKayboarde() {
    // Check if no view has focus:
    View view = getDialog().getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()){
      @Override
      public void onBackPressed() {
        //do your stuff
        dismiss();
      }
    };
  }

  @Override
  public void onBrandItemClick(BrandsSpinnerItem brandsSpinnerItem) {
//    ToastUtils.shortToast(brandsSpinnerItem.getCarBrand());
    set(items.indexOf(brandsSpinnerItem));
  }

  // 1. Defines the listener interface with a method passing back data result.
  public interface BrandsDialogListener {
    void onBrandSelected(int i);
  }


}
