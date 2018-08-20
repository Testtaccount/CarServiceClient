package am.gsoft.carserviceclient.ui.activity.base;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.ui.fragment.dialogs.ProgressDialogFragment;
import am.gsoft.carserviceclient.util.NetworkUtil;
import am.gsoft.carserviceclient.util.ToastUtils;
import am.gsoft.carserviceclient.util.bridges.ActionBarBridge;
import am.gsoft.carserviceclient.util.bridges.ConnectionBridge;
import am.gsoft.carserviceclient.util.bridges.LoadingBridge;
import am.gsoft.carserviceclient.util.helpers.SharedHelper;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity implements ActionBarBridge,
    ConnectionBridge, LoadingBridge {

  private static final String TAG = BaseActivity.class.getSimpleName();

  protected App app;
  private boolean isUIDisabled;

  public Toolbar mToolbar;
  public Menu mMenu;

  private ViewGroup root;
  private ActionBar actionBar;
  private TextView mToolbarTitleTv;
  private TextView mToolbarSubTitleTv;




  protected abstract int layoutResId();
//  protected abstract int menuResId();


  protected SharedHelper appSharedHelper;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    root = (ViewGroup) getLayoutInflater().inflate(layoutResId(), null);
    setContentView(root);
    Log.d("BaseActivity", "onCreate");

    findViews();
    initFields();

    initActionBar();
  }

  private void findViews() {
    mToolbar = (Toolbar) findViewById(R.id.tb);
    if (mToolbar != null) {
      mToolbarTitleTv = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
      mToolbarSubTitleTv = (TextView) mToolbar.findViewById(R.id.tv_toolbar_subtitle);
    }
  }

  private void initFields() {
    Log.d("BaseActivity", "initFields()");
    app = App.getInstance();
    appSharedHelper = App.getAppSharedHelper();
  }

  @Override
  public void initActionBar() {
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle("");
      }
    }
  }

  @Override
  public void showActionBar() {
    if (actionBar != null) {
      actionBar.show();
    }
  }

  @Override
  public void hideActionBar() {
    if (actionBar != null) {
      actionBar.hide();
    }
  }

  @Override
  public void setActionBarTitle(String title) {
    if (actionBar != null) {
      mToolbarTitleTv.setText(title);
    }
  }

  @Override
  public void setActionBarTitle(@StringRes int title) {
    setActionBarTitle(getString(title));
  }

  @Override
  public void setActionBarSubtitle(String subtitle) {
    if (actionBar != null) {
      mToolbarSubTitleTv.setVisibility(View.VISIBLE);
      mToolbarSubTitleTv.setText(subtitle);
    }
  }

  @Override
  public void setActionBarSubtitle(int subtitle) {
    setActionBarSubtitle(getString(subtitle));
  }

  @Override
  public void showActionBarTitle() {
    mToolbarTitleTv.setVisibility(View.VISIBLE);
  }

  @Override
  public void showActionBarSubTitle() {
    mToolbarSubTitleTv.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideActionBarTitle() {
    mToolbarTitleTv.setVisibility(View.GONE);
  }

  @Override
  public void hideActionBarSubTitle() {
    mToolbarSubTitleTv.setVisibility(View.GONE);
  }

  @Override
  public void setActionBarIcon(Drawable icon) {
    if (actionBar != null) {
      // In appcompat v21 there will be no icon if we don't add this display option
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setIcon(icon);
    }
  }

  @Override
  public void setActionBarIcon(@DrawableRes int icon) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      drawable = getDrawable(icon);
    } else {
      drawable = getResources().getDrawable(icon);
    }

    setActionBarIcon(drawable);
  }

  @Override
  public void setActionBarUpButtonEnabled(boolean enabled) {
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(enabled);
      actionBar.setDisplayHomeAsUpEnabled(enabled);
    }
  }

  @Override
  public void setDisplayShowTitleEnabled(boolean enabled) {
    if (actionBar != null) {
      actionBar.setDisplayShowTitleEnabled(enabled);
    }
  }

  public void showMenuOption(int id) {
    if(mMenu!=null){
      MenuItem item = mMenu.findItem(id);
      if(item!=null) {
        item.setVisible(true);
      }
    }
  }

  public void hideMenuOption(int id) {
    if(mMenu!=null){
      MenuItem item = mMenu.findItem(id);
      if(item!=null) {
        item.setVisible(false);
      }
    }
  }

  public Toolbar getToolBar() {
    return mToolbar;
  }


//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    if (menuResId() != 0) {
//      getMenuInflater().inflate(menuResId(), menu);
//      mMenu = menu;
//    }
////    hideMenuOption(R.id.action_done);
//    return super.onCreateOptionsMenu(menu);
//  }

//  @Nullable
//  public final Menu getMenu() {
//    return mMenu;
//  }


  @Override
  public synchronized void showProgress() {
    ProgressDialogFragment.show(getSupportFragmentManager());
  }

  @Override
  public synchronized void hideProgress() {
    ProgressDialogFragment.hide(getSupportFragmentManager());
  }


  private void blockUI(boolean stopUserInteractions) {
    if (isUIDisabled == stopUserInteractions) {
      return;
    }
    isUIDisabled = stopUserInteractions;
    disableEnableControls(!stopUserInteractions, root);
  }

  private void disableEnableControls(boolean enable, ViewGroup vg) {
    if (vg instanceof Toolbar) {
      return;
    }

    for (int i = 0; i < vg.getChildCount(); i++) {
      View child = vg.getChildAt(i);
      child.setEnabled(enable);
      if (child instanceof ViewGroup) {
        disableEnableControls(enable, (ViewGroup) child);
      }
    }
  }

  @Override
  public boolean checkNetworkAvailableWithError() {
    if (!isNetworkAvailable()) {
      ToastUtils.longToast(R.string.dlg_fail_connection);
      return false;
    } else {
      return true;
    }
  }

  @Override
  public boolean isNetworkAvailable() {
    return NetworkUtil.isNetworkAvailable(this);
  }

  protected void focusOnView(NestedScrollView ns, View v) {
    //2
    ns.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            ns.scrollTo(0, v.getBottom());
          }
        });

    //0
//        ns.post(new Runnable() {
//            @Override
//            public void run() {
//                ns.scrollTo(0, v.getBottom());
//            }
//        });

    //1
//        ns.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ns.post(new Runnable() {
//                    public void run() {
//                        ns.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
//            }
//        });
  }

}
