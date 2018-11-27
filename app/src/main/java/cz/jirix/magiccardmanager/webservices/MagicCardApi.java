package cz.jirix.magiccardmanager.webservices;

import android.arch.lifecycle.LiveData;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicSet;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MagicCardApi extends IWebservice {
    String BASE_URL = "https://api.magicthegathering.io/v1/";

    @GET("sets")
    LiveData<List<MagicSet>> getAllSets();

    @GET("sets")
    LiveData<List<MagicSet>> getSets(@Query("name") String name, @Query("block") String block);

    @GET("sets")
    Call<List<MagicSet>> getSetsCall();

}
