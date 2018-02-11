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
        Comment
    }

    private long mOldTimeStamp;

    private long mTimeStamp;

    private User mUser;

    private MomentInfoList.MomentInfo mMomentInfo;

    private OrderCommentInfo mOrderCommentInfo;

    private ArrayList<ItemType> mItemList;

    public DetailHelper() {
    }

    public void setUser(User user) {
        mUser = user;
        mTimeStamp = System.currentTimeMillis();
    }

    public void setMomentInfo(MomentInfoList.MomentInfo momentInfo) {
        mMomentInfo = momentInfo;
        mTimeStamp = System.currentTimeMillis();
    }

    public void setOrderCommentInfo(OrderCommentInfo orderCommentInfo) {
        this.mOrderCommentInfo = orderCommentInfo;
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

        if (getFollow() != null) {
            itemList.add(ItemType.Follow);
        }

        itemList.add(ItemType.Gallery);

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

        boolean markFlag = false;
        boolean commentFlag = false;

        itemList.add(ItemType.CommentTitle);
        if (getMark() != null) {
            markFlag = true;
            itemList.add(ItemType.Mark);
        }

        if (getComment() != null) {
            commentFlag = true;
            itemList.add(ItemType.Comment);
        }

        if (!markFlag || !commentFlag) {
            itemList.remove(ItemType.Mark);
            itemList.remove(ItemType.Comment);
            itemList.remove(ItemType.CommentTitle);
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

    public UserFriendInfo getFollow() {
        UserFriendInfo userFriendInfo = mUser.getUserFriendInfo();
        if (userFriendInfo != null) {
            return userFriendInfo;
        }
        return null;
    }

    public String[] getGallery() {
        if (mMomentInfo != null) {
            String[] pics = new String[3];
            int size = mMomentInfo.getResList().size();
            if (size > 0) {
                pics[0] = mMomentInfo.getResList().get(size - 1).getUrl();
            }

            if (size > 1) {
                pics[1] = mMomentInfo.getResList().get(size - 2).getUrl();
            }

            if (size > 2) {
                pics[2] = mMomentInfo.getResList().get(size - 3).getUrl();
            }

            return pics;
        }

        return null;
    }

    public ArrayList<String> getOtherGallery() {
        ArrayList<String> pics = new ArrayList();
        if (mMomentInfo != null) {
            for (MomentInfoList.MomentRes res : mMomentInfo.getResList()) {
                pics.add(res.getUrl());
            }
        }

        UserExpertInfo userExpertInfo = mUser.getUserExpertInfo();
        if (userExpertInfo != null) {
            if (TextUtils.equals("2", userExpertInfo.getWorkCardAuth())) {
                pics.add(userExpertInfo.getWorkCardImg());
            }

            if (TextUtils.equals("2", userExpertInfo.getBusinessCardAuth())) {
                pics.add(userExpertInfo.getBusinessCardImg());
            }
        }

        if (pics.size() == 0) {
            pics = null;
        }

        return pics;
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

    public OrderScoreMap getMark() {
        if (mOrderCommentInfo != null) {
            return mOrderCommentInfo.getOrderScoreMap();
        }

        return null;
    }

    public OrderCommentList.Comment getComment() {
        if (mOrderCommentInfo != null) {
            OrderCommentList orderCommentList = mOrderCommentInfo.getOrderCommentList();

            if (orderCommentList != null && orderCommentList.size() > 0) {
                return orderCommentList.get(0);
            }
        }

        return null;
    }
}
