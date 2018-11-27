package cz.jirix.magiccardmanager;

import android.app.Application;

import cz.jirix.magiccardmanager.injection.RepositoryProvider;
import cz.jirix.magiccardmanager.injection.WebserviceProvider;

public class CustomApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        RepositoryProvider.get().initRepositories();
        WebserviceProvider.get().initWebservices();

    }
}
