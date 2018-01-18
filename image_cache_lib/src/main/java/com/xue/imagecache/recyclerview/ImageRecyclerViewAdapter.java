package com.xue.imagecache.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

public abstract class ImageRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, MODLE> extends RecyclerView.Adapter<VH> implements ListPreloader.PreloadModelProvider<MODLE> {

    private RequestManager requestManager ;

    public ImageRecyclerViewAdapter(Context context) {
        requestManager = Glide.with(context);
    }

    public ImageRecyclerViewAdapter(ContextWrapper contextWrapper) {
        requestManager = Glide.with(contextWrapper);
    }

    public ImageRecyclerViewAdapter(Activity activity) {
        requestManager = Glide.with(activity);
    }

    public ImageRecyclerViewAdapter(FragmentActivity fragmentActivity) {
        requestManager = Glide.with(fragmentActivity);
    }

    public ImageRecyclerViewAdapter(Fragment fragment){
        requestManager = Glide.with(fragment);
    }

    @Override
    public final RequestBuilder getPreloadRequestBuilder(MODLE modle) {
        return requestManager.load(modle);
    }
}