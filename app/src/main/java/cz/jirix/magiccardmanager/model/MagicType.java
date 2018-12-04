package cz.jirix.magiccardmanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MagicType {

    @PrimaryKey
    @NonNull
    private String mType;

    public MagicType(@NonNull String type) {
        mType = type;
    }

    @NonNull
    public String getType() {
        return mType;
    }

    public void setType(@NonNull String type) {
        mType = type;
    }

    @Override
    public String toString() {
        return mType;
    }
}
