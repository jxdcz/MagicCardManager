package cz.jirix.magiccardmanager.injection;

import java.util.HashMap;
import java.util.Map;

import cz.jirix.magiccardmanager.webservices.IWebservice;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import retrofit2.Retrofit;

public class WebserviceProvider {

    private static WebserviceProvider sInstance;

    private Map<String, IWebservice> mWebservices;

    private WebserviceProvider(){
        mWebservices = new HashMap<>();
    }

    public static WebserviceProvider get(){
        if(sInstance == null){
            sInstance = new WebserviceProvider();
        }
        return sInstance;
    }

    public void initWebservices(){
        // Magic API
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MagicCardApi.BASE_URL).build();
        mWebservices.put(Services.MAGIC, retrofit.create(MagicCardApi.class));

    }

    public IWebservice getService(String type){
        return mWebservices.get(type);
    }

    public static class Services{
        public static final String MAGIC = "magicTheGatheringApi";
    }

}
