package cz.jirix.magiccardmanager.injection;

import java.util.HashMap;
import java.util.Map;

import cz.jirix.magiccardmanager.repository.AddInfoRepository;
import cz.jirix.magiccardmanager.repository.IRepository;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;

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
        mRepositories.put(Repo.ADD_INFO, new AddInfoRepository((MagicCardApi) webserviceProvider.getService(WebserviceProvider.Services.MAGIC)));
    }

    public IRepository getRepository(String type) {
        return mRepositories.get(type);
    }


    public static class Repo{
        public static final String ADD_INFO = "additionalInfoRepository";
    }
}
