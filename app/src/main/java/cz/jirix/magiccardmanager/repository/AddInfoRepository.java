package cz.jirix.magiccardmanager.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicRarity;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicSetsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInfoRepository implements IRepository{
    private static final String TAG = AddInfoRepository.class.getSimpleName();


    private MagicCardApi mMagicWebservice;

    private MutableLiveData<List<MagicColor>> mLiveColors = new MutableLiveData<>();
    private MutableLiveData<List<MagicSet>> mLiveSets = new MutableLiveData<>();
    private MutableLiveData<List<MagicRarity>> mLiveRarities = new MutableLiveData<>();

    public AddInfoRepository(MagicCardApi webservice) {
        mMagicWebservice = webservice;

        //TODO persistent
        List<MagicColor> colors = new ArrayList<>();
        colors.add(new MagicColor("W", "White"));
        colors.add(new MagicColor("BL", "Black"));
        colors.add(new MagicColor("G", "Green"));
        colors.add(new MagicColor("R", "Red"));
        colors.add(new MagicColor("B", "Blue"));
        setColors(colors);

        List<MagicRarity> rarities = new ArrayList<>();
        rarities.add(new MagicRarity("Land"));
        rarities.add(new MagicRarity("Common"));
        rarities.add(new MagicRarity("Uncommon"));
        rarities.add(new MagicRarity("Rare"));
        rarities.add(new MagicRarity("Mythic Rare"));
        rarities.add(new MagicRarity("Timeshifted"));
        rarities.add(new MagicRarity("Masterpiece"));
        setRarities(rarities);

    }

    public void setColors(List<MagicColor> colors){
        mLiveColors.postValue(colors);
    }

    public void setRarities(List<MagicRarity> rarities) {
        mLiveRarities.postValue(rarities);
    }

    public LiveData<List<MagicColor>> getLiveColors() {
        return mLiveColors;
    }

    public LiveData<List<MagicSet>> getLiveSets(){
        loadSetsFromWebservice();
        return mLiveSets;
    }

    private void loadSetsFromWebservice() {
        mMagicWebservice.getSetsCall().enqueue(new Callback<MagicSetsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MagicSetsResponse> call, @NonNull Response<MagicSetsResponse> response) {
                if (response.body() != null) {
                    List<MagicSet> list = response.body().getSets();
                    mLiveSets.postValue(list);
                    saveSetsToDb(list);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MagicSetsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Error fetching sets from remote service: " + t.getMessage());
            }
        });
    }

    private void saveSetsToDb(List<MagicSet> sets){
        // TODO ROOM
    }
}
