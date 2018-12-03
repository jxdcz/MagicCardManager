package cz.jirix.magiccardmanager.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.fragments.adapters.MagicSetsSpinnerAdapter;
import cz.jirix.magiccardmanager.fragments.adapters.SimpleArrayWithDefValueAdapter;
import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicType;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;
import cz.jirix.magiccardmanager.viewModel.SearchViewModel;
import cz.jirix.magiccardmanager.views.LoadingButton;


public class SearchFragment extends Fragment {

    public static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.autocomplete_set) AutoCompleteTextView mAutocompleteSet;
    @BindView(R.id.spinner_color) Spinner mSpinnerColor;
    @BindView(R.id.spinner_type) Spinner mSpinnerType;

    @BindView(R.id.edit_card_name) EditText mEditCardName;
    @BindView(R.id.range_bar_power) RangeBar mRangeBarPower;
    @BindView(R.id.range_bar_toughness) RangeBar mRangeBarToughness;

    @BindView(R.id.button_search) LoadingButton mButtonSearch;

    private SimpleArrayWithDefValueAdapter<MagicColor> mAdapterColors;
    private MagicSetsSpinnerAdapter mAdapterSets;
    private SimpleArrayWithDefValueAdapter<MagicType> mAdapterTypes;
    private boolean mIgnoreFirstStateChange;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        repopulateLastSearch();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        initSpinnerColors();
        initSpinnerSets();
        initSpinnerTypes();
        initCardNameEdit();
        initToughnessRange();
        initPowerRange();
        observeLoadingState();

        return rootView;
    }

    private void repopulateLastSearch() {
        CardSearchCriteria lastSearch = getViewModel().getCurrentSelection();
        mEditCardName.setText(lastSearch.getCardName());
        // a workaround to not show suggestions when repopulating
        int lastThreshold = mAutocompleteSet.getThreshold();
        mAutocompleteSet.setThreshold(999);
        mAutocompleteSet.setText(lastSearch.getSetName());
        mAutocompleteSet.setThreshold(lastThreshold);

        mSpinnerColor.setSelection(mAdapterColors.getItemPosition(lastSearch.getColor()));
        mSpinnerType.setSelection(mAdapterTypes.getItemPosition(lastSearch.getType()));

        if (lastSearch.getPowerMin() != 0 && lastSearch.getPowerMax() != 0) {
            mRangeBarPower.setRangePinsByValue(lastSearch.getPowerMin(), lastSearch.getPowerMax());
        }
        if (lastSearch.getToughnessMin() != 0 && lastSearch.getToughnessMax() != 0) {
            mRangeBarToughness.setRangePinsByValue(lastSearch.getToughnessMin(), lastSearch.getToughnessMax());
        }
    }


    private int parseInt(String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(string);
    }

    private void initPowerRange() {
        mRangeBarPower.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) ->
                getViewModel().onPowerEntered(
                        parseInt(leftPinValue),
                        parseInt(rightPinValue)

                )
        );
    }

    private void initToughnessRange() {
        mRangeBarToughness.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) ->
                getViewModel().onToughnessEntered(
                        parseInt(leftPinValue),
                        parseInt(rightPinValue)

                )
        );
    }

    private void initCardNameEdit() {
        mEditCardName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getViewModel().onCardNameEntered(editable.toString());
            }
        });
    }


    private void initSpinnerColors() {
        mAdapterColors = new SimpleArrayWithDefValueAdapter<>(getContext(), getString(R.string.all_possible));
        mSpinnerColor.setAdapter(mAdapterColors);
        mSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MagicColor color = mAdapterColors.getItem(i);
                getViewModel().onCardColorEntered(color == null ? null : color.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // empty
            }
        });
        observeColors();
    }

    private void observeColors() {
        getViewModel().getCardColors().observe(this, magicColors -> mAdapterColors.setData(magicColors));
    }

    private void initSpinnerTypes() {
        mAdapterTypes = new SimpleArrayWithDefValueAdapter<>(getContext(), getString(R.string.all_possible));
        mSpinnerType.setAdapter(mAdapterTypes);
        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MagicType magicType = mAdapterTypes.getItem(i);
                getViewModel().onCardTypeEntered(magicType == null ? null : magicType.getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        observeTypes();
    }

    private void observeTypes() {
        getViewModel().getCardTypes().observe(this, magicTypes -> mAdapterTypes.setData(magicTypes));
    }

    private void initSpinnerSets() {
        mAdapterSets = new MagicSetsSpinnerAdapter(getContext());
        mAutocompleteSet.setAdapter(mAdapterSets);
        mAutocompleteSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getViewModel().onCardSetEntered(editable.toString());
            }
        });
        observeSets();
    }

    private void observeSets() {
        getViewModel().getCardSets().observe(this, magicSets -> mAdapterSets.setData(magicSets));
    }

    private void observeLoadingState() {
        mIgnoreFirstStateChange = true;
        getViewModel().getLoadingState().observe(this, this::onLoadingStateChanged);
    }

    private void onLoadingStateChanged(String state) {
        if (state.equals(getViewModel().getLastLoadingState()) || mIgnoreFirstStateChange) {
            mIgnoreFirstStateChange = false;
            return;
        }
        getViewModel().acknowledgeState(state);

        switch (state) {
            case CurrentSelectionRepository.LoadingState.NETWORK_ERROR:
                showErrorToast();
                mButtonSearch.showProgress(false);
                break;
            case CurrentSelectionRepository.LoadingState.SUCCESS:
                mButtonSearch.showProgress(false);
                break;
        }
    }
    
    private void showErrorToast(){
        if(getContext() != null) {
            // without app context, toast gets weird styling
            Toast toast = Toast.makeText(getContext().getApplicationContext(), R.string.error_network_load_failed, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @OnClick(R.id.button_search)
    public void onSearchClicked() {
        mButtonSearch.showProgress(true);
        getViewModel().onSelectionSubmitted();
    }

    private SearchViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SearchViewModel.class);
    }
}
