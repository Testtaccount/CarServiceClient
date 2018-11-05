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

  @Query("SELECT * FROM oil WHERE `key`=:key")
  Oil get(String key);

  @Query("SELECT * FROM oil WHERE carKey=:carKey")
  LiveData<List<Oil>> getAllByCarKey(String carKey);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(Oil oil);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<Oil> oils);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(Oil oil);

  @Delete
  void delete(Oil oil);

  @Query("DELETE FROM oil WHERE `key`=:key")
  void delete(String key);

  @Query("DELETE FROM oil")
  void nukeTable();

  @Query("SELECT MAX(serviceDoneDate) FROM oil WHERE serviceCompanyId=:serviceCompanyId AND NOT serviceDoneDate=:doneDate")
  long getMinServiceDateForCar(String serviceCompanyId,long doneDate);

  @Query("SELECT serviceCompanyId FROM oil WHERE carKey=:carKey")
  List<String> getOilServices(String carKey);

//  @Query("SELECT serviceCompanyId FROM oil")
//  Set<String> getServiceDates();

  @Query("SELECT MAX(serviceDoneDate) FROM oil WHERE serviceCompanyId=:serviceId AND carKey=:carKey")
  long getMaxServiceDateByServiceIdAndCarKey(String serviceId,String carKey);


}
