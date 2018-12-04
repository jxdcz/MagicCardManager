package cz.jirix.magiccardmanager.webservices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MagicCardApi {
    /*
    WIKI
    https://docs.magicthegathering.io
     */

    String BASE_URL = "https://api.magicthegathering.io/v1/";

    String HEADER_RESP_TOTAL_COUNT = "Total-Count";
    String HEADER_RESP_COUNT = "Count";
    String HEADER_RESP_PAGE_SIZE = "Page-Size";

    @GET("sets")
    Call<MagicSetsResponse> getSetsCall();

    @GET("types")
    Call<MagicTypesResponse> getTypesCall();

    @GET("cards")
    Call<MagicCardsResponse> getCardsCall(@Query("name") String name, @Query("colors") String colors, @Query("setName") String setName, @Query("types") String type, @Query("page") int page);

}
