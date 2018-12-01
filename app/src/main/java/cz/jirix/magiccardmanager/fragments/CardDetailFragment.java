package cz.jirix.magiccardmanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.viewModel.CardDetailViewModel;


public class CardDetailFragment extends Fragment {

    @BindView(R.id.text_error_no_image_url) TextView mTextErrorNoImage;
    @BindView(R.id.image_card) ImageView mImageCard;

    public static CardDetailFragment newInstance(){
        return new CardDetailFragment();
    }

    public CardDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_detail, container, false);
        ButterKnife.bind(this, rootView);

        initImageView();

        return rootView;
    }

    private void initImageView(){
        getViModel().getCurrentCard().observe(this, this::loadImage);
    }

    private void loadImage(MagicCard card){
        if(card == null || card.getImageUrl() == null){
            showError(true);
            return;
        }

        showError(false);
        Picasso.get().load(card.getImageUrl())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(mImageCard);
    }

    private void showError(boolean show){
        mTextErrorNoImage.setVisibility(show ? View.VISIBLE : View.GONE);
        mImageCard.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public CardDetailViewModel getViModel() {
        return ViewModelProviders.of(this).get(CardDetailViewModel.class);
    }
}
