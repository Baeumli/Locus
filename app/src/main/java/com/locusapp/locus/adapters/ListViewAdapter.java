package com.locusapp.locus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.locusapp.locus.models.Bounty;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<Bounty> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListViewAdapter(List<Bounty> listData, Context aContext) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
