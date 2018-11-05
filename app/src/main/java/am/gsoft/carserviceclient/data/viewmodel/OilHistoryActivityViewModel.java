package am.gsoft.carserviceclient.data.viewmodel;

import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class OilHistoryActivityViewModel extends ViewModel {

    private final AppRepository mRepository;
    private final MutableLiveData<String> selectedCar = new MutableLiveData<String>();


    public OilHistoryActivityViewModel(AppRepository repository) {
        mRepository = repository;

    }


    public void selectCar(String key) {
        selectedCar.setValue(key);
    }


    public LiveData<List<Oil>> getCarOils() {
        return Transformations
            .switchMap(selectedCar, new Function<String, LiveData<List<Oil>>>() {
                @Override
                public LiveData<List<Oil>> apply(String key) {
                    return mRepository.getOils(key);
                }
            });
    }
}
