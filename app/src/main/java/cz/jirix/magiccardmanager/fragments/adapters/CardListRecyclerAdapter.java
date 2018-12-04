package cz.jirix.magiccardmanager.fragments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.model.MagicCard;

public class CardListRecyclerAdapter extends RecyclerView.Adapter<CardListRecyclerAdapter.ViewHolder> {

    private int mPreviousCount;
    private List<MagicCard> mData;
    private View.OnClickListener mOnClickListener;

    public CardListRecyclerAdapter(View.OnClickListener listener) {
        mData = new ArrayList<>();
        mPreviousCount = 0;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(position + 1 + mPreviousCount));
        holder.mContentView.setText(mData.get(position).getName());

        holder.itemView.setTag(mData.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<MagicCard> data, int previousResultCount) {
        mData = data;
        mPreviousCount = previousResultCount;
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
