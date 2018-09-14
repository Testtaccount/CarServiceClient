package am.gsoft.carserviceclient.ui.activity.main;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.database.AppDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

  private AppDatabase mAppDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

    mAppDatabase=AppDatabase.getInstance(getApplicationContext());
//    Car car1=new Car(1,
//        "YwCS7MYdvwR2eXmCihCxcWjb2Xm1_1534755322982",
//        2131230873,
//        -256,
//        "Aixam",
//        "model",
//        "1888",
//        "5656d e",
//        "rr",
//        "Km");
//    Car car2=new Car(2,
//        "YwCS7MYdvwR2eXmCihCxcWjb2Xm1_1534948658677",
//        2131230873,
//        -256,
//        "Blsjkf",
//        "model",
//        "1888",
//        "5656d e",
//        "rr",
//        "Km");
//    List<Car> cars=new ArrayList<>();
//    cars.add(car1);
//    cars.add(car2);
//    mAppDatabase.mCarDao().insertAll(cars);
//
//    Car c=mAppDatabase.mCarDao().get(1);
//    Log.d("testt",c.toString());

//    Oil o1=new Oil(9,
//        "YwCS7MYdvwR2eXmCihCxcWjb2Xm1_1534948658677");
  }
}
