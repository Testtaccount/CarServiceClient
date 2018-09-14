/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package am.gsoft.carserviceclient.data.viewmodel;

import am.gsoft.carserviceclient.data.AppRepository;
import am.gsoft.carserviceclient.data.Resource;
import am.gsoft.carserviceclient.data.database.entity.Car;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class GarageActivityViewModel extends ViewModel {

    private final AppRepository mRepository;
    private LiveData<Resource<List<Car>>> mCars;


    public GarageActivityViewModel(AppRepository repository) {
        mRepository = repository;

    }

    public LiveData<Resource<List<Car>>> getCars() {
        if (mCars == null) {
            mCars = mRepository.loadCars();
        }
        return mCars;
    }

}
