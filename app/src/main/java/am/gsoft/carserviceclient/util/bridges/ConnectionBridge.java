package am.gsoft.carserviceclient.util.bridges;

public interface ConnectionBridge {

    boolean checkNetworkAvailableWithError();

    boolean isNetworkAvailable();
}