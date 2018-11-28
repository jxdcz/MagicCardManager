package cz.jirix.magiccardmanager.webservices;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicCard;

public class MagicCardsResponse {

    @SerializedName("cards")
    private List<MagicCard> mCards;

    public MagicCardsResponse(List<MagicCard> cards) {
        mCards = cards;
    }

    public List<MagicCard> getCards() {
        return mCards;
    }

    public void setCards(List<MagicCard> cards) {
        mCards = cards;
    }
}
