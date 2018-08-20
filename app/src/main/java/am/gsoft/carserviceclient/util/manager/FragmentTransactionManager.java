package am.gsoft.carserviceclient.util.manager;

import am.gsoft.carserviceclient.R;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentTransactionManager {

    public static void displayFragment(FragmentManager fragmentManager, Fragment fragment,
                                       @IdRes int view, boolean addToBackStack) {
        if (addToBackStack) {
                   fragmentManager.beginTransaction()
                    .addToBackStack(null)
//                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_righ)
                    .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_righ)
                .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
