package cz.jirix.magiccardmanager.provider;

import java.util.HashMap;
import java.util.Map;

import cz.jirix.magiccardmanager.repository.AddInfoRepository;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;
import cz.jirix.magiccardmanager.repository.IRepository;

public class RepositoryProvider {

    private static RepositoryProvider sInstance;

    private Map<String, IRepository> mRepositories;


    private RepositoryProvider(){
        mRepositories = new HashMap<>();
    }

    public static RepositoryProvider get(){
        if(sInstance == null){
            sInstance = new RepositoryProvider();
        }
        return sInstance;
    }


    public void initRepositories(WebserviceProvider webserviceProvider){
        mRepositories.put(Repo.ADD_INFO, new AddInfoRepository(webserviceProvider.getMagicService()));
        mRepositories.put(Repo.PREFERENCES, new CurrentSelectionRepository(webserviceProvider.getMagicService()));
    }

    public IRepository getRepository(String type) {
        return mRepositories.get(type);
    }


    public static class Repo{
        public static final String ADD_INFO = "additionalInfoRepository";
        public static final String PREFERENCES = "currentPreferences";
    }
}
