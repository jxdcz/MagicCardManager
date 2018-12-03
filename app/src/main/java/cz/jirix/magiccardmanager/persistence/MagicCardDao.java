package cz.jirix.magiccardmanager.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicCard;
import io.reactivex.Single;

@Dao
public interface MagicCardDao {

    @Query("SELECT * FROM MagicCard")
    List<MagicCard> getAll();

    @Query("SELECT * FROM MagicCard")
    Single<List<MagicCard>> getAllRx();

    @Query("SELECT * FROM MagicCard WHERE mSetId = :setId AND mName LIKE '%' || :name || '%' AND mColors LIKE '%' || :color || '%' AND mType = :type")
    Single<List<MagicCard>> getAllBy(String name, String setId, String color, String type);

    @Query("SELECT * FROM MagicCard WHERE mSetId = :setId AND mType = :type AND mName LIKE '%' || :name || '%'")
    List<MagicCard> getAllBy(String name, String setId, String type);

    @Query("SELECT * FROM MagicCard WHERE mSetId = :setId")
    List<MagicCard> getAllBy(String setId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MagicCard> cards);

    @Delete
    void deleteCards(List<MagicCard> cards);

}
