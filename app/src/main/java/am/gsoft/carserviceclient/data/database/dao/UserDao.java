package am.gsoft.carserviceclient.data.database.dao;

import am.gsoft.carserviceclient.data.database.entity.User;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface UserDao {

  @Query("SELECT * FROM user WHERE `key`=:key")
  User get(String key);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(User user);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(User user);

  @Delete
  void delete(User user);

  @Query("DELETE FROM user WHERE `key`=:key")
  void delete(String key);
}
