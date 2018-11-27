package cz.jirix.magiccardmanager.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import cz.jirix.magiccardmanager.injection.RepositoryProvider;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.repository.AddInfoRepository;

public class SearchViewModel extends AndroidViewModel {

    private AddInfoRepository mAdditionalInfoRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        mAdditionalInfoRepository = (AddInfoRepository) RepositoryProvider.get().getRepository(RepositoryProvider.Repo.ADD_INFO);
    }





    public LiveData<List<MagicColor>> getCardColors(){
        return mAdditionalInfoRepository.getLiveColors();
    }

    public LiveData<List<MagicSet>> getCardSets() {
        return mAdditionalInfoRepository.getLiveSets();
    }
}
