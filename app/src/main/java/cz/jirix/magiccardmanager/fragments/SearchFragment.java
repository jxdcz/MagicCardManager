package cz.jirix.magiccardmanager.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicSet;
import cz.jirix.magiccardmanager.viewModel.SearchViewModel;


public class SearchFragment extends Fragment {

    public static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.spinner_set) Spinner mSpinnerSet;
    @BindView(R.id.spinner_color) Spinner mSpinnerColor;

    @BindView(R.id.edit_card_name) EditText mEditCardName;
    @BindView(R.id.edit_power_min) EditText mEditPowerMin;
    @BindView(R.id.edit_power_max) EditText mEditPowerMax;
    @BindView(R.id.edit_toughness_min) EditText mEditToughnessMin;
    @BindView(R.id.edit_toughness_max) EditText mEditToughnessMax;

    private MagicColorsSpinnerAdapter mAdapterColors;
    private MagicSetsSpinnerAdapter mAdapterSets;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        initSpinnerColors();
        initSpinnerSets();

        return rootView;
    }





    private void initSpinnerColors(){
        mAdapterColors = new MagicColorsSpinnerAdapter(getContext());
        mSpinnerColor.setAdapter(mAdapterColors);
        observeColors();
    }

    private void observeColors(){
        getViewModel().getCardColors().observe(this, magicColors -> mAdapterColors.setData(magicColors));
    }

    private void initSpinnerSets(){
        mAdapterSets = new MagicSetsSpinnerAdapter(getContext());
        mSpinnerSet.setAdapter(mAdapterSets);
        observeSets();
    }

    private void observeSets() {
        getViewModel().getCardSets().observe(this, magicSets -> mAdapterSets.setData(magicSets));
    }


    @OnClick(R.id.button_search)
    public void onSearchClicked(){

    }

    private SearchViewModel getViewModel(){
        return ViewModelProviders.of(this).get(SearchViewModel.class);
    }


    private static class MagicColorsSpinnerAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        private List<MagicColor> mData;

        public MagicColorsSpinnerAdapter(Context context){
            mInflater = LayoutInflater.from(context);
            mData = new ArrayList<>();
        }

        public void setData(List<MagicColor> colors){
            mData = new ArrayList<>(colors);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }

            MagicColor color = (MagicColor) getItem(i);
            ((TextView)convertView).setText(color.getName());
            return convertView;
        }
    }

    private static class MagicSetsSpinnerAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        private List<MagicSet> mData;

        public MagicSetsSpinnerAdapter(Context context){
            mInflater = LayoutInflater.from(context);
            mData = new ArrayList<>();
        }

        public void setData(List<MagicSet> sets){
            mData = new ArrayList<>(sets);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }

            MagicSet set = (MagicSet) getItem(i);
            ((TextView)convertView).setText(set.getName());
            return convertView;
        }
    }

}
