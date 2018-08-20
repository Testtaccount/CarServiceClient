package am.gsoft.carserviceclient.util;

public abstract class IdGenerator {

  public static long getId(){
    return System.currentTimeMillis();

  }


//  public static int getNextNotifId() {
//    int id = App.getAppSharedHelper().getIntId() + 1;
//    if (id == Integer.MAX_VALUE) {
//      id = 0;
//    }
//    App.getAppSharedHelper().saveIntId(id);
//    Logger.d("testt","getNextNotifId: " + id);
//    return id;
//  }


}