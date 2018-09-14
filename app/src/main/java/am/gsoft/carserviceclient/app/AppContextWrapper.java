package am.gsoft.carserviceclient.app;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.util.VersionUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

public class AppContextWrapper extends ContextWrapper {
    private static final String CAR_SERVICE_CLIENT_SHARED =  "car_service_client_shared";
    private static final String LANGUAGE =  "language";

    public AppContextWrapper(Context base) {
        super(base);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (VersionUtils.isAfter24()) {
            configuration.setLocale(newLocale);

            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);

            context = context.createConfigurationContext(configuration);

        } else if (VersionUtils.isAfter17()) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }

    public static String getLanguage(Context context){
        SharedPreferences preferences = context.getSharedPreferences(CAR_SERVICE_CLIENT_SHARED, MODE_PRIVATE);
        String lan = preferences.getString(LANGUAGE, null);
        if(lan==null){
            String[] languages = context.getResources().getStringArray(R.array.languages);
            lan = Locale.getDefault().getLanguage();
            for(String el:languages){
                if(lan.equals(el))
                    return el;
            }
            return context.getString(R.string.en);
        }else{
            return lan;
        }
    }

    public static boolean isChangeCurrentLocale(Context context,String lan){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return !context.getResources().getConfiguration().getLocales().toLanguageTags().equals(lan);
        }else{
            return !context.getResources().getConfiguration().locale.toString().equals(lan);
        }
    }

    public static void saveLanguage(Context context, String languages){
        SharedPreferences preferences = context.getSharedPreferences(CAR_SERVICE_CLIENT_SHARED, MODE_PRIVATE);
        SharedPreferences.Editor edit= preferences.edit();
        edit.putString(LANGUAGE, languages);
        edit.apply();
    }

}
