package cz.jirix.magiccardmanager.provider;

import android.arch.persistence.room.Room;
import android.content.Context;

import cz.jirix.magiccardmanager.persistence.MagicDatabase;

public class DbProvider {

    private static final String DB_NAME = "magicDb";

    private static DbProvider sInstance;

    private MagicDatabase mMagicDatabase;

    private DbProvider(){
    }

    public static DbProvider get(){
        if(sInstance == null){
            sInstance = new DbProvider();
        }
        return sInstance;
    }

    public void init(Context context){
        mMagicDatabase = Room.databaseBuilder(context.getApplicationContext(), MagicDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public MagicDatabase getDatabase(){
        return mMagicDatabase;
    }

}
