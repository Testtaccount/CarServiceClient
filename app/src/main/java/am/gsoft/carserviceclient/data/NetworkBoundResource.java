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
package am.gsoft.carserviceclient.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;

public abstract class NetworkBoundResource<ResultType, RequestType> {

  private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

  protected abstract Class<RequestType> getClazz();


  @MainThread
  NetworkBoundResource() {
    result.setValue(Resource.loading(null));
    LiveData<ResultType> dbSource = loadFromDb();
    result.addSource(dbSource, data -> {
      result.removeSource(dbSource);
      if (shouldFetch(data)) {
        fetchFromNetwork(dbSource);
      } else {
        result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
      }
    });
  }

  @NonNull
  @MainThread
  protected abstract LiveData<ResultType> loadFromDb();

  @NonNull
  @MainThread
  protected abstract Call<HashMap<String, RequestType>> createCall();

  protected abstract Query getDatabaseReference();

  @WorkerThread
  protected abstract void saveCallResult(@NonNull List<RequestType> item);

  private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
    result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
    //Firebase
    getDatabaseReference().addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            List<RequestType> list = new ArrayList<>();
            if (dataSnapshot.exists()) {
              for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                list.add(snapshot.getValue(getClazz()));
              }
            }

            result.removeSource(dbSource);
            saveResultAndReInit(list);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            onFetchFailed();
            result.removeSource(dbSource);
            result.addSource(dbSource,
                newData -> result.setValue(Resource.error(databaseError.getMessage(), newData)));
          }
        });

    //Retrofit
//        createCall().enqueue(new Callback<HashMap<String, RequestType>>() {
//            @Override
//            public void onResponse(Call<HashMap<String, RequestType>> call, Response<HashMap<String, RequestType>> response) {
//                result.removeSource(dbSource);
//
//                saveResultAndReInit(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<HashMap<String, RequestType>> call, Throwable t) {
//                onFetchFailed();
//                result.removeSource(dbSource);
//                result.addSource(dbSource, newData -> result.setValue(Resource.error(t.getMessage(), newData)));
//            }
//        });
  }

  @MainThread
  private void saveResultAndReInit(List<RequestType> response) {
    new AsyncTask<Void, Void, Void>() {

      @Override
      protected Void doInBackground(Void... voids) {
        saveCallResult(response);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        result.addSource(loadFromDb(), newData -> result.setValue(Resource.success(newData)));
      }
    }.execute();
  }

  @MainThread
  protected abstract boolean shouldFetch(@Nullable ResultType data);

  @MainThread
  protected void onFetchFailed() {
  }

  public final LiveData<Resource<ResultType>> getAsLiveData() {
    return result;
  }
}
