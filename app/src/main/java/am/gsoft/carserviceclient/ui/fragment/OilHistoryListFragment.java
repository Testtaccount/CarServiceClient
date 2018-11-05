package am.gsoft.carserviceclient.ui.fragment;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.viewmodel.OilHistoryActivityViewModel;
import am.gsoft.carserviceclient.ui.adapter.OilHistoryAdapter;
import am.gsoft.carserviceclient.util.Constant;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OilHistoryListFragment extends BaseFragment implements View.OnClickListener,OilHistoryAdapter.OnOilHistoryItemClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final String TAG = OilHistoryListFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private RelativeLayout progressUsersRl;
    private NestedScrollView nestedScrollView;
    private RecyclerView mRecyclerView;
    private OilHistoryAdapter mAdapter;
    private LinearLayoutManager layoutManager;;
//    private ArrayList<Oil> oils;
    private OilHistoryActivityViewModel mViewModel;
     private OnOilHistoryFragmentInteractionListener mListener;

    public static OilHistoryListFragment newInstance() {
        return new OilHistoryListFragment();
    }

    public static OilHistoryListFragment newInstance(ArrayList<Oil> oils) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.Argument.ARGUMENT_OIL_HISTORY_LIST, oils);
        OilHistoryListFragment fragment = new OilHistoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(getActivity()).get(OilHistoryActivityViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oil_history_list, container, false);
        findViews(view);
        initFields();
        setListeners();
        getData();
        initRecyclerView();
        customizeActionBar();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getActivity().getApplicationContext());
//    mViewModel = ViewModelProviders.of(getActivity(), factory).getProductById(MainActivityViewModel.class);
        mViewModel.getCarOils().observe(this, new Observer<List<Oil>>() {
            @Override
            public void onChanged(@Nullable List<Oil> oils) {

                if (oils != null) {
                    Collections.sort(oils, new Comparator<Oil>() {
                      @Override
                      public int compare(Oil o1, Oil o2) {
                        return Long.compare(o2.getServiceDoneDate(),o1.getServiceDoneDate());
                      }
                    }) ;
                }

                mAdapter.setOilList(oils);
//                if (mPosition == RecyclerView.NO_POSITION)
//                    mPosition = 0;
//                mRecyclerView.smoothScrollToPosition(mPosition);

                // Show the weather list or the loading screen based on whether the forecast data exists
                // and is loaded


                if (oils != null && oils.size() > 0) {
                    OilHistoryListFragment.this.showOilsDataView();
                } else {
                    OilHistoryListFragment.this.showLoading();
                }
            }
        });



//        mAppRepository.getalloils().observe(UsersListFragment.this, new Observer<List<Oil>>() {
//            @Override
//            public void onChanged(@Nullable List<Oil> oils) {
//                Collections.sort(oils, new Comparator<Oil>() {
//                    @Override
//                    public int compare(Oil o1, Oil o2) {
//                        return Long.compare(o1.getServiceDoneDate(),o2.getServiceDoneDate());
//                    }
//                });
//            }
//        });
//
//
//        if (oils != null) {
//            Collections.sort(oils, new Comparator<Oil>() {
//                public int compare(Oil left, Oil right) {
//                    return Integer.compare(oils.indexOf(left), oils.indexOf(right));
//                }
//            });
//        }
    }

    private void showOilsDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        progressUsersRl.setVisibility(View.GONE);
     }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressUsersRl.setVisibility(View.VISIBLE);
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
        progressUsersRl = view.findViewById(R.id.rl_progress_users);
        nestedScrollView = view.findViewById(R.id.nscw);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.oil_history_recycler_view);
    }

    private void initFields() {

    }

    private void initRecyclerView() {
        // Set up the recycler view
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        Collections.reverse(oils);
//        Collections.sort(oils, new Comparator<Oil>() {
//          @Override
//          public int compare(Oil o1, Oil o2) {
//            return Long.compare(o2.getServiceDoneDate(),o1.getServiceDoneDate());
//          }
//        });
        mAdapter = new OilHistoryAdapter(this);
//        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getData() {
        if (getArguments() != null) {
//            oils = getArguments().getParcelableArrayList(Argument.ARGUMENT_OIL_HISTORY_LIST);
        }
    }

    private void customizeActionBar() {

    }

    public void sortData() {

        //SORT ARRAY ASCENDING AND DESCENDING
//        if (asc) {
//            Collections.sort(oils, new Comparator<Oil>() {
//                @Override
//                public int compare(Oil o1, Oil o2) {
//                    return Long.compare(o1.getServiceDoneDate(), o1.getServiceDoneDate());
//                }
//            });
//            mAdapter.setCounterNumber(1);
//        } else {
//            Collections.reverse(oils);
//            mAdapter.setCounterNumber(oils.size());
//
//        }
//        mAdapter.notifyDataSetChanged();

      mAdapter.sort( );
      focusOnView(nestedScrollView,mRecyclerView);
      runLayoutAnimation(mRecyclerView, R.anim.layout_animation_from_bottom);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final int item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
            AnimationUtils.loadLayoutAnimation(context, item);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    protected final void focusOnView(NestedScrollView ns, View v){
        ns.post(new Runnable() {
            @Override
            public void run() {
                ns.scrollTo(0, v.getTop());
            }
        });
    }

    @Override
    public void onOilHistoryItemClick(Oil oil) {
        mListener.onOilHistoryItemClickListener(oil);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOilHistoryFragmentInteractionListener) {
            mListener = (OnOilHistoryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnOilHistoryFragmentInteractionListener");
        }
    }

    public interface OnOilHistoryFragmentInteractionListener {

        void onOilHistoryItemClickListener(Oil oil);

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}