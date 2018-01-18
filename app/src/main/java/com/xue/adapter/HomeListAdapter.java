package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.bean.UserMinor;
import com.xue.imagecache.ImageCacheMannager;

import java.util.List;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private List<UserMinor> dataList = null;

    public HomeListAdapter() {

    }

    public HomeListAdapter(List<UserMinor> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<UserMinor> dataList) {
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fill(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        private ImageView photo;

        private TextView motto;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_home_list, itemView, false));
            findView();
        }

        private void findView() {
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            motto = itemView.findViewById(R.id.motto);

            UITool.zoomViewByWidth(160, 160, photo);

        }

        protected void fill(UserMinor userMinor) {
            name.setText("路伟");
            ImageCacheMannager.loadImage(photo.getContext(), R.drawable.photo_test, photo);
            motto.setText("就是赚钱");
        }
    }
}