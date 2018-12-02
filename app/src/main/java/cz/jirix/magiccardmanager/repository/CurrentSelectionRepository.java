package cz.jirix.magiccardmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
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

    private MutableLiveData<String> mDataLoadingState = new MutableLiveData<>();
    private MutableLiveData<MagicCard> mSelectedCard = new MutableLiveData<>();
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
    private MutableLiveData<Integer> mPagesOfResults = new MutableLiveData<>();
    private MutableLiveData<List<MagicCard>> mCurrentCards = new MutableLiveData<>();
    private CardSearchCriteria mCurrentSearchCriteria;


    public CurrentSelectionRepository(MagicCardApi cardApi) {
        mCardApi = cardApi;
        mDataLoadingState.setValue(LoadingState.IDLE);
        resetPages();
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

    public LiveData<String> getLoadingState(){
        return mDataLoadingState;
    }

    public void loadCards(CardSearchCriteria criteria){
        mDataLoadingState.setValue(LoadingState.LOADING);
        Call<MagicCardsResponse> call = mCardApi.getCardsCall(
                criteria.getCardName(),
                criteria.getColor(),
                criteria.getSetName(),
                criteria.getType()
        );

        call.enqueue(new Callback<MagicCardsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MagicCardsResponse> call, @NonNull Response<MagicCardsResponse> response) {
                //TODO debug
                if(criteria.getPowerMin() > 50){
                    onFailure(call, new Throwable());
                    return;
                }

                int returnedCount = Integer.parseInt(response.headers().get(MagicCardApi.HEADER_RESP_COUNT));
                int totalCount = Integer.parseInt(response.headers().get(MagicCardApi.HEADER_RESP_TOTAL_COUNT));

                int pages = totalCount / returnedCount;
                mPagesOfResults.postValue(pages);

                if (response.body() != null) {
                    mCurrentCards.postValue(response.body().getCards());
                }
                mDataLoadingState.setValue(LoadingState.SUCCESS);
            }

            @Override
            public void onFailure(@NonNull Call<MagicCardsResponse> call, @NonNull Throwable t) {
                Log.w(TAG, "Fetching cards from the api failed, fetching local results");
                mDataLoadingState.setValue(LoadingState.NETWORK_ERROR);
                resetPages();
                //TODO load room results
                mCurrentCards.setValue(new ArrayList<>());
            }
        });
    }

    private void resetPages(){
        mPagesOfResults.postValue(1);
        mCurrentPage.postValue(1);
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

    public LiveData<Integer> getCurrentCardsPageCount() {
        return mPagesOfResults;
    }

    public LiveData<Integer> getCurrentCardsPage() {
        return mCurrentPage;
    }


    public static class LoadingState{
        public static final String IDLE = "idle";
        public static final String LOADING = "loading";
        public static final String NETWORK_ERROR = "networkError";
        public static final String SUCCESS = "success";
    }
}
