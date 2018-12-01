package cz.jirix.magiccardmanager.model;

public class CardSearchCriteria {

    private String mCardName;
    private String mSetName;
    private String mColor;
    private int mPowerMin;
    private int mPowerMax;
    private int mToughnessMin;
    private int mToughnessMax;
    private String mType;

    public CardSearchCriteria() {
        // defaults to no criteria
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        mCardName = cardName;
    }

    public String getSetName() {
        return mSetName;
    }

    public void setSetName(String setName) {
        mSetName = setName;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public int getPowerMin() {
        return mPowerMin;
    }

    public void setPowerMin(int powerMin) {
        mPowerMin = powerMin;
    }

    public int getPowerMax() {
        return mPowerMax;
    }

    public void setPowerMax(int powerMax) {
        mPowerMax = powerMax;
    }

    public int getToughnessMin() {
        return mToughnessMin;
    }

    public void setToughnessMin(int toughnessMin) {
        mToughnessMin = toughnessMin;
    }

    public int getToughnessMax() {
        return mToughnessMax;
    }

    public void setToughnessMax(int toughnessMax) {
        mToughnessMax = toughnessMax;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
