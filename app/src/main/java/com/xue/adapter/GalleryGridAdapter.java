package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;

import java.util.List;

public class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.BaseViewHolder> {

    private final int TYPE_PHOTO = 1;

    private final int TYPE_ADD = 2;

    private List<String> mDataList;

    private int imageWidth;

    private AddCallBack mAddCallBack;

    public GalleryGridAdapter(AddCallBack addCallBack) {
        this.mAddCallBack = addCallBack;
    }

    public void setDataList(List<String> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDataList.size()) {
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
            return new AddViewHolder(parent, imageWidth, mAddCallBack);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.e("xue", "position == " + position);
        holder.fillData(position < mDataList.size() ? mDataList.get(position) : null);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 1;
        }

        return mDataList.size() < 9 ? mDataList.size() + 1 : mDataList.size();
    }

    public static class AddViewHolder extends BaseViewHolder {

        private AddCallBack addCallBack;

        public AddViewHolder(ViewGroup itemView, int imageWidth, final AddCallBack addCallBack) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_gallery_add, itemView, false), imageWidth);
            this.addCallBack = addCallBack;
        }

        @Override
        protected void findView(int imageWidth) {
            super.findView(imageWidth);
            UITool.zoomView(imageWidth, imageWidth, itemView);
        }

        @Override
        public void fillData(String imageUri) {
            super.fillData(imageUri);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addCallBack != null) {
                        addCallBack.onClickAdd();
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
        public void fillData(String imageUri) {
            Log.e("xue", "imageUri = " + imageUri);
            ImageCacheMannager.loadImage(itemView.getContext(), imageUri, photo, false);
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView, int imageWidth) {
            super(itemView);
            findView(imageWidth);
        }

        protected void findView(int imageWidth) {
        }

        public void fillData(String imageUri) {
        }
    }

    public interface AddCallBack {
        public void onClickAdd();
    }
}
