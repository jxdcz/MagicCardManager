package cz.jirix.magiccardmanager.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleArrayWithDefValueAdapter<T> extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<T> mData;
    private String mDefaultValue;


    public SimpleArrayWithDefValueAdapter(Context context, String defaultValue) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        mDefaultValue = defaultValue;
    }

    public void setData(List<T> items) {
        mData = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    @Override
    public T getItem(int i) {
        return i == 0 ? null : mData.get(i - 1);
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

        Object item = getItem(i);
        ((TextView) convertView).setText(item == null ? mDefaultValue : item.toString());
        return convertView;
    }

    public int getItemPosition(String value) {
        for (int i = 0; i < getCount(); i++) {
            Object item = getItem(i);
            if (item != null && item.toString().equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
