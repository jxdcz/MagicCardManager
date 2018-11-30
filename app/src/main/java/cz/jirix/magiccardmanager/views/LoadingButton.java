package cz.jirix.magiccardmanager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cz.jirix.magiccardmanager.R;

/**
 * A button with a built in progressBar / loader
 */
public class LoadingButton extends FrameLayout {

    private boolean mIsLoading;
    private Button mButton;
    private ProgressBar mProgressBar;
    private OnClickListener mOnClickListener;

    public LoadingButton(Context context) {
        super(context);
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.loading_button, this, true);
        mIsLoading = false;
        mButton = rootView.findViewById(R.id.button);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        mButton.setOnClickListener(view -> {
            if (mOnClickListener != null && !mIsLoading) {
                mOnClickListener.onClick(view);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void showProgress(boolean show) {
        mIsLoading = show;
        mButton.setVisibility(show ? GONE : VISIBLE);
        mProgressBar.setVisibility(show ? VISIBLE : GONE);
    }
}
