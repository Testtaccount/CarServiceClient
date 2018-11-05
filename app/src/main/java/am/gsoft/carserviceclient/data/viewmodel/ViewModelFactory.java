package am.gsoft.carserviceclient.data.viewmodel;

import am.gsoft.carserviceclient.data.AppRepository;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppRepository mRepository;
//    private final Application mApplication;

    public ViewModelFactory(AppRepository repository) {
        this.mRepository = repository;
//        this.mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LandingActivityViewModel.class)) {
            return (T) new LandingActivityViewModel(mRepository);
        } else if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(GarageActivityViewModel.class)) {
            return (T) new GarageActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(CreateCarActivityViewModel.class)) {
            return (T) new CreateCarActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(EditCarActivityViewModel.class)) {
            return (T) new EditCarActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(CreateNewOilActivityViewModel.class)) {
            return (T) new CreateNewOilActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(EditOilActivityViewModel.class)) {
            return (T) new EditOilActivityViewModel(mRepository);
        }else if (modelClass.isAssignableFrom(OilHistoryActivityViewModel.class)) {
            return (T) new OilHistoryActivityViewModel(mRepository);
        }

        throw new IllegalArgumentException(
                modelClass.getName() + " ViewModel is not supported.");

    }


}
