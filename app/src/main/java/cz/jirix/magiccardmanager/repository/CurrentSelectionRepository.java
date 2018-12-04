package cz.jirix.magiccardmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.persistence.MagicCardDao;
import cz.jirix.magiccardmanager.persistence.MagicSetDao;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicCardsResponse;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentSelectionRepository implements IRepository {
    private static final String TAG = CurrentSelectionRepository.class.getSimpleName();

    private MagicCardApi mCardApi;
    private MagicCardDao mCardDao;
    private MagicSetDao mSetDao;

    private MutableLiveData<String> mDataLoadingState = new MutableLiveData<>();
    private MutableLiveData<MagicCard> mSelectedCard = new MutableLiveData<>();
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
    private MutableLiveData<Integer> mPagesCount = new MutableLiveData<>();
    private MutableLiveData<List<MagicCard>> mCurrentCards = new MutableLiveData<>();
    private CardSearchCriteria mCurrentSearchCriteria;
    private int mPageSize;

    public CurrentSelectionRepository(MagicCardApi cardApi, MagicCardDao cardDao, MagicSetDao setDao) {
        mCardApi = cardApi;
        mCardDao = cardDao;
        mSetDao = setDao;
        mDataLoadingState.setValue(LoadingState.IDLE);
        resetPages();
    }

    public void selectCard(String cardId) {
        // we could just pass the card itself, but this is safer
        List<MagicCard> currentCards = mCurrentCards.getValue();
        if (currentCards == null) {
            return;
        }

        MagicCard selected = null;
        for (MagicCard card : currentCards) {
            if (card.getId().equals(cardId)) {
                selected = card;
            }
        }

        if (selected != null) {
            mSelectedCard.postValue(selected);
        }
    }

    public LiveData<String> getLoadingState() {
        return mDataLoadingState;
    }

    public void loadCardsPrevPage() {
        int current = mCurrentPage.getValue() == null ? 1 : mCurrentPage.getValue();
        loadCards(mCurrentSearchCriteria, current <= 1 ? current : current - 1);
    }

    public void loadCardsNextPage() {
        int current = mCurrentPage.getValue() == null ? 1 : mCurrentPage.getValue();
        int last = mPagesCount.getValue() == null ? current : mPagesCount.getValue();
        loadCards(mCurrentSearchCriteria, current >= last ? current : current + 1);
    }

    public void loadCards(CardSearchCriteria criteria, int page) {
        mDataLoadingState.setValue(LoadingState.LOADING);
        Call<MagicCardsResponse> call = mCardApi.getCardsCall(
                criteria.getCardName(),
                criteria.getColor(),
                criteria.getSetName(),
                criteria.getType(),
                page
        );

        call.enqueue(new Callback<MagicCardsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MagicCardsResponse> call, @NonNull Response<MagicCardsResponse> response) {
                onNetworkLoadCardsSuccess(page, response);
            }

            @Override
            public void onFailure(@NonNull Call<MagicCardsResponse> call, @NonNull Throwable t) {
                onNetworkLoadCardsFailure(criteria, t);
            }
        });
    }

    private void onNetworkLoadCardsSuccess(int page, Response<MagicCardsResponse> response){
        int returnedCount = Integer.parseInt(response.headers().get(MagicCardApi.HEADER_RESP_COUNT));
        int totalCount = Integer.parseInt(response.headers().get(MagicCardApi.HEADER_RESP_TOTAL_COUNT));
        mPageSize = Integer.parseInt(response.headers().get(MagicCardApi.HEADER_RESP_PAGE_SIZE));

        int pages = returnedCount == 0 ? 1 : totalCount / returnedCount; // if we get no results
        mPagesCount.postValue(pages);
        mCurrentPage.postValue(page);

        if (response.body() != null) {
            List<MagicCard> cards = response.body().getCards();
            saveCardsToDb(cards);
            mCurrentCards.postValue(cards);
        }
        mDataLoadingState.setValue(LoadingState.SUCCESS);
    }

    private void onNetworkLoadCardsFailure(CardSearchCriteria criteria, Throwable t){
        Log.w(TAG, "Fetching cards from the api failed, fetching local results: " + t);
        mDataLoadingState.setValue(LoadingState.NETWORK_ERROR);
        resetPages();
        loadCardsFromDb(criteria);
    }

    private void loadCardsFromDb(CardSearchCriteria criteria) {
        Single.fromCallable(() -> {
            MagicSet set = mSetDao.getByName(criteria.getSetName());
            return mCardDao.getAllBy(
                    anyIfNotSet(criteria.getCardName()),
                    anyIfNotSet(set == null ? null : set.getCode()),
                    anyIfNotSet(criteria.getType()),
                    anyIfNotSet(criteria.getColor())
            );
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MagicCard>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<MagicCard> magicTypes) {
                        mCurrentCards.postValue(magicTypes);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

    }

    private String anyIfNotSet(String param){
        return param == null || param.isEmpty() ? "%%" : param;
    }

    private void saveCardsToDb(List<MagicCard> cards) {
        Completable.fromCallable(() ->{
            mCardDao.insertAll(cards);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void resetPages() {
        mPagesCount.postValue(1);
        mCurrentPage.postValue(1);
        mPageSize = 0;
    }

    public CardSearchCriteria getCurrentSearchCriteria() {
        return mCurrentSearchCriteria;
    }

    public void setCurrentSearchCriteria(CardSearchCriteria searchCriteria) {
        mCurrentSearchCriteria = searchCriteria;
    }

    public void setCurrentCards(List<MagicCard> cards){
        mCurrentCards.postValue(cards);
    }

    public LiveData<List<MagicCard>> getCurrentCards() {
        return mCurrentCards;
    }

    public LiveData<MagicCard> getSelectedCard() {
        return mSelectedCard;
    }

    public LiveData<Integer> getCurrentCardsPageCount() {
        return mPagesCount;
    }

    public LiveData<Integer> getCurrentCardsPage() {
        return mCurrentPage;
    }

    public int getCardsPerPage() {
        return mPageSize;
    }

    public static class LoadingState {
        public static final String IDLE = "idle";
        public static final String LOADING = "loading";
        public static final String NETWORK_ERROR = "networkError";
        public static final String SUCCESS = "success";
    }
}
