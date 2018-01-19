package com.xue.netease;

import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by xfilshy on 2018/1/18.
 */

public class SimpleAVChatData implements AVChatData {

    private String account;

    private AVChatType avChatType;

    public SimpleAVChatData(String account, AVChatType avChatType) {
        this.account = account;
        this.avChatType = avChatType;
    }

    @Override
    public long getChatId() {
        return 0;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public AVChatType getChatType() {
        return avChatType;
    }

    @Override
    public long getTimeTag() {
        return 0;
    }

    @Override
    public String getExtra() {
        return null;
    }

    @Override
    public String getPushSound() {
        return null;
    }
}
