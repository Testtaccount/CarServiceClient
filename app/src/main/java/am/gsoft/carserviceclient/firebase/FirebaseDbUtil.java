package am.gsoft.carserviceclient.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDbUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
//            mDatabase.getReference().keepSynced(true);
        }
        return mDatabase;
    }
}
