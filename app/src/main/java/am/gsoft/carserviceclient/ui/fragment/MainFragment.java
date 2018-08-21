package am.gsoft.carserviceclient.ui.fragment;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.util.Constant;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MainFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private Bundle mArgumentData;
    private RecyclerView mRecyclerView;
    private TextView noDataTv;
//    private ArrayList<Car> listOfGeoPlaces;
    private FloatingActionButton addCarFab;


    private FirebaseUser firebaseUser;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public static MainFragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        findViews(view);
        setListeners();
        getData();
        customizeActionBar();
        checkIfEmpty();
//        listOfGeoPlaces = new ArrayList<>();

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new PlaceListAdapter(listOfGeoPlaces, this);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        mRecyclerView.setAdapter(mAdapter);
//
//        mAdapter.addGeofencePlace(new GeofencePlace(1,"aaa",44.11,44.33,12,1));
//        mAdapter.addGeofencePlace(new GeofencePlace(1,"bbb",44.11,44.33,12,1));



//        checkIfEmpty();

        return view;
    }

    private void checkIfEmpty() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fab_add_place:
//                mListener.addPlaceAction(mAdapter);
//
//                break;
        }
    }

    private void setListeners() {
//        addCarFab.setOnClickListener(this);
    }

    private void findViews(View view) {
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.places_list_recycler_view);
//        noDataTv = (TextView) view.findViewById(R.id.tv_no_data);
//        addCarFab=(FloatingActionButton)view.findViewById(R.id.fab_add_place);
    }

    private void getData() {
        if (getArguments() != null) {
            mArgumentData = getArguments().getBundle(Constant.Argument.ARGUMENT_DATA);
        }
    }

    private void customizeActionBar() {
    }

    public interface OnFragmentInteractionListener {

        void addPlaceAction();

        void deletePlaceAction(int placeId);

    }
}