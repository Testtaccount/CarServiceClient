package am.gsoft.carserviceclient.ui.activity.setting;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.ui.activity.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsActivity extends BaseActivity {//implements SharedPreferences.OnSharedPreferenceChangeListener {

//  SharedPreferences sharedPreferences;

  @Override
  protected int layoutResId() {
    return R.layout.activity_settings;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setupSharedPreferences();
    customizeActionBar();
  }

  private void customizeActionBar() {
    setActionBarTitle(R.string.action_settings);
    setActionBarUpButtonEnabled(true);
  }

  private void setupSharedPreferences() {
    // Get all of the values from shared preferences to set it up
//    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//      changeLanguages(sharedPreferences);
    // Register the listener
//    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
//        Intent startSettingsActivity = new Intent(this, MainActivity.class);
//        startActivity(startSettingsActivity);
//        finish();
//        //onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }


//  @Override
//  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//    if (key.equals(getString(R.string.pref_language_key))) {
//      changeLanguages(sharedPreferences);
//    }
//  }

//  private void changeLanguages(SharedPreferences sharedPreferences) {
//    String sLocale = sharedPreferences.getString(getString(R.string.pref_language_key),
//        getString(R.string.pref_language_en_value));
////    setLanguage(sLocale);
//    saveLanguage(getApplicationContext(), sLocale);
//
////    Locale myLocale = new Locale(sLocale);
////    Resources res = getResources();
////    DisplayMetrics dm = res.getDisplayMetrics();
////    Configuration conf = res.getConfiguration();
////    conf.locale = myLocale;
////    res.updateConfiguration(conf, dm);
////    onConfigurationChanged(conf);
//    recreate();
//  }
}
