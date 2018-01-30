package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xue.BaseApplication;
import com.xue.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.BaseViewHolder> {

    private final int TYPE_TEXT_MSG_RIGHT = 1;

    private final int TYPE_TEXT_MSG_LEFT = 2;

    private List<IMMessage> mIMMessageList;

    public ChatListAdapter() {
    }

    public void setDataList(List<IMMessage> immessageList) {
        mIMMessageList = immessageList;
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage imMessage = mIMMessageList.get(position);
        if (imMessage.getMsgType().getValue() == MsgTypeEnum.text.getValue()) {
            if (TextUtils.equals(imMessage.getFromAccount(), BaseApplication.get().getUserId())) {
                return TYPE_TEXT_MSG_RIGHT;
            } else {
                return TYPE_TEXT_MSG_LEFT;
            }
        }

        return 0;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == TYPE_TEXT_MSG_RIGHT) {
            baseViewHolder = new TextMessageRightViewHolder(parent);
        } else if (viewType == TYPE_TEXT_MSG_LEFT) {
            baseViewHolder = new TextMessageLeftViewHolder(parent);
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.fillData(mIMMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mIMMessageList == null) {
            return 0;
        }
        return mIMMessageList.size();
    }

    public static class TextMessageLeftViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        private TextView message;

        public TextMessageLeftViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_text_left, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        public void fillData(IMMessage imMessage) {
            message.setText(imMessage.getContent());
        }
    }

    public static class TextMessageRightViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        private TextView message;

        public TextMessageRightViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_text_right, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        public void fillData(IMMessage imMessage) {
            message.setText(imMessage.getContent());
        }
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void fillData(IMMessage imMessage);
    }
}
