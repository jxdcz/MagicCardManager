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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.fragments.adapters.SimpleArrayWithDefValueAdapter;
import cz.jirix.magiccardmanager.fragments.adapters.MagicSetsSpinnerAdapter;
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
    @BindView(R.id.edit_power_min) EditText mEditPowerMin;
    @BindView(R.id.edit_power_max) EditText mEditPowerMax;
    @BindView(R.id.edit_toughness_min) EditText mEditToughnessMin;
    @BindView(R.id.edit_toughness_max) EditText mEditToughnessMax;

    @BindView(R.id.button_search) LoadingButton mButtonSearch;

    private SimpleArrayWithDefValueAdapter<MagicColor> mAdapterColors;
    private MagicSetsSpinnerAdapter mAdapterSets;
    private SimpleArrayWithDefValueAdapter<MagicType> mAdapterTypes;

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
    public void onStart() {
        super.onStart();
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
        initToughnessEdit();
        initPowerEdit();
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
        //mSpinnerColor.setSelection(mAdapterColors.getItemPosition(lastSearch.getColor()));
    }


    private void initPowerEdit() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getViewModel().onPowerEntered(
                        Integer.parseInt(mEditPowerMin.getText().toString()),
                        Integer.parseInt(mEditPowerMax.getText().toString())
                );
            }
        };
        mEditPowerMin.addTextChangedListener(watcher);
        mEditPowerMax.addTextChangedListener(watcher);
    }

    private void initToughnessEdit() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getViewModel().onToughnessEntered(
                        Integer.parseInt(mEditToughnessMin.getText().toString()),
                        Integer.parseInt(mEditToughnessMax.getText().toString())
                );
            }
        };
        mEditToughnessMin.addTextChangedListener(watcher);
        mEditToughnessMax.addTextChangedListener(watcher);
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
        mAdapterColors = new SimpleArrayWithDefValueAdapter<>(getContext());
        mSpinnerColor.setAdapter(mAdapterColors);
        mSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MagicColor color = (MagicColor) mAdapterColors.getItem(i);
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
        mAdapterTypes = new SimpleArrayWithDefValueAdapter<>(getContext());
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
        getViewModel().getLoadingState().observe(this, this::onLoadingStateChanged);
    }

    private void onLoadingStateChanged(String state) {
        if (state.equals(getViewModel().getLastLoadingState())) {
            return;
        }
        getViewModel().acknowledgeState(state);

        switch (state) {
            case CurrentSelectionRepository.LoadingState.NETWORK_ERROR:
                Toast.makeText(getContext(), R.string.error_network_load_failed, Toast.LENGTH_SHORT).show();
                mButtonSearch.showProgress(false);
                break;
            case CurrentSelectionRepository.LoadingState.SUCCESS:
                mButtonSearch.showProgress(false);
                break;
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
