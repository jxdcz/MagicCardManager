package cz.jirix.magiccardmanager.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.fragments.CardDetailFragment;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.viewModel.CardListViewModel;

public class CardListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    private CardListRecyclerAdapter mCardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        setupToolbar();

        if (findViewById(R.id.card_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.card_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
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

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mCardListAdapter = new CardListRecyclerAdapter(this, mTwoPane);
        getViewModel().getCurrentCards().observe(this, magicCards -> mCardListAdapter.setData(magicCards));
        recyclerView.setAdapter(mCardListAdapter);
    }


    private CardListViewModel getViewModel(){
        return ViewModelProviders.of(this).get(CardListViewModel.class);
    }

    public static class CardListRecyclerAdapter extends RecyclerView.Adapter<CardListRecyclerAdapter.ViewHolder> {

        private final CardListActivity mParentActivity;

        private List<MagicCard> mData;
        private boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MagicCard item = (MagicCard) view.getTag();
                if (mTwoPane) {
                    /*
                    possibly useless
                    Bundle arguments = new Bundle();
                    CardDetailFragment fragment = new CardDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.card_detail_container, fragment)
                            .commit();
                            */
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CardDetailActivity.class);
                    context.startActivity(intent);
                }
            }
        };

        CardListRecyclerAdapter(CardListActivity parent, boolean twoPane) {
            mData = new ArrayList<>();
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(position));
            holder.mContentView.setText(mData.get(position).getName());

            holder.itemView.setTag(mData.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setData(List<MagicCard> data) {
            mData = data;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
