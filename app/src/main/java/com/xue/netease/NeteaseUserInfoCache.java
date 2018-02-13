package com.xue.netease;

import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeteaseUserInfoCache implements Observer<List<NimUserInfo>> {


    private static class NeteaseUserInfoCacheBuilder {

        private static NeteaseUserInfoCache instance = new NeteaseUserInfoCache();
    }

    private Map<String, NimUserInfo> mCacheList;

    private CacheChangeCallback mCallback;

    private boolean isForce = true;

    @Override
    public String toString() {
        return super.toString();
    }

    private NeteaseUserInfoCache() {
        mCacheList = new HashMap();
        NIMSDK.getUserServiceObserve().observeUserInfoUpdate(this, true);
    }

    public void setCallback(CacheChangeCallback callback) {
        this.mCallback = callback;
    }

    public static NeteaseUserInfoCache get() {
        return NeteaseUserInfoCacheBuilder.instance;
    }

    public boolean update(ArrayList<String> accounts) {
        if (isForce) {
            NIMSDK.getUserService().fetchUserInfo(accounts);
            isForce = false;
            return false;
        } else {
            ArrayList<String> list = new ArrayList();
            for (String account : accounts) {
                if (checkLocal(account) == null) {
                    list.add(account);
                }
            }

            if (list.size() > 0) {
                NIMSDK.getUserService().fetchUserInfo(list);
                return false;
            }

            return true;
        }
    }

    private NimUserInfo checkLocal(String account) {
        NimUserInfo nimUserInfo = mCacheList.get(account);
        if (nimUserInfo == null) {
            nimUserInfo = NIMSDK.getUserService().getUserInfo(account);

            if (nimUserInfo != null) {
                mCacheList.put(account, nimUserInfo);

                return nimUserInfo;
            }
        }

        return mCacheList.get(account);
    }

    public NimUserInfo getUserInfo(String account) {
        NimUserInfo nimUserInfo = mCacheList.get(account);
        if (nimUserInfo == null) {
            nimUserInfo = NIMSDK.getUserService().getUserInfo(account);

            if (nimUserInfo != null) {
                mCacheList.put(account, nimUserInfo);
                return nimUserInfo;
            } else {
                ArrayList<String> list = new ArrayList(1);
                list.add(account);
                NIMSDK.getUserService().fetchUserInfo(list);
                return null;
            }
        }
        return mCacheList.get(account);
    }


    @Override
    public void onEvent(List<NimUserInfo> nimUserInfos) {
        if (mCallback != null) {
            mCallback.onChange();
        }
    }

    public interface CacheChangeCallback {

        public void onChange();

    }
}
