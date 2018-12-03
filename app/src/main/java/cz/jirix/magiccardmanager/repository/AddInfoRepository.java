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
import cz.jirix.magiccardmanager.model.MagicType;
import cz.jirix.magiccardmanager.persistence.MagicSetDao;
import cz.jirix.magiccardmanager.persistence.MagicTypeDao;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicSetsResponse;
import cz.jirix.magiccardmanager.webservices.MagicTypesResponse;
import io.reactivex.Completable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInfoRepository implements IRepository{
    private static final String TAG = AddInfoRepository.class.getSimpleName();

    private MagicCardApi mMagicWebservice;

    private MagicSetDao mSetDao;
    private MagicTypeDao mTypeDao;

    private MutableLiveData<List<MagicColor>> mLiveColors = new MutableLiveData<>();
    private MutableLiveData<List<MagicSet>> mLiveSets = new MutableLiveData<>();
    private MutableLiveData<List<MagicRarity>> mLiveRarities = new MutableLiveData<>();
    private MutableLiveData<List<MagicType>> mLiveTypes = new MutableLiveData<>();

    public AddInfoRepository(MagicCardApi webservice, MagicSetDao setDao, MagicTypeDao typeDao) {
        mMagicWebservice = webservice;
        mSetDao = setDao;
        mTypeDao = typeDao;

        //TODO persistent - these do not come from the API, but it still would be better to save them at first launch
        List<MagicColor> colors = new ArrayList<>();
        colors.add(new MagicColor("W", "White"));
        colors.add(new MagicColor("BL", "Black"));
        colors.add(new MagicColor("G", "Green"));
        colors.add(new MagicColor("R", "Red"));
        colors.add(new MagicColor("B", "Blue"));
        setColors(colors);

        //TODO search by rarities
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

    public LiveData<List<MagicRarity>> getRarities(){
        return mLiveRarities;
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
                loadSetsFromDb();
            }
        });
    }

    private void loadSetsFromDb(){
        mSetDao.getAllRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MagicSet>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onSuccess(List<MagicSet> magicSets) { mLiveSets.postValue(magicSets); }

            @Override
            public void onError(Throwable e) { }
        });
    }

    private void saveSetsToDb(List<MagicSet> sets){
        Completable.fromCallable(() ->{
            // remove all existing and insert the new ones - use a better algorithm in the future
            List<MagicSet> existing = mSetDao.getAll();
            mSetDao.deleteSets(existing);
            mSetDao.insertAll(sets);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


    }

    public LiveData<List<MagicType>> getLiveTypes(){
        loadTypesFromWebservice();
        return mLiveTypes;
    }

    public void loadTypesFromWebservice() {
        mMagicWebservice.getTypesCall().enqueue(new Callback<MagicTypesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MagicTypesResponse> call, @NonNull Response<MagicTypesResponse> response) {
                if (response.body() != null) {
                    List<MagicType> list = response.body().getConvertedTypes();
                    mLiveTypes.postValue(list);
                    saveTypesToDb(list);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MagicTypesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "Error fetching sets from remote service: " + t.getMessage());
                loadTypesFromDb();
            }
        });
    }

    private void loadTypesFromDb(){
        mTypeDao.getAllRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MagicType>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<MagicType> magicTypes) { mLiveTypes.postValue(magicTypes); }

                    @Override
                    public void onError(Throwable e) { }
                });
    }

    private void saveTypesToDb(List<MagicType> types){
        Completable.fromCallable(() ->{
            // remove all existing and insert the new ones - use a better algorithm in the future
            List<MagicType> existing = mTypeDao.getAll();
            mTypeDao.deleteSets(existing);
            mTypeDao.insertAll(types);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
