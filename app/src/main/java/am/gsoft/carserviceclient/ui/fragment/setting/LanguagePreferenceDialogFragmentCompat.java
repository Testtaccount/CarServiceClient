package am.gsoft.carserviceclient.ui.fragment.setting;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.util.ToastUtils;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.TextView;

/**
 * The Dialog for the {@link LanguagePreference}.
 *
 * @author Jakob Ulbrich
 */
public class LanguagePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    /**
     * The TimePicker widget
     */
    private TextView mTimePicker;

    /**
     * Creates a new Instance of the TimePreferenceDialogFragment and stores the key of the
     * related Preference
     *
     * @param key The key of the related Preference
     * @return A new Instance of the TimePreferenceDialogFragment
     */
    public static LanguagePreferenceDialogFragmentCompat newInstance(String key) {
        final LanguagePreferenceDialogFragmentCompat
                fragment = new LanguagePreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = (TextView) view.findViewById(R.id.edit);

        // Exception: There is no TimePicker with the id 'edit' in the dialog.
//        if (mTimePicker == null) {
//            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'edit'");
//        }

        // Get the time from the related Preference
        String minutesAfterMidnight = null;
        DialogPreference preference = getPreference();
        if (preference instanceof LanguagePreference) {
            minutesAfterMidnight = ((LanguagePreference) preference).getTime();
        }

        // Set the time to the TimePicker
        ToastUtils.longToast(minutesAfterMidnight);
        mTimePicker.setText(minutesAfterMidnight);
    }

    /**
     * Called when the Dialog is closed.
     *
     * @param positiveResult Whether the Dialog was accepted or canceled.
     */
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            ToastUtils.longToast("positiveResult");
//            // Get the current values from the TimePicker
//            int hours;
//            int minutes;
//            if (Build.VERSION.SDK_INT >= 23) {
//                hours = mTimePicker.getHour();
//                minutes = mTimePicker.getMinute();
//            } else {
//                hours = mTimePicker.getCurrentHour();
//                minutes = mTimePicker.getCurrentMinute();
//            }
//
//            // Generate value to save
//            int minutesAfterMidnight = (hours * 60) + minutes;
//
//            // Save the value
//            DialogPreference preference = getPreference();
//            if (preference instanceof LanguagePreference) {
//                LanguagePreference timePreference = ((LanguagePreference) preference);
//                // This allows the client to ignore the user value.
//                if (timePreference.callChangeListener(minutesAfterMidnight)) {
//                    // Save the value
//                    timePreference.setTime(minutesAfterMidnight);
//                }
//            }
        }
    }
}
