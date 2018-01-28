package com.xue.tools;

import android.app.Activity;
import android.content.Context;

import com.xue.R;
import com.xue.imagecache.ImageCacheMannager;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
        ImageCacheMannager.loadImage(activity, path, R.drawable.gallery_pick_photo, R.drawable.gallery_pick_photo, galleryImageView, false);
    }

    @Override
    public void clearMemoryCache() {

    }
}