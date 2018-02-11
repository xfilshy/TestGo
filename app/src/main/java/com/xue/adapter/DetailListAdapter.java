package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.DetailHelper;
import com.xue.bean.OrderCommentList;
import com.xue.bean.OrderScoreMap;
import com.xue.bean.UserEducationInfo;
import com.xue.bean.UserFriendInfo;
import com.xue.bean.UserWorkInfo;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.support.view.SimpleProgressBar;


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
     * 关注
     */
    private int TYPE_FOLLOW = 8;

    private DetailHelper mData;

    private AdapterOnItemClickCallback<DetailHelper.ItemType> callback;

    public void setData(DetailHelper data) {
        this.mData = data;
    }

    public void setCallback(AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
        this.callback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == TYPE_DESCRIBE) {
            baseViewHolder = new DescribeViewHolder(parent);
        } else if (viewType == TYPE_GALLERY) {
            baseViewHolder = new GalleryViewHolder(parent);
        } else if (viewType == TYPE_FOLLOW) {
            baseViewHolder = new FollowViewHolder(parent);
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
            return TYPE_FOLLOW;
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
        holder.fill(mData, mData.getItemType(position), callback);
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

        public abstract void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback);
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

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
            if (itemType == DetailHelper.ItemType.Intro) {
                intro.setText(detailHelper.getIntro());
            }
        }
    }

    public static class GalleryViewHolder extends BaseViewHolder {

        private ImageView image1;

        private ImageView image2;

        private ImageView image3;

        public GalleryViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_gallery, itemView, false));
            findView();
        }

        private void findView() {
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
        }

        public void fill(DetailHelper detailHelper, final DetailHelper.ItemType itemType, final AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
            String[] pics = detailHelper.getGallery();
            if (pics == null) {
                image1.setImageDrawable(null);
                image2.setImageDrawable(null);
                image3.setImageDrawable(null);
                image1.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(pics[0])) {
                    ImageCacheMannager.loadImage(itemView.getContext(), pics[0], image1, false);
                    image1.setVisibility(View.VISIBLE);
                } else {
                    image1.setImageDrawable(null);
                    image1.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(pics[1])) {
                    ImageCacheMannager.loadImage(itemView.getContext(), pics[1], image2, false);
                    image2.setVisibility(View.VISIBLE);
                } else {
                    image2.setImageDrawable(null);
                    image2.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(pics[2])) {
                    ImageCacheMannager.loadImage(itemView.getContext(), pics[2], image3, false);
                    image3.setVisibility(View.VISIBLE);
                } else {
                    image3.setImageDrawable(null);
                    image3.setVisibility(View.GONE);
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(itemType, v);
                    }
                }
            });
        }
    }

    public static class FollowViewHolder extends BaseViewHolder {

        private TextView count;

        public FollowViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_follow, itemView, false));
            findView();
        }

        private void findView() {
            count = itemView.findViewById(R.id.count);
        }

        public void fill(DetailHelper detailHelper, final DetailHelper.ItemType itemType, final AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
            UserFriendInfo userFriendInfo = detailHelper.getFollow();
            if (userFriendInfo != null) {
                count.setText(userFriendInfo.getFansCount() + "人");
            }
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

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
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

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
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

        public void fill(DetailHelper detailHelper, final DetailHelper.ItemType itemType, final AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(itemType, v);
                    }
                }
            });
        }
    }

    public static class MarkViewHolder extends BaseViewHolder {

        private TextView avg;

        private TextView total;

        private SimpleProgressBar progress1;

        private SimpleProgressBar progress2;

        private SimpleProgressBar progress3;

        private SimpleProgressBar progress4;

        private SimpleProgressBar progress5;

        public MarkViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_mark, itemView, false));
            findView();
        }

        private void findView() {
            avg = itemView.findViewById(R.id.avg);
            total = itemView.findViewById(R.id.total);
            progress1 = itemView.findViewById(R.id.progress1);
            progress2 = itemView.findViewById(R.id.progress2);
            progress3 = itemView.findViewById(R.id.progress3);
            progress4 = itemView.findViewById(R.id.progress4);
            progress5 = itemView.findViewById(R.id.progress5);
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
            OrderScoreMap orderScoreMap = detailHelper.getMark();
            avg.setText(orderScoreMap.getAvgScore());
            total.setText(orderScoreMap.getTotal() + "条评论");
            progress1.setProgress((orderScoreMap.get("1") / (float) orderScoreMap.getTotal()));
            progress2.setProgress((orderScoreMap.get("2") / (float) orderScoreMap.getTotal()));
            progress3.setProgress((orderScoreMap.get("3") / (float) orderScoreMap.getTotal()));
            progress4.setProgress((orderScoreMap.get("4") / (float) orderScoreMap.getTotal()));
            progress5.setProgress((orderScoreMap.get("5") / (float) orderScoreMap.getTotal()));
        }
    }

    public static class CommentViewHolder extends BaseViewHolder {

        private ImageView profile;

        private TextView userName;

        private TextView content;

        private TextView date;

        private RatingBar score;

        public CommentViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_detail_list_comment, itemView, false));
            findView();
        }

        private void findView() {
            profile = itemView.findViewById(R.id.profile);
            userName = itemView.findViewById(R.id.userName);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);
        }

        public void fill(DetailHelper detailHelper, DetailHelper.ItemType itemType, AdapterOnItemClickCallback<DetailHelper.ItemType> callback) {
            OrderCommentList.Comment comment = detailHelper.getComment();
            ImageCacheMannager.loadImage(itemView.getContext(), comment.getFromProfile(), profile, true);
            userName.setText(comment.getFromUserName());
            content.setText(comment.getContent());
            date.setText(comment.getCreatedAt());
            score.setRating(comment.getScore());
        }
    }
}