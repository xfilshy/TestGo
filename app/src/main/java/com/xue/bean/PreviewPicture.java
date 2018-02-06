package com.xue.bean;

import android.graphics.Rect;
import android.os.Parcel;

import com.previewlibrary.enitity.IThumbViewInfo;

public class PreviewPicture implements IThumbViewInfo {

    private String url;  //图片地址

    private Rect mBounds; // 记录坐标

    public PreviewPicture(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.mBounds, 0);
    }

    protected PreviewPicture(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Creator<PreviewPicture> CREATOR = new Creator<PreviewPicture>() {

        public PreviewPicture createFromParcel(Parcel source) {
            return new PreviewPicture(source);
        }

        public PreviewPicture[] newArray(int size) {
            return new PreviewPicture[size];
        }
    };
}