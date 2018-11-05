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
import am.gsoft.carserviceclient.data.database.entity.Oil;
import android.arch.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;


public class EditOilActivityViewModel extends ViewModel {

  private final AppRepository mRepository;
  private Map<String, String> ph;

  public EditOilActivityViewModel(AppRepository repository) {
    mRepository = repository;
  }

  public void editOil(Oil editedOil, String serviceCompanyId) {
    mRepository.editOil(editedOil,serviceCompanyId);
    updatePO(editedOil);
  }

  private void updatePO(Oil editedOil) {
    Map<String, String> map = getPO();
    map.put(editedOil.getBrand(), editedOil.getServiceCompanyId());
    mRepository.saveOilPOMap((HashMap<String, String>) map);
  }


  public HashMap<String, String> getPO() {
    return mRepository.getOilPOMap()==null?new HashMap<>():mRepository.getOilPOMap();
  }


}
