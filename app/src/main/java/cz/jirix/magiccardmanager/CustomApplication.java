package cz.jirix.magiccardmanager;

import android.app.Application;

import cz.jirix.magiccardmanager.provider.DbProvider;
import cz.jirix.magiccardmanager.provider.RepositoryProvider;
import cz.jirix.magiccardmanager.provider.WebserviceProvider;

public class CustomApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DbProvider.get().init(getApplicationContext());
        WebserviceProvider.get().initWebservices();
        RepositoryProvider.get().initRepositories(WebserviceProvider.get(), DbProvider.get());
    }
}
