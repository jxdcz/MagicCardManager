package cz.jirix.magiccardmanager.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.model.MagicType;
import cz.jirix.magiccardmanager.navigation.AppNavigator;
import cz.jirix.magiccardmanager.provider.RepositoryProvider;
import cz.jirix.magiccardmanager.repository.AddInfoRepository;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;

public class SearchViewModel extends AndroidViewModel {

    private AddInfoRepository mAdditionalInfoRepository;
    private CurrentSelectionRepository mCurrentSelectionRepository;


    private String mLastLoadingState;
    private CardSearchCriteria mCurrentSelection;

    private LoadingStateObserver mStateObserver = new LoadingStateObserver();


    public SearchViewModel(@NonNull Application application) {
        super(application);
        mAdditionalInfoRepository = (AddInfoRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.ADD_INFO);
        mCurrentSelectionRepository = (CurrentSelectionRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.PREFERENCES);
        mCurrentSelection = mCurrentSelectionRepository.getCurrentSearchCriteria();
        if(mCurrentSelection == null){
            mCurrentSelection = new CardSearchCriteria();
        }
    }

    public LiveData<List<MagicColor>> getCardColors() {
        return mAdditionalInfoRepository.getLiveColors();
    }

    public LiveData<List<MagicSet>> getCardSets() {
        return mAdditionalInfoRepository.getLiveSets();
    }

    public LiveData<String> getLoadingState() {
        return mCurrentSelectionRepository.getLoadingState();
    }

    public void acknowledgeState(String state) {
        mLastLoadingState = state;
    }

    public String getLastLoadingState() {
        return mLastLoadingState;
    }

    public CardSearchCriteria getCurrentSelection() {
        return mCurrentSelection;
    }

    public void onToughnessEntered(int min, int max) {
        mCurrentSelection.setToughnessMin(min);
        mCurrentSelection.setToughnessMax(max);
    }

    public void onPowerEntered(int min, int max) {
        mCurrentSelection.setPowerMin(min);
        mCurrentSelection.setPowerMax(max);
    }

    public void onCardNameEntered(String name) {
        mCurrentSelection.setCardName(name);
    }

    public void onCardColorEntered(String color) {
        mCurrentSelection.setColor(color);
    }

    public void onCardSetEntered(String name) {
        mCurrentSelection.setSetName(name);
    }

    public void onSelectionSubmitted() {
        mCurrentSelectionRepository.setCurrentSearchCriteria(mCurrentSelection);
        mStateObserver.ignoreNextChange();
        mCurrentSelectionRepository.getLoadingState().observeForever(mStateObserver);
        mCurrentSelectionRepository.loadCards(mCurrentSelection, 1);
    }

    private void onDataLoadingStateChanged(String state) {
        if (state == null) {
            return;
        }

        switch (state) {
            case CurrentSelectionRepository.LoadingState.NETWORK_ERROR:
            case CurrentSelectionRepository.LoadingState.SUCCESS:
                // we are finished
                mCurrentSelectionRepository.getLoadingState().removeObserver(mStateObserver);
                AppNavigator.goToCardListDetailActivity(getApplication().getApplicationContext());
                break;
        }
    }

    public void onCardTypeEntered(String type) {
        mCurrentSelection.setType(type);
    }

    public LiveData<List<MagicType>> getCardTypes() {
        return mAdditionalInfoRepository.getLiveTypes();
    }

    private class LoadingStateObserver implements Observer<String> {

        private boolean mIgnoreNextChange;

        void ignoreNextChange() {
            mIgnoreNextChange = true;
        }

        @Override
        public void onChanged(@Nullable String state) {
            if (mIgnoreNextChange) {
                mIgnoreNextChange = false;
                return;
            }
            onDataLoadingStateChanged(state);

        }
    }
}
