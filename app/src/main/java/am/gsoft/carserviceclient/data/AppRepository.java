package am.gsoft.carserviceclient.data;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.app.AppExecutors;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.data.database.entity.User;
import am.gsoft.carserviceclient.data.network.AppNetworkService;
import am.gsoft.carserviceclient.firebase.FirebaseApi;
import am.gsoft.carserviceclient.firebase.FirebaseApi.Path;
import am.gsoft.carserviceclient.notification.NotificationsRepository;
import am.gsoft.carserviceclient.util.IdGenerator;
import am.gsoft.carserviceclient.util.ToastUtils;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AppRepository {

  private static final String TAG = AppRepository.class.getSimpleName();

  private static final Object LOCK = new Object();

  private static AppRepository sInstance;
  private final AppDatabase mAppDatabase;
  private final AppNetworkService mAppNetworkService;
  private final AppExecutors mExecutors;
  private final FirebaseApi mFirebaseApi;
  private User mUser;
  private boolean mInitialized = false;

  private AppRepository(AppDatabase appDatabase, AppNetworkService appNetworkService,
      AppExecutors executors, FirebaseApi firebaseApi) {
    mAppDatabase = appDatabase;
    mAppNetworkService = appNetworkService;
    mExecutors = executors;
    mFirebaseApi = firebaseApi;
   loadUser();

    // As long as the repository exists, observe the network LiveData.
    // If that LiveData changes, update the appDatabase.
//    LiveData<WeatherEntry[]> networkData = mWeatherNetworkDataSource.getCurrentWeatherForecasts();
//    networkData.observeForever(new Observer<WeatherEntry[]>() {
//      @Override
//      public void onChanged(@Nullable WeatherEntry[] newForecastsFromNetwork) {
//        mExecutors.diskIO().execute(() -> {
//          // Deletes old historical data
//          AppRepository.this.deleteOldData();
//          Log.d(LOG_TAG, "Old weather deleted");
//          // Insert our new weather data into Sunshine's appDatabase
//          mAppDatabase.bulkInsert(newForecastsFromNetwork);
//          Log.d(LOG_TAG, "New values inserted");
//        });
//      }
//    });
  }

  public synchronized static AppRepository getInstance(AppDatabase appDatabase,
      AppNetworkService appNetworkService, AppExecutors executors, FirebaseApi firebaseApi) {
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = new AppRepository(appDatabase, appNetworkService, executors, firebaseApi);
      }
    }
    return sInstance;
  }

  ///////////////
  public void saveUser(User user, ResultListener<User> userResultListenerListener) {
    mFirebaseApi.getUserDatabaseReference().child(user.getKey())
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            mUser = user;
            mExecutors.diskIO().execute(new Runnable() {
              @Override
              public void run() {
                mAppDatabase.mUserDao().insert(user);
//        appSharedHelper.saveUser(user);
                userResultListenerListener.onLoad(user);
              }
            });

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            userResultListenerListener.onFail(databaseError.toException().toString());
          }

        });

    mFirebaseApi.getUserDatabaseReference().child(user.getKey()).setValue(user);
  }

  public  void loadUser(){
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        if (App.getInstance().isSimulator) {
          mUser = mAppDatabase.mUserDao().get("YwCS7MYdvwR2eXmCihCxcWjb2Xm1");
        } else {
          if (mFirebaseApi.getFirebaseUser() != null) {
            mUser = mAppDatabase.mUserDao().get(mFirebaseApi.getFirebaseUser().getUid());
          }
        }
      }
    });

  }

  ///////////////
  public void getCars(LifecycleOwner owner, ResultListener<List<Car>> resultListener) {
    LiveData<List<Car>> listLiveData = mAppDatabase.mCarDao().getAll();
    listLiveData.observe(owner, new Observer<List<Car>>() {
      @Override
      public void onChanged(@Nullable List<Car> cars) {
//        listLiveData.removeObserver(this);

        if (cars != null && cars.size() > 0) {
          listLiveData.removeObserver(this);
          resultListener.onLoad(cars);

        } else {
          //Firebase
          mFirebaseApi.getCarDatabaseReference().child(mUser.getKey())
              .addListenerForSingleValueEvent(
                  new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      List<Car> cars1 = new ArrayList<>();
                      if (dataSnapshot.exists()) {
                        for (DataSnapshot carSnapshot : dataSnapshot.getChildren()) {
                          String key = carSnapshot.getKey();
                          Car car = carSnapshot.getValue(Car.class);
                          if (car != null) {
                            car.setKey(key);
                            cars1.add(car);
                          }
                        }
                      }

                      mExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                          mAppDatabase.mCarDao().insertAll(cars1);
                          if (cars1.size() == 0) {
                            resultListener.onLoad(cars1);
                          }
                        }
                      });

//                  getCarsResultListener.onSuccess(userCarList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                      resultListener.onFail(null);
                      ToastUtils.shortToast("onFail getUserCarList!!");
                    }
                  });
          //Retrofit
