package cz.jirix.magiccardmanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class MagicCard {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("manaCost")
    private String mManaCost;

    @SerializedName("cmc")
    private float mConvertedManaCost;

    @SerializedName("colors")
    private List<String> mColors;

    @SerializedName("type")
    private String mType;

    @SerializedName("rarity")
    private String mRarity;

    @SerializedName("set")
    private String mSetId;

    @SerializedName("text")
    private String mText;

    @SerializedName("artist")
    private String mArtist;

    @SerializedName("power")
    private String mPower;

    @SerializedName("toughness")
    private String mToughness;

    @SerializedName("multiverseid")
    private int mMultiverseId;

    @SerializedName("imageUrl")
    private String mImageUrl;


    @Ignore
    public MagicCard(@NonNull String id){
        mId = id;
    }

    public MagicCard(@NonNull String id, String name, String manaCost, float convertedManaCost, List<String> colors, String type, String rarity, String setId, String text, String artist, String power, String toughness, int multiverseId, String imageUrl) {
        mId = id;
        mName = name;
        mManaCost = manaCost;
        mConvertedManaCost = convertedManaCost;
        mColors = colors;
        mType = type;
        mRarity = rarity;
        mSetId = setId;
        mText = text;
        mArtist = artist;
        mPower = power;
        mToughness = toughness;
        mMultiverseId = multiverseId;
        mImageUrl = imageUrl;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getManaCost() {
        return mManaCost;
    }

    public void setManaCost(String manaCost) {
        mManaCost = manaCost;
    }

    public float getConvertedManaCost() {
        return mConvertedManaCost;
    }

    public void setConvertedManaCost(float convertedManaCost) {
        mConvertedManaCost = convertedManaCost;
    }

    public List<String> getColors() {
        return mColors;
    }

    public void setColors(List<String> colors) {
        mColors = colors;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getRarity() {
        return mRarity;
    }

    public void setRarity(String rarity) {
        mRarity = rarity;
    }

    public String getSetId() {
        return mSetId;
    }

    public void setSetId(String setId) {
        mSetId = setId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getPower() {
        return mPower;
    }

    public void setPower(String power) {
        mPower = power;
    }

    public String getToughness() {
        return mToughness;
    }

    public void setToughness(String toughness) {
        mToughness = toughness;
    }

    public int getMultiverseId() {
        return mMultiverseId;
    }

    public void setMultiverseId(int multiverseId) {
        mMultiverseId = multiverseId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

}
