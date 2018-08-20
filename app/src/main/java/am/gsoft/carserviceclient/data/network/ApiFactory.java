package am.gsoft.carserviceclient.data.network;


import am.gsoft.carserviceclient.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {


    public static class Url {
        static final String BASE_URL = "https://carserviceclient-18e43.firebaseio.com/";
    }
    private static AppNetworkService appNetworkService;


    public static AppNetworkService getAppNetworkService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(Level.BODY);
            httpClient.addInterceptor(logging);
        }
        OkHttpClient okHttpClient =  httpClient.build();
        if (appNetworkService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .client(okHttpClient)
                //.callbackExecutor(Executors.newFixedThreadPool(5))
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(JSONConverterFactory.create())
                .build();

            appNetworkService = retrofit.create(AppNetworkService.class);
        }
        return appNetworkService;
    }
}
