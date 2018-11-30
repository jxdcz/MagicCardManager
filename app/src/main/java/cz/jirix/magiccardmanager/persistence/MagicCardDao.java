package cz.jirix.magiccardmanager.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicCard;

@Dao
public interface MagicCardDao {

    @Query("SELECT * FROM MagicCard")
    List<MagicCard> getAll();

}
