package cz.jirix.magiccardmanager.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.model.MagicColor;

public class MagicColorsSpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MagicColor> mData;

    public MagicColorsSpinnerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    public void setData(List<MagicColor> colors) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        MagicColor color = (MagicColor) getItem(i);
        ((TextView) convertView).setText(color.getName());
        return convertView;
    }

    public int getItemPosition(String color) {
        for(int i=0;i<mData.size();i++){
            MagicColor item = mData.get(i);
            if(item.getName().equals(color)){
                return i;
            }
        }
        return -1;
    }
}
