package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.DetailHelper;
import com.xue.bean.UserEducationInfo;
import com.xue.bean.UserWorkInfo;
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

    /**
     * 收藏
     */
    private int TYPE_FAVORITE = 8;

    private DetailHelper mData;

    public void setData(DetailHelper data) {
        this.mData = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == TYPE_DESCRIBE) {
            baseViewHolder = new DescribeViewHolder(parent);
        } else if (viewType == TYPE_GALLERY) {
            baseViewHolder = new GalleryViewHolder(parent);
        } else if (viewType == TYPE_FAVORITE) {
            baseViewHolder = new FavoriteiewHolder(parent);
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
        if (mData.getItemType(position) == DetailHelper.ItemType.Intro) {
            return TYPE_DESCRIBE;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Follow) {
            return TYPE_FAVORITE;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Gallery) {
            return TYPE_GALLERY;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.InfoTitle) {
            return TYPE_TITLE;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Education) {
            return TYPE_INFO;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Work) {
            return TYPE_INFO;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.HomeTown) {
            return TYPE_INFO;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.CommentTitle) {
            return TYPE_COMMENT_TITLE;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Mark) {
            return TYPE_MARK;
        } else if (mData.getItemType(position) == DetailHelper.ItemType.Comment) {
            return TYPE_COMMENT;
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.fill(mData, mData.getItemType(position));
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.getCount();
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType);
    }

    public static class DescribeViewHolder extends BaseViewHolder {

        private TextView intro;

        public DescribeViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_describe, itemView, false));
            findView();
        }

        private void findView() {
            intro = itemView.findViewById(R.id.intro);
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
            if (itemType == DetailHelper.ItemType.Intro) {
                intro.setText(detailHelper.getIntro());
            }
        }
    }

    public static class GalleryViewHolder extends BaseViewHolder {

        public GalleryViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_gallery, itemView, false));
        }

        private void findView() {
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
        }
    }

    public static class FavoriteiewHolder extends BaseViewHolder {

        public FavoriteiewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_favorite, itemView, false));
        }

        private void findView() {
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
        }
    }

    public static class TitleViewHolder extends BaseViewHolder {

        private TextView title;

        public TitleViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_title, itemView, false));
            findView();
        }

        private void findView() {
            title = itemView.findViewById(R.id.title);
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
            if (itemType == DetailHelper.ItemType.InfoTitle) {
                title.setText("我的信息");
            }
        }
    }

    public static class InfoViewHolder extends BaseViewHolder {

        private TextView key;

        private TextView value;

        public InfoViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_info, itemView, false));
            findView();
        }

        private void findView() {
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
            if (itemType == DetailHelper.ItemType.Work) {
                UserWorkInfo.Work work = detailHelper.getWork();
                key.setText("工作经历");
                value.setText(work.getCompanyName() + "  " + work.getIndustryName() + "\n" + work.getPositionName() + "  " + work.getDirectionName());
            } else if (itemType == DetailHelper.ItemType.Education) {
                UserEducationInfo.Education education = detailHelper.getEducation();
                key.setText("工作经历");
                value.setText(education.getSchoolName() + "\n" + education.getMajorName() + "  " + education.getAcademicName());
            } else if (itemType == DetailHelper.ItemType.HomeTown) {
                key.setText("家乡");
                value.setText(detailHelper.getHomeTown());
            }
        }
    }

    public static class CommentTitleViewHolder extends BaseViewHolder {

        public CommentTitleViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_comment_title, itemView, false));
        }

        private void findView() {
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
        }
    }

    public static class MarkViewHolder extends BaseViewHolder {

        public MarkViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_mark, itemView, false));
        }

        private void findView() {
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
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

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType) {
            ImageCacheMannager.loadImage(itemView.getContext(), R.drawable.photo_test, photo, true);

        }
    }
}