package cz.jirix.magiccardmanager.fragments.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cz.jirix.magiccardmanager.model.MagicSet;

public class MagicSetsSpinnerAdapter extends ArrayAdapter<MagicSet> {

    private LayoutInflater mInflater;

    public MagicSetsSpinnerAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        mInflater = LayoutInflater.from(context);

    }

    public void setData(List<MagicSet> sets) {
        clear();
        addAll(sets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int i, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        MagicSet set = getItem(i);
        if(set != null) {
            ((TextView) convertView).setText(set.getName());
        }
        return convertView;
    }
}
