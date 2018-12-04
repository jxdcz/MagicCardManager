package cz.jirix.magiccardmanager.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.provider.RepositoryProvider;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;

public class CardListViewModel extends AndroidViewModel {

    private CurrentSelectionRepository mSelectionRepository;

    public CardListViewModel(@NonNull Application application) {
        super(application);
        mSelectionRepository = (CurrentSelectionRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.PREFERENCES);
    }

    public LiveData<List<MagicCard>> getCurrentCards() {
        return mSelectionRepository.getCurrentCards();
    }

    public void onCardSelected(MagicCard card) {
        mSelectionRepository.selectCard(card.getId());
    }

    public LiveData<String> getNetworkState() {
        return mSelectionRepository.getLoadingState();
    }

    public LiveData<Integer> getCurrentCardsPageCount() {
        return mSelectionRepository.getCurrentCardsPageCount();
    }

    public LiveData<Integer> getCurrentCardsPage() {
        return mSelectionRepository.getCurrentCardsPage();
    }

    public void onNextPage() {
        mSelectionRepository.loadCardsNextPage();
    }

    public void onPrevPage() {
        mSelectionRepository.loadCardsPrevPage();
    }

    public int getPreviousResultsCount() {
        int currentPage = getCurrentCardsPage().getValue() == null ? 1 : getCurrentCardsPage().getValue();
        int itemsPerPage = mSelectionRepository.getCardsPerPage();
        return ((currentPage - 1) * itemsPerPage);
    }
}
