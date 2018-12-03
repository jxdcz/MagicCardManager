package cz.jirix.magiccardmanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.fragments.CardDetailFragment;
import cz.jirix.magiccardmanager.fragments.adapters.CardListRecyclerAdapter;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.navigation.AppNavigator;
import cz.jirix.magiccardmanager.repository.CurrentSelectionRepository;
import cz.jirix.magiccardmanager.viewModel.CardListViewModel;

public class CardListActivity extends AppCompatActivity {

    @BindView(R.id.text_warning_network_error) TextView mTextNetworkWarning;
    @BindView(R.id.card_list) RecyclerView mListCardList;
    @BindView(R.id.text_page_number) TextView mTextPageNumber;
    @BindView(R.id.text_card_list_empty) TextView mTextListEmpty;
    @BindView(R.id.button_page_prev) Button mButtonPagePrev;
    @BindView(R.id.button_page_next) Button mButtonPageNext;
    @BindView(R.id.layout_loading_new_results) View mLayoutLoading;

    private boolean mTwoPane;
    private CardListRecyclerAdapter mCardListAdapter;

    // TODO ideally put all this into a fragment (as with MainActivity)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        View rootView = findViewById(android.R.id.content);
        setupToolbar();

        if (findViewById(R.id.card_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                CardDetailFragment fragment = CardDetailFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.card_detail_container, fragment)
                        .commit();
            }
        }

        ButterKnife.bind(this, rootView);
        initRecyclerView();
        initWarningText();
        initPagerViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView() {
        mCardListAdapter = new CardListRecyclerAdapter(view -> onCardClicked((MagicCard) view.getTag()));
        getViewModel().getCurrentCards().observe(this, this::onCardsChanged);
        mListCardList.setAdapter(mCardListAdapter);
    }

    private void onCardsChanged(List<MagicCard> cards){
        mCardListAdapter.setData(cards);
        mListCardList.scrollToPosition(0);
        showEmptyListView(cards == null || cards.isEmpty());
    }

    private void initWarningText() {
        getViewModel().getNetworkState().observe(this, state -> {
            mTextNetworkWarning.setVisibility(CurrentSelectionRepository.LoadingState.NETWORK_ERROR.equals(state) ? View.VISIBLE : View.GONE);
            mLayoutLoading.setVisibility(CurrentSelectionRepository.LoadingState.LOADING.equals(state) ? View.VISIBLE : View.GONE);
        });
    }

    private void initPagerViews() {
        getViewModel().getCurrentCardsPageCount().observe(this,
                pages -> onCardPagesChanged(getViewModel().getCurrentCardsPage().getValue(), pages)
        );
        getViewModel().getCurrentCardsPage().observe(this,
                page -> onCardPagesChanged(page, getViewModel().getCurrentCardsPageCount().getValue())
        );
    }

    @OnClick(R.id.button_page_prev)
    void onPrevPageClicked() {
        getViewModel().onPrevPage();
    }

    @OnClick(R.id.button_page_next)
    void onNextPageClicked() {
        getViewModel().onNextPage();
    }


    private void showEmptyListView(boolean show) {
        mTextListEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        mListCardList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void onCardPagesChanged(Integer current, Integer last) {
        if (current == null || last == null) {
            return;
        }
        mTextPageNumber.setText(getString(R.string.paging_label, current, last));
        mButtonPagePrev.setEnabled(current > 1);
        mButtonPageNext.setEnabled(current < last);
    }

    private void onCardClicked(MagicCard card) {
        getViewModel().onCardSelected(card);

        if (!mTwoPane) {
            AppNavigator.goToCardDetailActivity(this);
        }
    }

    private CardListViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CardListViewModel.class);
    }
}
