package am.gsoft.carserviceclient.data.database.dao;

import am.gsoft.carserviceclient.data.database.entity.Oil;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface OilDao {

  @Query("SELECT * FROM oil")
  LiveData<List<Oil>> getAll();

  @Query("SELECT * FROM oil WHERE id=:id")
  Oil get(long id);

  @Query("SELECT * FROM oil WHERE carId=:carId")
  LiveData<List<Oil>> getAllByCarId(long carId);

  @Insert
  long insert(Oil oil);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<Oil> oils);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(Oil oil);

  @Delete
  void delete(Oil oil);

  @Query("DELETE FROM oil WHERE id=:id")
  void delete(long id);
}
