package cz.jirix.magiccardmanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.viewModel.CardDetailViewModel;


public class CardDetailFragment extends Fragment {

    @BindView(R.id.text_error_image_download_failed) TextView mTextErrorNoImage;
    @BindView(R.id.progress_image_loading) ProgressBar mProgressLoading;
    @BindView(R.id.image_card) ImageView mImageCard;


    private static final int STATE_LOADING = 1;
    private static final int STATE_NO_IMAGE = 2;
    private static final int STATE_SUCCESS = 3;
    private static final int STATE_ERROR = 4;

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
            showLayout(STATE_NO_IMAGE);
            return;
        }

        showLayout(STATE_LOADING);
        Picasso.get().load(card.getImageUrl())
                .error(android.R.drawable.stat_notify_error)
                .into(mImageCard, new Callback() {
                    @Override
                    public void onSuccess() {
                        showLayout(STATE_SUCCESS);
                    }

                    @Override
                    public void onError(Exception e) {
                        showLayout(STATE_ERROR);
                    }
                });
    }

    private void showLayout(int state){
        mTextErrorNoImage.setVisibility(state == STATE_NO_IMAGE || state == STATE_ERROR? View.VISIBLE : View.GONE);
        mTextErrorNoImage.setText(state == STATE_NO_IMAGE ? R.string.can_t_find_an_image_for_this_card : R.string.image_download_failed);

        mProgressLoading.setVisibility(state == STATE_LOADING ? View.VISIBLE : View.GONE);
        mImageCard.setVisibility(state == STATE_SUCCESS ? View.VISIBLE : View.GONE);
    }

    public CardDetailViewModel getViModel() {
        return ViewModelProviders.of(this).get(CardDetailViewModel.class);
    }
}
