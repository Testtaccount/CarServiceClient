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

  @Query("SELECT * FROM car WHERE id=:id")
  Car get(long id);

  @Insert
  long insert(Car car);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<Car> cars);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(Car car);

  @Delete
  void delete(Car car);

  @Query("DELETE FROM car WHERE id=:id")
  void delete(long id);

  @Query("SELECT count(*) FROM car")
  int getCount();
}
