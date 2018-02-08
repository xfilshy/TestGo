package com.xue.bean;

import android.text.TextUtils;

import java.util.ArrayList;

public class DetailHelper {

    public enum ItemType {
        //独白
        Intro,
        //关注
        Follow,
        //相册
        Gallery,
        //我的信息标题
        InfoTitle,
        //工作经历
        Work,
        //教育经历
        Education,
        //家乡
        HomeTown,
        //评论标题
        CommentTitle,
        //评分
        Mark,
        //评论
        Comment,
    }

    private long mOldTimeStamp;

    private long mTimeStamp;

    private User mUser;

    private ArrayList<ItemType> mItemList;

    public DetailHelper() {
    }

    public void setUser(User user) {
        mUser = user;
        mTimeStamp = System.currentTimeMillis();
    }

    public int getCount() {
        if (mTimeStamp == mOldTimeStamp) {
            return mItemList == null ? 0 : mItemList.size();
        }

        ArrayList<ItemType> itemList = new ArrayList();

        if (!TextUtils.isEmpty(getIntro())) {
            itemList.add(ItemType.Intro);
        }

        if (!TextUtils.isEmpty(getFollow())) {
            itemList.add(ItemType.Follow);
        }

        if (getGallery() != null) {
            itemList.add(ItemType.Gallery);
        }

        boolean infoFlag = false;
        itemList.add(ItemType.InfoTitle);

        if (getWork() != null) {
            itemList.add(ItemType.Work);
            infoFlag = true;
        }

        if (getEducation() != null) {
            itemList.add(ItemType.Education);
            infoFlag = true;
        }

        if (!TextUtils.isEmpty(getHomeTown())) {
            itemList.add(ItemType.HomeTown);
            infoFlag = true;
        }

        if (!infoFlag) {
            itemList.remove(ItemType.InfoTitle);
        }

        mItemList = itemList;
        mOldTimeStamp = mTimeStamp;

        return mItemList.size();
    }

    public ItemType getItemType(int position) {
        return mItemList.get(position);
    }

    public String getIntro() {
        UserDetailInfo userDetailInfo = mUser.getUserDetailInfo();
        if (userDetailInfo != null) {
            return userDetailInfo.getIntro();
        }

        return null;
    }

    public String getFollow() {
        return null;
    }

    public String[] getGallery() {
        return null;
    }

    public UserWorkInfo.Work getWork() {
        UserWorkInfo userWorkInfo = mUser.getUserWorkInfo();
        if (userWorkInfo != null && userWorkInfo.size() > 0) {
            UserWorkInfo.Work work = userWorkInfo.get(0);

            return work;
        }

        return null;
    }

    public UserEducationInfo.Education getEducation() {
        UserEducationInfo userEducationInfo = mUser.getUserEducationInfo();
        if (userEducationInfo != null && userEducationInfo.size() > 0) {
            UserEducationInfo.Education education = userEducationInfo.get(0);

            return education;
        }

        return null;
    }

    public String getHomeTown() {
        UserDetailInfo userDetailInfo = mUser.getUserDetailInfo();
        if (userDetailInfo != null) {
            return userDetailInfo.getHomeTownName();
        }

        return null;
    }
}
