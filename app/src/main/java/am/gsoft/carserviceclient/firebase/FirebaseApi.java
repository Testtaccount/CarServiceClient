package am.gsoft.carserviceclient.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FirebaseApi {

  private static final String TAG = FirebaseApi.class.getSimpleName();

  private static volatile FirebaseApi sInstance;

  public class Path {
    public static final String USERS = "users";
    public static final String SERVICE_DATE = "service_date";
    public static final String CARS = "cars";
    public static final String DELETED_CARS = "deleted_cars";
    public static final String CARS_PLATE_NUMBERS = "cars_plate_numbers";
    public static final String OILS = "oils";
    public static final String USER_SERVICE = "user_service";
    public static final String SERVICE_USER = "service_user";

  }


  public static FirebaseApi getInstance() {
    if (sInstance == null) {
      synchronized (TAG) {
        if (sInstance == null) {
          sInstance = new FirebaseApi();
        }
      }
    }
    return sInstance;
  }

  /**
   * firebase property
   */
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mFirebaseUser;
//  private FirebaseDatabase mFirebaseDb;
  private DatabaseReference databaseReference;
  private DatabaseReference userDatabaseReference;
  private DatabaseReference serviceDateReference;
  private DatabaseReference carDatabaseReference;
  private DatabaseReference oilDatabaseReference;
  private DatabaseReference userServiceDatabeseReferance;
  private DatabaseReference serviceUserDatabeseReferance;

  private FirebaseApi() {

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseUser = mFirebaseAuth.getCurrentUser();
//    mFirebaseDb = FirebaseDatabase.getInstance();
//    mFirebaseDb.setPersistenceEnabled(true);
    databaseReference = FirebaseDbUtil.getDatabase().getReference();
    userDatabaseReference = getChildDbReferance(Path.USERS);
    serviceDateReference = getChildDbReferance(Path.SERVICE_DATE);
    carDatabaseReference = getChildDbReferance(Path.CARS);
    oilDatabaseReference = getChildDbReferance(Path.OILS);
    userServiceDatabeseReferance = getChildDbReferance(Path.USER_SERVICE);
    serviceUserDatabeseReferance = getChildDbReferance(Path.SERVICE_USER);
//    storage = FirebaseStorage.getInstance();
//    bookStorageRef = storage.getReference().child(BOOKS);

  }

  private DatabaseReference getChildDbReferance(String tableId) {
    return databaseReference.child(tableId);
  }

  public FirebaseAuth getFirebaseAuth() {
    return mFirebaseAuth;
  }

  public FirebaseUser getFirebaseUser() {
    return  mFirebaseAuth.getCurrentUser();
  }

//  public FirebaseDatabase getFirebaseDb() {
//    return mFirebaseDb;
//  }

  public DatabaseReference getDatabaseReference() {
    return databaseReference;
  }

  public DatabaseReference getUserDatabaseReference() {
    return userDatabaseReference;
  }

  public DatabaseReference getServiceDateReference() {
    return serviceDateReference;
  }

  public DatabaseReference getCarDatabaseReference() {
//    carDatabaseReference.keepSynced(true);
    return carDatabaseReference;
  }

  public DatabaseReference getOilDatabaseReference() {
    oilDatabaseReference.keepSynced(true);
    return oilDatabaseReference;
  }

  public DatabaseReference getUserServiceDatabeseReferance() {
     return userServiceDatabeseReferance;
  }

  public DatabaseReference getServiceUserDatabeseReferance() {
     return serviceUserDatabeseReferance;
  }
}
