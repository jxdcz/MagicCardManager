package cz.jirix.magiccardmanager.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.model.MagicType;

@Database(entities = {MagicCard.class, MagicSet.class, MagicType.class}, version = 3)
@TypeConverters(Converters.class)
public abstract class MagicDatabase extends RoomDatabase {
    public abstract MagicCardDao magicCardDao();
    public abstract MagicSetDao magicSetDao();
    public abstract MagicTypeDao magicTypeDao();


}