//          mAppNetworkService.getCarList(mUser.getKey())
//              .enqueue(new Callback<HashMap<String, Car>>() {
//                @Override
//                public void onResponse(
//                    Call<HashMap<String, Car>> call, Response<HashMap<String, Car>> response) {
//                  if (response.code() == 200) {
//                    Log.v(TAG, "Success" + call.toString());
//                  } else {
//                    Log.v(TAG, "feild" + call.toString());
//                  }
//                  HashMap<String, Car> hashMap = response.body();
//
//                  if (hashMap != null) {
//                    List<Car> cars1 = new ArrayList<Car>(hashMap.values());
//
//                    mExecutors.diskIO().execute(new Runnable() {
//                      @Override
//                      public void run() {
//                        mAppDatabase.mCarDao().insertAll(cars1);
////                        resultListener.onCarLoadComplated(cars1);
//                      }
//                    });
////                       database.mCarDao().insertAll(list);
//
//                  } else {
//                    resultListener.onCarLoadComplated(null);
//                  }
//
//                }
//
//                @Override
//                public void onFailure(Call<HashMap<String, Car>> call, Throwable t) {
//                  Log.v(TAG, "onFailure");
//                  resultListener.onCarLoadComplated(null);
//                }
//              });

        }
      }
    });
  }

  public void saveCar(Car car, ResultListener<Car> carResultListenerListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        long id = mAppDatabase.mCarDao().insert(car);
        if (id != -1) {
          car.setId(id);
          car.setKey(mUser.getKey() + "_" + IdGenerator.getId());
          App.getAppSharedHelper().saveSpinnerPosition(car.getId());

          //Firebase
          mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
              .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                  mFirebaseApi.getDatabaseReference().child(Path.CARS_PLATE_NUMBERS)
                      .child(car.getNumbers()).setValue(car.getKey());

                  mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                      mAppDatabase.mCarDao().update(car);
                      carResultListenerListener.onLoad(car);
                    }
                  });

//                  userCarList.add(car);
//                  appSharedHelper.saveSpinnerPosition(getCarPosition(car));
//                  appSharedHelper.setCarListToSave(userCarList);
//                  notifyCarFound(car, carResultListenerListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  carResultListenerListener.onLoad(null);
                }

              });

          mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
              .setValue(car);

          //Retrofit
//          mAppNetworkService.saveCar(mUser.getKey(), car.getKey(), car)
//              .enqueue(new Callback<Car>() {
//                @Override
//                public void onResponse(Call<Car> call, Response<Car> response) {
//                  if (response.code() == 200) {
//                    Log.v(TAG, "Success" + call.toString());
//                  } else {
//                    Log.v(TAG, "feild" + call.toString());
//                  }
//                  Car car1 = response.body();
//
//                  if (car1 != null) {
//                    mExecutors.diskIO().execute(new Runnable() {
//                      @Override
//                      public void run() {
//                        mAppDatabase.mCarDao().update(car);
//                        carResultListenerListener.onLoad(car);
//                      }
//                    });
//                  } else {
//                    carResultListenerListener.onLoad(null);
//                  }
//                }
//
//                @Override
//                public void onFailure(Call<Car> call, Throwable t) {
//                  carResultListenerListener.onFail(null);
//                }
//              });
        }
      }
    });

  }

  public void editCar(Car car, ResultListener<Car> carResultListenerListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        int id = mAppDatabase.mCarDao().update(car);
        if (id != -1) {
//          car.setId(id);
//          car.setKey(mUser.getKey() + "_" + id);
          //Firebase
          //Firebase
          mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
              .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  carResultListenerListener.onLoad(car);

//           // List<Car> list = appSharedHelper.getCarListToSave();
//                  userCarList.set((int) appSharedHelper.getSpinnerPosition(), editedCar);
//                  appSharedHelper.setCarListToSave(userCarList);
//
//                  notifyCarFound(editedCar, carResultListenerListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  carResultListenerListener.onLoad(null);
                }

              });

          mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
              .setValue(car);

          //Retrofit
