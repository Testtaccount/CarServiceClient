package am.gsoft.carserviceclient.ui.activity;

import static am.gsoft.carserviceclient.util.Constant.Action.ACTION_OIL_HISTORY_ACTIVITY_INTENT;
import static am.gsoft.carserviceclient.util.Constant.Extra.EXTRA_CURRENT_OIL_LIST;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.InjectorUtils;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.viewmodel.OilHistoryActivityViewModel;
import am.gsoft.carserviceclient.data.viewmodel.ViewModelFactory;
import am.gsoft.carserviceclient.ui.activity.main.MainActivity;
import am.gsoft.carserviceclient.ui.fragment.OilHistoryDetailFragment;
import am.gsoft.carserviceclient.ui.fragment.OilHistoryListFragment;
import am.gsoft.carserviceclient.util.manager.DialogManager;
import am.gsoft.carserviceclient.util.manager.FragmentTransactionManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class OilHistoryActivity extends BaseActivity implements View.OnClickListener,
    OilHistoryListFragment.OnOilHistoryFragmentInteractionListener,
    OilHistoryDetailFragment.OnOilHistoryDetailsFragmentInteractionListener,
    AppBarLayout.OnOffsetChangedListener {

  private static final String TAG = OilHistoryActivity.class.getSimpleName();

  private AppBarLayout appBarLayout;
  private FloatingActionButton sortOilFab;

  private boolean showSortItem;
  private boolean appBarIsExpanded = true;

  private ArrayList<Oil> oils;
  private String carKey;
  private OilHistoryActivityViewModel mViewModel;

  @Override
  protected int layoutResId() {
    return R.layout.activity_oil_history;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(OilHistoryActivityViewModel.class);
    findViews();
    customizeActionBar();
    getIntentData();
    initFields();
    setListeners();

    openScreen(
        OilHistoryListFragment.newInstance( ),
        false);
  }

  private void findViews() {
    appBarLayout = findViewById(R.id.appBar);
    sortOilFab = (FloatingActionButton) findViewById(R.id.fab_sort_oil);
  }

  private void customizeActionBar() {
//    initActionBar();
//    showActionBarIcon();
    appBarLayout.setExpanded(false);
    setActionBarUpButtonEnabled(true);
//    setActionBarTitle("Oil history");
  }

  private void getIntentData() {
//    if (getIntent().getExtras() != null) {
//      oils = getIntent().getParcelableArrayListExtra(EXTRA_CURRENT_OIL_LIST);
      carKey = getIntent().getStringExtra(EXTRA_CURRENT_OIL_LIST);
      mViewModel.selectCar(carKey);
//    } else {
//      oils = new ArrayList<>();
//    }
  }

  private void initFields() {
//    oils = new ArrayList<>();
//    for (int i = 0; i < 65; i++) {
//      Oil spinnerItem = createNewOil(i);
//      oils.add(spinnerItem);
//    }
    oils = new ArrayList<>();

    showSortItem = true;

  }

  private void setListeners() {
    sortOilFab.setOnClickListener(this);

    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      boolean isShow = false;
      int scrollRange = -1;

      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
          scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
          if (showSortItem) {
            isShow = true;

            showMenuOption(R.id.action_sort);
            findViewById(R.id.v_shadow).setVisibility(View.INVISIBLE);
            if (appBarIsExpanded) {
              sortOilFab.setVisibility(View.VISIBLE);
            }

          }

        } else if (isShow) {
          isShow = false;
          hideMenuOption(R.id.action_sort);
          findViewById(R.id.v_shadow).setVisibility(View.VISIBLE);
        }
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DialogManager.getInstance().dismissPreloader(this.getClass());
  }

  @Override
  public void onResume() {
    super.onResume();
    appBarLayout.addOnOffsetChangedListener(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    appBarLayout.removeOnOffsetChangedListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_sort, menu);
    mMenu = menu;
    hideMenuOption(R.id.action_sort);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.action_sort:
        OilHistoryListFragment fragment = (OilHistoryListFragment) getSupportFragmentManager().findFragmentByTag(OilHistoryListFragment.TAG);
        if (fragment != null) {
          fragment.sortData();
         }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

//  public void hideMenuOption(int id) {
//    MenuItem item = menu.findItem(id);
//    item.setVisible(false);
//  }
//
//  private void showMenuOption(int id) {
//    MenuItem item = menu.findItem(id);
//    item.setVisible(true);
//  }

  @Override
  public void setShowSortItem(boolean b) {
    showSortItem = false;
  }

  @Override
  public void onBackPressed() {

    int count = getSupportFragmentManager().getBackStackEntryCount();

    if (count == 0) {
      Intent intent = new Intent(OilHistoryActivity.this, MainActivity.class);
      intent.setAction(ACTION_OIL_HISTORY_ACTIVITY_INTENT);
      startActivity(intent);
      finish();
//    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
      overridePendingTransition(R.anim.up_slide_enter, R.anim.down_slide_exit);

      super.onBackPressed();
    } else {
      getSupportFragmentManager().popBackStack();
      showSortItem = true;
//      if(appBarIsExpanded) {
      sortOilFab.setVisibility(View.VISIBLE);
//      }

    }


  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab_sort_oil:
        OilHistoryListFragment fragment = (OilHistoryListFragment) getSupportFragmentManager()
            .findFragmentByTag(OilHistoryListFragment.TAG);
        if (fragment != null) {
          fragment.sortData();
         }
        break;
      default:
    }
  }

  private void openScreen(Fragment fragment, boolean mustAddToBackStack) {
    FragmentTransactionManager.displayFragment(
        getSupportFragmentManager(),
        fragment,
        R.id.fl_main_container,
        mustAddToBackStack

    );
  }

  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    appBarIsExpanded = (verticalOffset == 0);
  }

  @Override
  public void onOilHistoryItemClickListener(Oil oil) {
    openScreen(
        OilHistoryDetailFragment.newInstance(oil), true);

//    ToastUtils.shortToast(spinnerItem.getBrand());

  }

}
