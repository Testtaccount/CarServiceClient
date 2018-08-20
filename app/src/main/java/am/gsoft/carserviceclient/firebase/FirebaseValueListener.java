package am.gsoft.carserviceclient.firebase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Super on 5/10/2017.
 */

public class FirebaseValueListener {
    private Query query;
    private ValueEventListener listener;

    public FirebaseValueListener() {

    }

    public FirebaseValueListener(Query query, ValueEventListener listener) {
        this.query = query;
        this.listener = listener;
    }

    public void removeListener() {
        query.removeEventListener(listener);
    }

}
