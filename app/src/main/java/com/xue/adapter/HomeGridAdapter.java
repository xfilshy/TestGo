package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.bean.UserMinor;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.ui.activity.DetailActivity;

import java.util.List;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ViewHolder> {

    private List<UserMinor> dataList = null;

    private int imageWidth = 0;

    public HomeGridAdapter() {
    }

    public HomeGridAdapter(List<UserMinor> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<UserMinor> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (imageWidth == 0) {
            int sw = UITool.getScreenWidth(parent.getContext());
            int dx = UITool.dipToPx(parent.getContext(), 3);
            if ((sw - dx) % 2 == 0) {
                imageWidth = (sw - dx) / 2;
            } else {
                imageWidth = (sw - dx + 1) / 2;
            }
        }
        return new ViewHolder(parent, imageWidth);
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

        private TextView position;

        private TextView price;

        private TextView vip;

        public ViewHolder(ViewGroup itemView, int imageWidth) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_home_list_user, itemView, false));
            findView(imageWidth);
        }

        private void findView(int imageWidth) {
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            position = itemView.findViewById(R.id.position);
            price = itemView.findViewById(R.id.price);
            vip = itemView.findViewById(R.id.vip);
            UITool.zoomView(imageWidth, imageWidth, itemView);

        }

        protected void fill(final UserMinor userMinor) {
            name.setText(userMinor.getUserBase().getCellphone());
            ImageCacheMannager.loadImage(photo.getContext(), R.drawable.photo_test, photo, false);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (userMinor != null) {
                        DetailActivity.launch(view.getContext() , userMinor.getUserBase().getId());
                    }
                }
            });
        }
    }
}