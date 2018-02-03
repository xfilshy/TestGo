package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.xue.R;
import com.xue.bean.CityList;

import java.util.HashMap;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private CityList mDataList;

    private OnItemClickCallback mCallback;

    private HashMap<String, Integer> ids = new HashMap();

    {
        ids.put("A", 1);
        ids.put("B", 2);
        ids.put("C", 3);
        ids.put("D", 4);
        ids.put("E", 5);
        ids.put("F", 6);
        ids.put("G", 7);
        ids.put("H", 8);
        ids.put("I", 9);
        ids.put("J", 10);
        ids.put("K", 11);
        ids.put("L", 12);
        ids.put("M", 13);
        ids.put("N", 14);
        ids.put("O", 15);
        ids.put("P", 16);
        ids.put("Q", 17);
        ids.put("R", 18);
        ids.put("S", 19);
        ids.put("T", 20);
        ids.put("U", 21);
        ids.put("V", 22);
        ids.put("W", 23);
        ids.put("X", 24);
        ids.put("Y", 25);
        ids.put("Z", 26);
    }

    public void setCallback(OnItemClickCallback callback) {
        this.mCallback = callback;
    }

    public void setDataList(CityList dataList) {
        this.mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, mCallback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fill(mDataList.get(position));
    }

    @Override
    public long getHeaderId(int position) {
        Integer i = ids.get(mDataList.get(position).getInitial());
        if (i == null) {
            return -1;
        } else {
            return i;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_header, parent, false)) {
        };

        return viewHolder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(mDataList.get(position).getInitial());
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityTextView;

        private OnItemClickCallback clickCallback;

        public ViewHolder(ViewGroup itemView, OnItemClickCallback callback) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_city_city, itemView, false));
            this.clickCallback = callback;
            cityTextView = this.itemView.findViewById(R.id.city);
        }

        public void fill(final CityList.City city) {
            cityTextView.setText(city.getName());
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (clickCallback != null) {
                        clickCallback.onItemClick(city);
                    }
                }
            });
        }
    }

    public interface OnItemClickCallback {
        public void onItemClick(CityList.City city);
    }
}
