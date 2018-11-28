package cz.jirix.magiccardmanager.provider;

import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceProvider {

    private static WebserviceProvider sInstance;

    // cant use map and search by ID, retrofit doesn't allow that
    private MagicCardApi mMagicService;
    //todo MORE

    private WebserviceProvider(){
    }

    public static WebserviceProvider get(){
        if(sInstance == null){
            sInstance = new WebserviceProvider();
        }
        return sInstance;
    }

    public void initWebservices(){
        // Magic API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MagicCardApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMagicService = retrofit.create(MagicCardApi.class);

    }

    public MagicCardApi getMagicService() {
        return mMagicService;
    }
}
