package cz.jirix.magiccardmanager.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicSet;
import io.reactivex.Single;

@Dao
public interface MagicSetDao {

    @Query("SELECT * FROM MagicSet")
    List<MagicSet> getAll();

    @Query("SELECT * FROM MagicSet")
    Single<List<MagicSet>> getAllRx();

    @Insert
    void insertAll(List<MagicSet> sets);

    @Delete
    void deleteSets(List<MagicSet> sets);

    @Query("SELECT * FROM MAGICSET WHERE mName = :name")
    MagicSet getByName(String name);
}
