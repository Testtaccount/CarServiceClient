package am.gsoft.carserviceclient.ui.fragment.setting;

import am.gsoft.carserviceclient.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements
    SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle bundle, String s) {
    // Add 'general' preferences, defined in the XML file
    addPreferencesFromResource(R.xml.pref_general);

//     Preference statisticsDeletePref = findPreference(getString(R.string.pref_language_key));
//    statisticsDeletePref.setOnPreferenceClickListener(new  Preference.OnPreferenceClickListener() {
//      @Override
//      public boolean onPreferenceClick(Preference preference) {
//        ToastUtils.longToast("s");
//        return false;
//      }
//    });

    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    PreferenceScreen prefScreen = getPreferenceScreen();
    int count = prefScreen.getPreferenceCount();

    for (int i = 0; i < count; i++) {
      Preference preference = prefScreen.getPreference(i);
      if (!(preference instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(preference.getKey(), "");
        setSummary(preference, value);
      }
    }

  }

  @Override
  public void onDisplayPreferenceDialog(Preference preference) {

    // Try if the preference is one of our custom Preferences
    DialogFragment dialogFragment = null;
    if (preference instanceof LanguagePreference) {
      // Create a new instance of TimePreferenceDialogFragment with the key of the related
      // Preference
      dialogFragment = LanguagePreferenceDialogFragmentCompat.newInstance(preference.getKey());
    }


    if (dialogFragment != null) {
      // The dialog was created (it was one of our custom Preferences), show the dialog for it
      dialogFragment.setTargetFragment(this, 0);
      dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference" +
          ".PreferenceFragment.DIALOG");
    } else {
      // Dialog creation could not be handled here. Try with the super method.
      super.onDisplayPreferenceDialog(preference);
    }

  }

  @Override
  public void onStop() {
    super.onStop();
    // unregister the preference change listener
    getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // register the preference change listener
    getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference preference = findPreference(key);
    String value = sharedPreferences.getString(key, "");
    setSummary(preference, value);
  }

  private void setSummary(Preference preference, String value) {
    if (preference instanceof ListPreference) {
      ListPreference listPreference = (ListPreference) preference;
      int index = listPreference.findIndexOfValue(value);
      listPreference.setSummary(listPreference.getEntries()[index]);
    }
  }

}
