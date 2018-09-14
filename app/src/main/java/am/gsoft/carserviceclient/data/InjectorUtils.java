package am.gsoft.carserviceclient.data;

import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.network.ApiFactory;
import am.gsoft.carserviceclient.data.network.AppNetworkService;
import am.gsoft.carserviceclient.data.viewmodel.ViewModelFactory;
import am.gsoft.carserviceclient.firebase.FirebaseApi;
import am.gsoft.carserviceclient.notification.NotificationsController;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.util.helpers.SharedHelper;
import android.content.Context;

public class InjectorUtils {

    public static AppRepository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        AppNetworkService appNetworkService =  ApiFactory.getAppNetworkService();
        FirebaseApi firebaseApi=FirebaseApi.getInstance();
        SharedHelper appSharedHelper=SharedHelper.getInstance();
        return AppRepository.getInstance(database, appNetworkService, executors,firebaseApi,appSharedHelper);
    }

    public static NotificationsRepository provideNotificationRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NotificationsController notificationsController=new NotificationsController(context);
        return NotificationsRepository.getInstance(database, executors,notificationsController);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new ViewModelFactory(repository);
    }
}