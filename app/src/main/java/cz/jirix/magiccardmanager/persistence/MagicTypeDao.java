package cz.jirix.magiccardmanager.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicType;
import io.reactivex.Single;

@Dao
public interface MagicTypeDao {

    @Query("SELECT * FROM MagicType")
    List<MagicType> getAll();

    @Query("SELECT * FROM MagicType")
    Single<List<MagicType>> getAllRx();

    @Insert
    void insertAll(List<MagicType> types);

    @Delete
    void deleteSets(List<MagicType> types);

}
