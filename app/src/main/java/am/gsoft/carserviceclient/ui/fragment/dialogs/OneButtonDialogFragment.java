package am.gsoft.carserviceclient.ui.fragment.dialogs;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.afollestad.materialdialogs.MaterialDialog;

public class OneButtonDialogFragment extends DialogFragment {

    private static final String TAG = OneButtonDialogFragment.class.getSimpleName();
    private static final String ARG_CONTENT = "content";
    private static final String ARG_CANCELABLE = "cancelable";

    private String content;
    private MaterialDialog.ButtonCallback callback;

    public static void show(FragmentManager fm, String content, boolean cancelable, MaterialDialog.ButtonCallback callback) {
        OneButtonDialogFragment oneButtonDialogFragment = new OneButtonDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_CONTENT, content);
        args.putBoolean(ARG_CANCELABLE, cancelable);
        oneButtonDialogFragment.setArguments(args);
        oneButtonDialogFragment.setCallbacks(callback);

        oneButtonDialogFragment.show(fm, TAG);
    }

    public static void show(FragmentManager fm, int content, boolean cancelable, MaterialDialog.ButtonCallback callback) {
        show(fm, App.getInstance().getString(content), cancelable, callback);
    }

    public static void show(FragmentManager fm, int content, boolean cancelable) {
        show(fm, App.getInstance().getString(content), cancelable, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        content = getArguments().getString(ARG_CONTENT);
        return createDialog();
    }

    private MaterialDialog createDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .positiveText(R.string.dlg_ok)
                .callback(callback);

        if (!TextUtils.isEmpty(content)) {
            builder.content(content);
        }

        MaterialDialog materialDialog = builder.build();

        if (!getArguments().getBoolean(ARG_CANCELABLE)) {

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            materialDialog.setOnKeyListener(keyListener);

            materialDialog.setCanceledOnTouchOutside(false);

        }

        return materialDialog;
    }

    public void setCallbacks(MaterialDialog.ButtonCallback callback) {
        this.callback = callback;
    }
}