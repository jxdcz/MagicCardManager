package cz.jirix.magiccardmanager.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MagicCard {

    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_MANA_COST = "manaCost";
    private static final String JSON_C_MANA_COST = "cmc";
    private static final String JSON_COLORS = "colors";
    private static final String JSON_TYPE = "type";
    private static final String JSON_RARITY = "rarity";
    private static final String JSON_SET = "set";
    private static final String JSON_TEXT = "text";
    private static final String JSON_ARTIST = "artist";
    private static final String JSON_POWER = "power";

    // would be much better with Kotlin data classes
    // yes, could use GSON, but I for JSON, I prefer the old school

    private String mId;
    private String mName;
    private String mManaCost;
    private int mConvertedManaCost;
    private List<String> mColors;
    private String mType;
    private String mRarity;
    private String mSetId;
    private String mText;
    private String mArtist;
    private int mPower;
    private int mToughness;
    private int mMultiverseId;
    private String mImageUrl;


    public MagicCard(String id, String name, String manaCost, int convertedManaCost, List<String> colors, String type, String rarity, String setId, String text, String artist, int power, int toughness, int multiverseId, String imageUrl) {
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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

    public int getConvertedManaCost() {
        return mConvertedManaCost;
    }

    public void setConvertedManaCost(int convertedManaCost) {
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

    public int getPower() {
        return mPower;
    }

    public void setPower(int power) {
        mPower = power;
    }

    public int getToughness() {
        return mToughness;
    }

    public void setToughness(int toughness) {
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
