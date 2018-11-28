package cz.jirix.magiccardmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicCardsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentSelectionRepository implements IRepository {
    private static final String TAG = CurrentSelectionRepository.class.getSimpleName();

    private MagicCardApi mCardApi;

    private MutableLiveData<MagicCard> mSelectedCard = new MutableLiveData<>();
    private MutableLiveData<List<MagicCard>> mCurrentCards = new MutableLiveData<>();
    private CardSearchCriteria mCurrentSearchCriteria;


    public CurrentSelectionRepository(MagicCardApi cardApi) {
        mCardApi = cardApi;
    }

    public void selectCard(String cardId){
        // we could just pass the card itself, but this is safer
        List<MagicCard> currentCards = mCurrentCards.getValue();
        if(currentCards == null){
            return;
        }

        MagicCard selected = null;
        for (MagicCard card : currentCards) {
            if(card.getId().equals(cardId)){
                selected = card;
            }
        }

        if(selected != null){
            mSelectedCard.postValue(selected);
        }
    }

    public void loadCards(CardSearchCriteria criteria){

        Call<MagicCardsResponse> call = mCardApi.getCardsCall(
                criteria.getCardName(),
                criteria.getColor(),
                criteria.getSetName()
        );

        call.enqueue(new Callback<MagicCardsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MagicCardsResponse> call, @NonNull Response<MagicCardsResponse> response) {
                if (response.body() != null) {
                    mCurrentCards.postValue(response.body().getCards());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MagicCardsResponse> call, @NonNull Throwable t) {
                Log.w(TAG, "Fetching cards from the api failed, fetching local results");
                //TODO load room results
            }
        });
    }

    public CardSearchCriteria getCurrentSearchCriteria() {
        return mCurrentSearchCriteria;
    }

    public void setCurrentSearchCriteria(CardSearchCriteria searchCriteria) {
        mCurrentSearchCriteria = searchCriteria;
    }

    public LiveData<List<MagicCard>> getCurrentCards() {
        return mCurrentCards;
    }

    public LiveData<MagicCard> getSelectedCard() {
        return mSelectedCard;
    }
}
