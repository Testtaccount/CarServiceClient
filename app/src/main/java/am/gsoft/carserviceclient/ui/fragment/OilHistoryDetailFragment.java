package am.gsoft.carserviceclient.ui.fragment;

import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;
import static am.gsoft.carserviceclient.util.DateUtils.longToString;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.util.Constant.Argument;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OilHistoryDetailFragment extends BaseFragment implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = OilHistoryDetailFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
//    private AppBarLayout appBarLayout;
//    private NestedScrollView nestedScrollView;
    private TextView oilCompanyIdTv;
    private TextView oilServiveDoneDateTv;
    private TextView oilServiveNextDateTv;
    private TextView oilBrandTv;
    private TextView oilTypeTv;
    private TextView oilVolumeTv;
    private TextView serviceDoneKmTv;
    private TextView nextServiceKmTv;
    private TextView recomendedKmTv;
    private TextView middleMonthKmTv;
    private LinearLayoutManager layoutManager;;
    private Oil oil;
    private Menu menu;
    private FloatingActionButton sortOilFab;
    private OnOilHistoryDetailsFragmentInteractionListener mListener;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static OilHistoryDetailFragment newInstance() {
        return new OilHistoryDetailFragment();
    }

    public static OilHistoryDetailFragment newInstance(Oil oil) {
        Bundle args = new Bundle();
        args.putParcelable(Argument.ARGUMENT_OIL_HISTORY, oil);
        OilHistoryDetailFragment fragment = new OilHistoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oil_history_details, container, false);
        findViews(view);
        setListeners();
        getData();
        initFields();
        customizeActionBar();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_sort_oil:

                break;
            default:
        }
    }






    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {

    }

    private void findViews(View view) {
//        appBarLayout=getActivity().findViewById(R.id.appBar);
//        nestedScrollView = view.findViewById(R.id.nscw);
        oilCompanyIdTv = (TextView) view.findViewById(R.id.tv_oil_company_id);
        oilServiveDoneDateTv = (TextView) view.findViewById(R.id.tv_oil_service_done_date);
        oilServiveNextDateTv = (TextView) view.findViewById(R.id.tv_oil_service_next_date);
        oilBrandTv = (TextView) view.findViewById(R.id.tv_oil_brand);
        oilTypeTv = (TextView) view.findViewById(R.id.tv_oil_type);
        oilVolumeTv = (TextView) view.findViewById(R.id.et_oil_volume);
        serviceDoneKmTv = (TextView) view.findViewById(R.id.tv_service_done_km);
        nextServiceKmTv = (TextView) view.findViewById(R.id.tv_next_service_km);
        recomendedKmTv = (TextView) view.findViewById(R.id.tv_recomended_km);
        middleMonthKmTv = (TextView) view.findViewById(R.id.tv_middle_month_km);
        sortOilFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_sort_oil);    }

    private void initFields() {
        setOilToUi(oil);
        sortOilFab.setVisibility(View.GONE);
    }


    public void getData() {
        if (getArguments() != null) {
            oil = getArguments().getParcelable(Argument.ARGUMENT_OIL_HISTORY);
        }
    }

    private void customizeActionBar() {

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        this.menu = menu;
//        inflater.inflate(R.menu.menu_sort, menu);
//        hideMenuOption(R.id.action_sort);
//        super.onCreateOptionsMenu(menu,inflater);
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_sort, menu);
//        mListener.hideMenuOption(R.id.action_sort);
//        super.onCreateOptionsMenu(menu,inflater);

        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
        mListener.setShowSortItem(false);
    }


    private void setOilToUi(Oil uiOil) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                oilBrandTv.setText(uiOil.getBrand());
                oilTypeTv.setText(uiOil.getType());
                oilVolumeTv.setText(String.valueOf(uiOil.getVolume()));
                serviceDoneKmTv.setText(String.valueOf(uiOil.getServiceDoneKm()));
                nextServiceKmTv.setText(String.valueOf(uiOil.getServiceNextKm()));
                recomendedKmTv.setText(String.valueOf(uiOil.getRecomendedKm()));
                middleMonthKmTv.setText(String.valueOf(uiOil.getMiddleMonthKm()));
                oilServiveDoneDateTv.setText(uiOil.getServiceDoneDate() == 0 ? "-"
                    : longToString(uiOil.getServiceDoneDate(), getDateFormat(DateType.DMY)));
                oilServiveNextDateTv.setText(uiOil.getServiceNextDate() == 0 ? "-"
                    : longToString(uiOil.getServiceNextDate(), getDateFormat(DateType.DMY)));

                oilCompanyIdTv.setText(String.format("(%s) %s", uiOil.getServiceCompanyId().substring(0, 3),
                            uiOil.getServiceCompanyId().substring(3,uiOil.getServiceCompanyId().length())));



            }
        }, 0);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOilHistoryDetailsFragmentInteractionListener) {
            mListener = (OnOilHistoryDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnOilHistoryDetailsFragmentInteractionListener");
        }
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.action_sort);
//        item.setVisible(false);
//    }

    public interface OnOilHistoryDetailsFragmentInteractionListener {

        void onOilHistoryItemClickListener(Oil oil);

        void setShowSortItem(boolean b);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}