//          mAppNetworkService.updateCar(mUser.getKey(), car.getKey(), car)
//              .enqueue(new Callback<Car>() {
//                @Override
//                public void onResponse(Call<Car> call, Response<Car> response) {
//                  if (response.code() == 200) {
//                    Log.v(TAG, "Success" + call.toString());
//                  } else {
//                    Log.v(TAG, "feild" + call.toString());
//                  }
//                  Car car1 = response.body();
//
//                  if (car1 != null) {
//                    carResultListenerListener.onLoad(car);
//                  } else {
//                    carResultListenerListener.onLoad(null);
//                  }
//                }
//
//                @Override
//                public void onFailure(Call<Car> call, Throwable t) {
//                  carResultListenerListener.onFail(null);
//                }
//              });
        }
      }
    });


  }

  public void deleteCar(Car car, ResultListener<Car> carResultListenerListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        mAppDatabase.mCarDao().delete(car);

//          car.setId(id);
//          car.setKey(mUser.getKey() + "_" + id);
        //Firebase

        mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
            .addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                mFirebaseApi.getDatabaseReference().child(Path.DELETED_CARS).child(car.getKey())
                    .setValue(car);

                //stay list in Firebase for car (not delete)
//                deleteOils(car.getKey(),carResultListenerListener);
                carResultListenerListener.onLoad(car);

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                carResultListenerListener.onFail(null);
              }

            });

        mFirebaseApi.getCarDatabaseReference().child(mUser.getKey()).child(car.getKey())
            .removeValue();

        //Retrofit
//        mAppNetworkService.deleteCar(mUser.getKey(), car.getKey()).enqueue(new Callback<Car>() {
//          @Override
//          public void onResponse(Call<Car> call, Response<Car> response) {
//            if (response.code() == 200) {
//              Log.v(TAG, "Success" + call.toString());
//              deleteOils(car.getKey(),carResultListenerListener);
//            } else {
//              Log.v(TAG, "feild" + call.toString());
//              carResultListenerListener.onLoad(null);
//            }
//          }
//
//          @Override
//          public void onFailure(Call<Car> call, Throwable t) {
//            carResultListenerListener.onFail(null);
//          }
//        });

      }
    });

  }
  //////////////

  //////////////
  public void getOils(Car c, LifecycleOwner owner, ResultListener<List<Oil>> resultListener) {
    LiveData<List<Oil>> listLiveData = mAppDatabase.mOilDao().getAllByCarId(c.getId());
    listLiveData.observe(owner, new Observer<List<Oil>>() {
      @Override
      public void onChanged(@Nullable List<Oil> oils) {
        if (oils != null && oils.size() > 0) {
          listLiveData.removeObserver(this);
          resultListener.onLoad(oils);
        } else {
          //Firebase
          mFirebaseApi.getOilDatabaseReference().child(c.getKey()).addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  List<Oil> oils1 = new ArrayList<>();
                  if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                      String kay = userSnapshot.getKey();
                      Oil oil = userSnapshot.getValue(Oil.class);
                      if (oil != null) {
                        oil.setKey(kay);
                        oils1.add(oil);
                      }
                    }
                  }
                  mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                      mAppDatabase.mOilDao().insertAll(oils1);
                      if (oils1.size() == 0) {
                        resultListener.onLoad(oils1);
                      }
                    }
                  });
//                  getCarOilsResultListener.onSuccess(carOilList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  resultListener.onFail(null);
                  ToastUtils.shortToast("onFail !!");
                }
              });

          //Retrofit
//          mAppNetworkService.getCarOils(c.getKey())
//              .enqueue(new Callback<HashMap<String, Oil>>() {
//                @Override
//                public void onResponse(Call<HashMap<String, Oil>> call,
//                    Response<HashMap<String, Oil>> response) {
//                  if (response.code() == 200) {
//                    Log.v(TAG, "Success" + call.toString());
//                  } else {
//                    Log.v(TAG, "feild" + call.toString());
//                  }
//                  HashMap<String, Oil> hashMap = response.body();
//                  if (hashMap != null) {
//                    List<Oil> oils1 = new ArrayList<Oil>(hashMap.values());
//
//                    mExecutors.diskIO().execute(new Runnable() {
//                      @Override
//                      public void run() {
//                        mAppDatabase.mOilDao().insertAll(oils1);
//                      }
//                    });
//
//                  } else {
//                    resultListener.onOilLoadComplated(c, null);
//                  }
//                }
//
//                @Override
//                public void onFailure(Call<HashMap<String, Oil>> call, Throwable t) {
//                  resultListener.onOilLoadComplated(c, null);
//
//                }
//              });
        }
      }
    });
  }

  public void saveOil(String carKey, Oil oil, ResultListener<Oil> resultListenerListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        long id = mAppDatabase.mOilDao().insert(oil);
        if (id != -1) {
          oil.setId(id);
          oil.setKey(carKey + "_" + IdGenerator.getId());

          //Firebase
          mFirebaseApi.getOilDatabaseReference().child(carKey)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                      mAppDatabase.mOilDao().update(oil);
                      resultListenerListener.onLoad(oil);
                    }
                  });
