package am.gsoft.carserviceclient.util.bridges;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public interface ActionBarBridge {

    void initActionBar();

    void showActionBar();

    void hideActionBar();

    void setActionBarTitle(String title);

    void setActionBarTitle(@StringRes int title);

    void setActionBarSubtitle(String subtitle);

    void setActionBarSubtitle(@StringRes int subtitle);

    void showActionBarTitle();

    void showActionBarSubTitle();

    void hideActionBarTitle();

    void hideActionBarSubTitle();

    void setActionBarIcon(Drawable icon);

    void setActionBarIcon(@DrawableRes int icon);

    void setActionBarUpButtonEnabled(boolean enabled);

    void setDisplayShowTitleEnabled(boolean enabled);

}