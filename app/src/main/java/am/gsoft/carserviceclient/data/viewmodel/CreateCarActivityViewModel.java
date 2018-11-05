
package am.gsoft.carserviceclient.data.viewmodel;

import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.data.Resource;
import am.gsoft.carserviceclient.data.database.entity.Car;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class CreateCarActivityViewModel extends ViewModel {

  private final AppRepository mRepository;
  private LiveData<Resource<List<Car>>> mCars;

  public CreateCarActivityViewModel(AppRepository repository) {
    mRepository = repository;
  }

  public LiveData<Resource<List<Car>>> getCars() {
    if (mCars == null) {
      mCars = mRepository.loadCars();
    }
    return mCars;
  }


  public void saveCar(Car car) {
    mRepository.saveCar(car);
  }


  public LiveData<Integer> getCarsCont() {
    return mRepository.getCarsCount();
  }
}
