package cz.jirix.magiccardmanager.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.navigation.AppNavigator;
import cz.jirix.magiccardmanager.provider.RepositoryProvider;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.repository.AddInfoRepository;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;

public class SearchViewModel extends AndroidViewModel {

    private AddInfoRepository mAdditionalInfoRepository;
    private CurrentSelectionRepository mCurrentSelectionRepository;


    private CardSearchCriteria mCurrentSelection;


    public SearchViewModel(@NonNull Application application) {
        super(application);
        mAdditionalInfoRepository = (AddInfoRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.ADD_INFO);
        mCurrentSelectionRepository = (CurrentSelectionRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.PREFERENCES);
        mCurrentSelection = new CardSearchCriteria();
    }

    public LiveData<List<MagicColor>> getCardColors(){
        return mAdditionalInfoRepository.getLiveColors();
    }

    public LiveData<List<MagicSet>> getCardSets() {
        return mAdditionalInfoRepository.getLiveSets();
    }

    public void onToughnessEntered(int min, int max){
        mCurrentSelection.setToughnessMin(min);
        mCurrentSelection.setToughnessMax(max);
    }

    public void onPowerEntered(int min, int max){
        mCurrentSelection.setPowerMin(min);
        mCurrentSelection.setPowerMax(max);
    }

    public void onCardNameEntered(String name){
        mCurrentSelection.setCardName(name);
    }

    public void onCardColorEntered(String color){
        mCurrentSelection.setColor(color);
    }

    public void onCardSetEntered(String name){
        mCurrentSelection.setSetName(name);
    }

    public void onSelectionSubmitted(){
        mCurrentSelectionRepository.setCurrentSearchCriteria(mCurrentSelection);
        mCurrentSelectionRepository.loadCards(mCurrentSelection);
        AppNavigator.goToCardListDetailActivity(getApplication().getApplicationContext());
    }
}
