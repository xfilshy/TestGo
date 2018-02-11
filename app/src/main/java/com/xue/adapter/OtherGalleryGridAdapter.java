package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;

import java.util.List;

public class OtherGalleryGridAdapter extends RecyclerView.Adapter<OtherGalleryGridAdapter.PhotoViewHolder> {

    private List<String> mDataList;

    private int imageWidth;

    private AdapterOnItemClickCallback<String> callback;

    public void setDataList(List<String> dataList) {
        this.mDataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<String> callback) {
        this.callback = callback;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (imageWidth == 0) {
            int sw = UITool.getScreenWidth(parent.getContext());
            int dx = UITool.dipToPx(parent.getContext(), 3) * 3 + UITool.dipToPx(parent.getContext(), 16) * 2;
            int mo = (sw - dx) % 4;
            if (mo == 0) {
                imageWidth = (sw - dx) / 4;
            } else {
                imageWidth = (sw - dx + 4 - mo) / 4;
            }
        }

        return new PhotoViewHolder(parent , imageWidth);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.fillData(mDataList.get(position), callback);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }

        return mDataList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;

        public PhotoViewHolder(ViewGroup itemView, int imageWidth) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_gallery_photo, itemView, false));
            findView(imageWidth);
        }

        protected void findView(int imageWidth) {
            photo = itemView.findViewById(R.id.photo);

            UITool.zoomView(imageWidth, imageWidth, photo);
        }

        public void fillData(final String url, final AdapterOnItemClickCallback<String> callback) {
            ImageCacheMannager.loadImage(itemView.getContext(), url, photo, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(url, photo);
                    }
                }
            });

        }
    }
}
