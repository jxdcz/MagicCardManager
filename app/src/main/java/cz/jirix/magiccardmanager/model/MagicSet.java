package cz.jirix.magiccardmanager.model;

import com.google.gson.annotations.SerializedName;

public class MagicSet {

    @SerializedName("code")
    private String mCode;
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;
    @SerializedName("border")
    private String mBorder;
    @SerializedName("mkm_id")
    private int mMkmId;
    @SerializedName("releaseDate")
    private String mReleaseDate;
    @SerializedName("block")
    private String mBlock;

    public MagicSet(String code, String name, String type, String border, int mkmId, String releaseDate, String block) {
        mCode = code;
        mName = name;
        mType = type;
        mBorder = border;
        mMkmId = mkmId;
        mReleaseDate = releaseDate;
        mBlock = block;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getBorder() {
        return mBorder;
    }

    public void setBorder(String border) {
        mBorder = border;
    }

    public int getMkmId() {
        return mMkmId;
    }

    public void setMkmId(int mkmId) {
        mMkmId = mkmId;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getBlock() {
        return mBlock;
    }

    public void setBlock(String block) {
        mBlock = block;
    }

    @Override
    public String toString() {
        return mCode +":" + mName + ":" + mType;
    }
}
