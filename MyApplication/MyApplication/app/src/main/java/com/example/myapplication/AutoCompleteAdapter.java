package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> {

    private List<String> mlistData;

    public AutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
    }

    public void setData(List<String> list) {
        mlistData.clear();
        mlistData.addAll(list);
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mlistData.get(position);
    }
}
