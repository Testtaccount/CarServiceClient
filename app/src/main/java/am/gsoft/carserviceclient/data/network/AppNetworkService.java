package am.gsoft.carserviceclient.data.network;

import am.gsoft.carserviceclient.data.database.entity.Car;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AppNetworkService {

  @GET("cars/{userKey}.json")
  Call<HashMap<String, Car>> getCarList(
      @Path("userKey") String userKey);

  @PUT("/cars/{userKey}/{carKey}.json")
  Call<Car> saveCar(
      // title configured as identifier for tasks
      @Path("userKey") String userKey,
      @Path("carKey") String carKey,
      @Body Car car);

  @PATCH("/cars/{userKey}/{carKey}.json")
  Call<Car> updateCar(
      @Path("userKey") String userKey,
      @Path("carKey") String carKey,
      @Body Car car);


  @DELETE("/cars/{userKey}/{carKey}.json")
  Call<Car> deleteCar(
      @Path("userKey") String userKey,
      @Path("carKey") String carKey);


  @PUT("/oils/{carKey}/{oilKey}.json")
  Call<Oil> saveOil(
      // title configured as identifier for tasks
      @Path("carKey") String carKey,
      @Path("oilKey") String oilKey,
      @Body Oil oil);


  @GET("oils/{carKey}.json")
  Call<HashMap<String, Oil>> getCarOils(
      @Path("carKey") String userKey);

  @PATCH("/oils/{carKey}/{oilKey}.json")
  Call<Oil> updateOil(
      @Path("carKey") String carKey,
      @Path("oilKey") String oilKey,
      @Body Oil oil);

  @DELETE("/oils/{carKey}.json")
  Call<Oil> deleteOils(
      @Path("carKey") String carKey);
}
