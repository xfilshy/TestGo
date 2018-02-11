package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.bean.MomentInfoList;
import com.xue.imagecache.ImageCacheMannager;

import java.util.List;

public class MyGalleryGridAdapter extends RecyclerView.Adapter<MyGalleryGridAdapter.BaseViewHolder> {

    private final int TYPE_PHOTO = 1;

    private final int TYPE_ADD = 2;

    private List<MomentInfoList.MomentRes> mDataList;

    private int imageWidth;

    private AdapterOnItemClickCallback<MomentInfoList.MomentRes> callback;

    private AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> longClickCallback;

    public void setDataList(List<MomentInfoList.MomentRes> dataList) {
        this.mDataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<MomentInfoList.MomentRes> callback) {
        this.callback = callback;
    }

    public void setLongClickCallback(AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> longClickCallback) {
        this.longClickCallback = longClickCallback;
    }

    @Override
    public int getItemViewType(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        if (position < size) {
            return TYPE_PHOTO;
        } else {
            return TYPE_ADD;
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        if (viewType == TYPE_PHOTO) {
            return new PhotoViewHolder(parent, imageWidth);
        } else if (viewType == TYPE_ADD) {
            return new AddViewHolder(parent, imageWidth);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        holder.fillData(position < size ? mDataList.get(position) : null, callback, longClickCallback);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 1;
        }

        return mDataList.size() < 9 ? mDataList.size() + 1 : mDataList.size();
    }

    public static class AddViewHolder extends BaseViewHolder {

        public AddViewHolder(ViewGroup itemView, int imageWidth) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_gallery_add, itemView, false), imageWidth);
        }

        @Override
        protected void findView(int imageWidth) {
            super.findView(imageWidth);
            UITool.zoomView(imageWidth, imageWidth, itemView);
        }

        @Override
        public void fillData(MomentInfoList.MomentRes momentRes, final AdapterOnItemClickCallback<MomentInfoList.MomentRes> callback, AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> longClickCallback) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(null, itemView);
                    }
                }
            });
        }
    }

    public static class PhotoViewHolder extends BaseViewHolder {

        private ImageView photo;

        public PhotoViewHolder(ViewGroup itemView, int imageWidth) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_gallery_photo, itemView, false), imageWidth);
        }

        @Override
        protected void findView(int imageWidth) {
            photo = itemView.findViewById(R.id.photo);

            UITool.zoomView(imageWidth, imageWidth, photo);
        }

        @Override
        public void fillData(final MomentInfoList.MomentRes momentRes, final AdapterOnItemClickCallback<MomentInfoList.MomentRes> callback, final AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> longClickCallback) {
            ImageCacheMannager.loadImage(itemView.getContext(), momentRes.getUrl(), photo, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(momentRes, photo);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickCallback != null) {
                        longClickCallback.onItemLongClick(momentRes, photo);
                    }
                    return true;
                }
            });
        }
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView, int imageWidth) {
            super(itemView);
            findView(imageWidth);
        }

        protected void findView(int imageWidth) {
        }

        public abstract void fillData(MomentInfoList.MomentRes momentRes, AdapterOnItemClickCallback<MomentInfoList.MomentRes> callback, AdapterOnItemLongClickCallback<MomentInfoList.MomentRes> longClickCallback);
    }
}
