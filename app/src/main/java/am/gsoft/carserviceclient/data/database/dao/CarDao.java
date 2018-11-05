package am.gsoft.carserviceclient.data.database.dao;

import am.gsoft.carserviceclient.data.database.entity.Car;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface CarDao {

  @Query("SELECT * FROM car")
  LiveData<List<Car>> getAll();

  @Query("SELECT * FROM car WHERE `key`=:key")
  Car get(String key);

  @Query("SELECT * FROM car WHERE `key`=:key")
  LiveData<Car> getCar(String key);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(Car car);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<Car> cars);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(Car car);

  @Delete
  void delete(Car car);

  @Query("DELETE FROM car WHERE `key`=:key")
  void delete(String key);

  @Query("SELECT count(*) FROM car")
  int getCount();

  @Query("SELECT count(*) FROM car")
  LiveData<Integer> getCountL();


  @Query("SELECT `key` FROM car")
  List<String> getCarKeys();


  @Query("DELETE FROM car")
  void nukeTable();

  @Query("SELECT `key` FROM car WHERE NOT `key`=:key")
  List<String> getCarKeysWithoutCurrent(String key);
}
