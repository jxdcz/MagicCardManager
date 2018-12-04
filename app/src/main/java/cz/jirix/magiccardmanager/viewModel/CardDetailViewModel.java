package cz.jirix.magiccardmanager.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.provider.RepositoryProvider;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;

public class CardDetailViewModel extends AndroidViewModel {

    private CurrentSelectionRepository mSelectionRepository;

    public CardDetailViewModel(@NonNull Application application) {
        super(application);
        mSelectionRepository = (CurrentSelectionRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.PREFERENCES);
    }

    public LiveData<MagicCard> getCurrentCard(){
        return mSelectionRepository.getSelectedCard();
    }





}
