package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elianshang.tools.UITool;
import com.xue.R;
import com.xue.bean.User;
import com.xue.bean.UserConfigInfo;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.bean.UserList;
import com.xue.imagecache.ImageCacheMannager;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ViewHolder> {

    private UserList dataList = null;

    private int imageWidth = 0;

    private AdapterOnItemClickCallback<User> callback;

    public void setDataList(UserList dataList) {
        this.dataList = dataList;
    }

    public void setCallback(AdapterOnItemClickCallback<User> callback) {
        this.callback = callback;
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
        holder.fill(dataList.get(position), callback);
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

        private TextView signature;

        private TextView fee;

        private TextView vip;

        public ViewHolder(ViewGroup itemView, int imageWidth) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_home_list_user, itemView, false));
            findView(imageWidth);
        }

        private void findView(int imageWidth) {
            UITool.zoomView(imageWidth, imageWidth, itemView);

            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            signature = itemView.findViewById(R.id.signature);
            fee = itemView.findViewById(R.id.fee);
            vip = itemView.findViewById(R.id.vip);
        }

        protected void fill(final User user, final AdapterOnItemClickCallback<User> callback) {
            UserDetailInfo userDetailInfo = user.getUserDetailInfo();
            if (userDetailInfo != null) {
                name.setText(userDetailInfo.getRealName());
                if (!TextUtils.isEmpty(userDetailInfo.getCover())) {
                    ImageCacheMannager.loadImage(photo.getContext(), userDetailInfo.getCover(), photo, false);
                } else if (!TextUtils.isEmpty(userDetailInfo.getProfile())) {
                    ImageCacheMannager.loadImage(photo.getContext(), userDetailInfo.getProfile(), photo, false);
                } else {
                    ImageCacheMannager.loadImage(photo.getContext(), null, photo, false);
                }
            } else {
                name.setText("信息不全");
                ImageCacheMannager.loadImage(photo.getContext(), null, photo, false);
            }

            UserConfigInfo userConfigInfo = user.getUserConfigInfo();
            if (userConfigInfo != null) {
                fee.setText(userConfigInfo.getFeeDefault() + "钻石/分钟");
            }

            UserExpertInfo userExpertInfo = user.getUserExpertInfo();
            String signString = null;
            String feeString = null;
            if (userExpertInfo != null) {
                signString = userExpertInfo.getSignature();
                feeString = userExpertInfo.getServiceFee() > 0 ? userExpertInfo.getServiceFee() + "钻石/分钟" : null;
            }

            if (!TextUtils.isEmpty(signString)) {
                signature.setText(signString);
            } else {
                signature.setText("TA很懒什么也没写");
            }

            if (!TextUtils.isEmpty(feeString)) {
                fee.setText(feeString);
            }

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemClick(user, view);
                    }
                }
            });

        }
    }
}