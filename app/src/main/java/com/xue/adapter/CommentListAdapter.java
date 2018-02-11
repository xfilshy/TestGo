package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xue.R;
import com.xue.bean.OrderCommentInfo;
import com.xue.bean.OrderCommentList;
import com.xue.bean.OrderScoreMap;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.support.view.SimpleProgressBar;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.BaseViewHolder> {

    private int TypeMark = 1;

    private int TypeComment = 2;

    private OrderCommentInfo mDataList;

    public void setDataList(OrderCommentInfo dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TypeMark;
        }

        return TypeComment;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == TypeMark) {
            baseViewHolder = new MarkViewHolder(parent);
        } else if (viewType == TypeComment) {
            baseViewHolder = new CommentViewHolder(parent);
        }

        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof MarkViewHolder) {
            holder.fill(mDataList.getOrderScoreMap());
        } else if (holder instanceof CommentViewHolder) {
            holder.fill(mDataList.getOrderCommentList().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        if (mDataList.getOrderCommentList() == null) {
            return 0;
        }

        return mDataList.getOrderCommentList().size() + 1;
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void fill(Object data);
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

        public void fill(Object data) {
            OrderScoreMap orderScoreMap = (OrderScoreMap) data;
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

        public void fill(Object data) {
            OrderCommentList.Comment comment = (OrderCommentList.Comment) data;
            ImageCacheMannager.loadImage(itemView.getContext(), comment.getFromProfile(), profile, true);
            userName.setText(comment.getFromUserName());
            content.setText(comment.getContent());
            date.setText(comment.getCreatedAt());
            score.setRating(comment.getScore());
        }
    }
}