//              List<Oil> list = appSharedHelper.getSaveOilListByCarKey(carKey);// == null ? new ArrayList<Oil>() : appSharedHelper.getSaveOilListByCarKey(carId);// getAppNotification anel
//              list.add(oil);
//              appSharedHelper.setSaveOilListByCarKey(carKey,list);
//              resultListenerListener.onSuccess(oil);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  resultListenerListener.onFail(null);
                }

              });

          mFirebaseApi.getOilDatabaseReference().child(carKey).child(oil.getKey()).setValue(oil);

          //Retrofit
//          mAppNetworkService.saveOil(carKey, oil.getKey(), oil).enqueue(new Callback<Oil>() {
//            @Override
//            public void onResponse(Call<Oil> call, Response<Oil> response) {
//              if (response.code() == 200) {
//                Log.v(TAG, "Success" + call.toString());
//              } else {
//                Log.v(TAG, "feild" + call.toString());
//              }
//              Oil car1 = response.body();
//
//              if (car1 != null) {
//                mExecutors.diskIO().execute(new Runnable() {
//                  @Override
//                  public void run() {
//                    mAppDatabase.mOilDao().update(oil);
//                    resultListenerListener.onComplate(oil);
//                  }
//                });
//              } else {
//                resultListenerListener.onComplate(null);
//              }
//            }
//
//            @Override
//            public void onFailure(Call<Oil> call, Throwable t) {
//              resultListenerListener.onComplate(null);
//            }
//          });
        }
      }
    });

  }

  public void editOil(String carKey, Oil oil, ResultListener<Oil> oilResultListenerListener) {
    mExecutors.diskIO().execute(new Runnable() {
      @Override
      public void run() {
        int id = mAppDatabase.mOilDao().update(oil);
        if (id != -1) {
//          car.setId(id);
//          car.setKey(mUser.getKey() + "_" + id);

          //Firebase
          mFirebaseApi.getOilDatabaseReference().child(carKey)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  oilResultListenerListener.onLoad(oil);

//              List<Oil> list = appSharedHelper.getSaveOilListByCarKey(carKey);
//
//              list.set(indexOfCurrentOil,oil);
//              appSharedHelper.setSaveOilListByCarKey(carKey,list);
//              carOilResultListener.onSuccess(oil);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  oilResultListenerListener.onFail(null);
//              carOilResultListener.onFail(databaseError.toException());
                }

              });

          mFirebaseApi.getOilDatabaseReference().child(carKey).child(oil.getKey()).setValue(oil);

          //Retrofit
//          mAppNetworkService.updateOil(carKey, oil.getKey(), oil)
//              .enqueue(new Callback<Oil>() {
//                @Override
//                public void onResponse(Call<Oil> call, Response<Oil> response) {
//                  if (response.code() == 200) {
//                    Log.v(TAG, "Success" + call.toString());
//                  } else {
//                    Log.v(TAG, "feild" + call.toString());
//                  }
//                  Oil oil1 = response.body();
//
//                  if (oil1 != null) {
//                    oilResultListenerListener.onComplate(oil1);
//                  } else {
//                    oilResultListenerListener.onComplate(null);
//                  }
//                }
//
//                @Override
//                public void onFailure(Call<Oil> call, Throwable t) {
//                  oilResultListenerListener.onComplate(null);
//                }
//              });
        }
      }
    });

  }

  public void deleteOils(String carKey, ResultListener<Car> carResultListenerListener) {

    //Retrofit
//    mExecutors.networkIO().execute(new Runnable() {
//      @Override
//      public void run() {
//        mAppNetworkService.deleteOils(carKey) .enqueue(new Callback<Oil>() {
//          @Override
//          public void onResponse(Call<Oil> call, Response<Oil> response) {
//            if (response.code() == 200) {
//              Log.v(TAG, "Success" + call.toString());
//              carResultListenerListener.onLoad(null);
//            } else {
//              Log.v(TAG, "feild" + call.toString());
//            }
//          }
//
//          @Override
//          public void onFailure(Call<Oil> call, Throwable t) {
//            Log.v(TAG, "feild" + call.toString());
//          }
//        });
//
//      }
//    });

  }

  //////////////////

  public void removeDataAndNotifications(List<Car> carList) {

    NotificationsRepository notificationsRepository = InjectorUtils.provideNotificationRepository(App.getInstance());
    for(Car c:carList){
      mExecutors.diskIO().execute(new Runnable() {
        @Override
        public void run() {
          notificationsRepository.deleteAllNotificationsByCarId(c.getId());
          mAppDatabase.mCarDao().delete(c);
        }
      });
    }

  }
}
