package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.BaseViewHolder> {

    /**
     * 独白
     */
    private int TYPE_DESCRIBE = 1;

    /**
     * 小标题
     */
    private int TYPE_TITLE = 2;

    /**
     * 用户信息 键值对
     */
    private int TYPE_INFO = 3;

    /**
     * 小标题
     */
    private int TYPE_COMMENT_TITLE = 4;

    /**
     * 评分总览
     */
    private int TYPE_MARK = 5;

    /**
     * 评论
     */
    private int TYPE_COMMENT = 6;

    /**
     * 相册
     */
    private int TYPE_GALLERY = 7;


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == TYPE_DESCRIBE) {
            baseViewHolder = new DescribeViewHolder(parent);
        } else if (viewType == TYPE_GALLERY) {
            baseViewHolder = new GalleryViewHolder(parent);
        } else if (viewType == TYPE_TITLE) {
            baseViewHolder = new TitleViewHolder(parent);
        } else if (viewType == TYPE_INFO) {
            baseViewHolder = new InfoViewHolder(parent);
        } else if (viewType == TYPE_COMMENT_TITLE) {
            baseViewHolder = new CommentTitleViewHolder(parent);
        } else if (viewType == TYPE_MARK) {
            baseViewHolder = new MarkViewHolder(parent);
        } else if (viewType == TYPE_COMMENT) {
            baseViewHolder = new CommentViewHolder(parent);
        }

        return baseViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DESCRIBE;
        } else if (position == 1) {
            return TYPE_GALLERY;
        } else if (position == 2) {
            return TYPE_TITLE;
        } else if (position > 2 && position < 7) {
            return TYPE_INFO;
        } else if (position == 7) {
            return TYPE_COMMENT_TITLE;
        } else if (position == 8) {
            return TYPE_MARK;
        } else {
            return TYPE_COMMENT;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).fillData();
        }
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DescribeViewHolder extends BaseViewHolder {

        public DescribeViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_describe, itemView, false));
        }
    }

    public static class GalleryViewHolder extends BaseViewHolder {

        public GalleryViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_gallery, itemView, false));
        }
    }

    public static class TitleViewHolder extends BaseViewHolder {

        public TitleViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_title, itemView, false));
        }
    }

    public static class InfoViewHolder extends BaseViewHolder {

        public InfoViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_info, itemView, false));
        }
    }

    public static class CommentTitleViewHolder extends BaseViewHolder {

        public CommentTitleViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_comment_title, itemView, false));
        }
    }

    public static class MarkViewHolder extends BaseViewHolder {

        public MarkViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_mark, itemView, false));
        }
    }

    public static class CommentViewHolder extends BaseViewHolder {

        private ImageView photo;

        public CommentViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_comment, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
        }

        public void fillData() {
            ImageCacheMannager.loadImage(itemView.getContext(), R.drawable.photo_test, photo, true);
        }
    }
}