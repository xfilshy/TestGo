package com.xue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.netease.NeteaseUserInfoCache;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.BaseViewHolder> {

    private final int TYPE_TEXT_MSG_RIGHT = 1;

    private final int TYPE_TEXT_MSG_LEFT = 2;

    private final int TYPE_AUDIO_MSG_RIGHT = 3;

    private final int TYPE_AUDIO_MSG_LEFT = 4;

    private final int TYPE_VIDEO_MSG_RIGHT = 5;

    private final int TYPE_VIDEO_MSG_LEFT = 6;

    private final int TYPE_AVCHAT_MSG_RIGHT = 7;

    private final int TYPE_AVCHAT_MSG_LEFT = 8;

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
            if (imMessage.getDirect() == MsgDirectionEnum.Out) {
                return TYPE_TEXT_MSG_RIGHT;
            } else {
                return TYPE_TEXT_MSG_LEFT;
            }
        } else if (imMessage.getMsgType().getValue() == MsgTypeEnum.audio.getValue()) {
            if (imMessage.getDirect() == MsgDirectionEnum.Out) {
                return TYPE_AUDIO_MSG_RIGHT;
            } else {
                return TYPE_AUDIO_MSG_LEFT;
            }
        } else if (imMessage.getMsgType().getValue() == MsgTypeEnum.video.getValue()) {
            if (imMessage.getDirect() == MsgDirectionEnum.Out) {
                return TYPE_VIDEO_MSG_RIGHT;
            } else {
                return TYPE_VIDEO_MSG_LEFT;
            }
        } else if (imMessage.getMsgType().getValue() == MsgTypeEnum.avchat.getValue()) {
            if (imMessage.getDirect() == MsgDirectionEnum.Out) {
                return TYPE_AVCHAT_MSG_RIGHT;
            } else {
                return TYPE_AVCHAT_MSG_LEFT;
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
        } else if (viewType == TYPE_AUDIO_MSG_RIGHT) {
            baseViewHolder = new AudioMessageRightViewHolder(parent);
        } else if (viewType == TYPE_AUDIO_MSG_LEFT) {
            baseViewHolder = new AudioMessageLeftViewHolder(parent);
        } else if (viewType == TYPE_VIDEO_MSG_RIGHT) {
            baseViewHolder = new VideoMessageRightViewHolder(parent);
        } else if (viewType == TYPE_VIDEO_MSG_LEFT) {
            baseViewHolder = new VideoMessageLeftViewHolder(parent);
        } else if (viewType == TYPE_AVCHAT_MSG_RIGHT) {
            baseViewHolder = new AVChatMessageRightViewHolder(parent);
        } else if (viewType == TYPE_AVCHAT_MSG_LEFT) {
            baseViewHolder = new AVChatMessageLeftViewHolder(parent);
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

        private ImageView profile;

        private TextView account;

        private TextView message;

        public TextMessageLeftViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_text_left, itemView, false));
            findView();
        }

        private void findView() {
            profile = itemView.findViewById(R.id.profile);
            account = itemView.findViewById(R.id.account);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        public void fillData(IMMessage imMessage) {
            NimUserInfo nimUserInfo = NeteaseUserInfoCache.get().getUserInfo(imMessage.getFromAccount());
            if (nimUserInfo != null) {
                ImageCacheMannager.loadImage(itemView.getContext(), nimUserInfo.getAvatar(), profile, true);
            }
            account.setText(imMessage.getFromNick());
            message.setText(imMessage.getContent());
        }
    }

    public static class TextMessageRightViewHolder extends BaseViewHolder {

        private ImageView profile;

        private TextView account;

        private TextView message;

        public TextMessageRightViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_text_right, itemView, false));
            findView();
        }

        private void findView() {
            profile = itemView.findViewById(R.id.profile);
            account = itemView.findViewById(R.id.account);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        public void fillData(IMMessage imMessage) {
            NimUserInfo nimUserInfo = NeteaseUserInfoCache.get().getUserInfo(imMessage.getFromAccount());
            if (nimUserInfo != null) {
                ImageCacheMannager.loadImage(itemView.getContext(), nimUserInfo.getAvatar(), profile, true);
            }
            account.setText(imMessage.getFromNick());
            message.setText(imMessage.getContent());
        }
    }

    public static class AudioMessageLeftViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public AudioMessageLeftViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_audio_left, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public static class AudioMessageRightViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public AudioMessageRightViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_audio_right, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public static class VideoMessageLeftViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public VideoMessageLeftViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_video_left, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public static class VideoMessageRightViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public VideoMessageRightViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_video_right, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public static class AVChatMessageLeftViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public AVChatMessageLeftViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_avchat_left, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public static class AVChatMessageRightViewHolder extends BaseViewHolder {

        private ImageView photo;

        private TextView account;

        public AVChatMessageRightViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_chat_avchat_right, itemView, false));
            findView();
        }

        private void findView() {
            photo = itemView.findViewById(R.id.photo);
            account = itemView.findViewById(R.id.account);
        }

        @Override
        public void fillData(IMMessage imMessage) {
        }
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void fillData(IMMessage imMessage);
    }
}
