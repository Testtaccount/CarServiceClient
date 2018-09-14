package am.gsoft.carserviceclient.ui.fragment.setting;

import am.gsoft.carserviceclient.R;
import android.content.SharedPreferences;
import android.os.Bundle;
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
