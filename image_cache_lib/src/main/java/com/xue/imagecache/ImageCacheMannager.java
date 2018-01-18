package com.xue.imagecache;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * 图片缓存管理类
 */
public class ImageCacheMannager {

    /**
     * 清除硬盘缓存
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清除内存缓存
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 下载图片
     */
    public static void downloadImage(RequestManager requestManager, Object model, RequestListener<File> listener) {
        requestManager.asFile().load(model).listener(listener).submit();
    }

    /**
     * 加载图片
     */
    public static void loadImage(Context context, Object model, ImageView imageView) {
        loadImage(context, model, 0, 0, imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(ContextWrapper contextWrapper, Object model, ImageView imageView) {
        loadImage(contextWrapper, model, 0, 0, imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Activity activity, Object model, ImageView imageView) {
        loadImage(activity, model, 0, 0, imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(FragmentActivity activity, Object model, ImageView imageView) {
        loadImage(activity, model, 0, 0, imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Fragment fragment, Object model, ImageView imageView) {
        loadImage(fragment, model, 0, 0, imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Context context, Object model, int placeholder, int error, ImageView imageView) {
        loadImage(context, model, placeholder, error, imageView, null);
    }

    /**
     * 加载图片
     */
    public static void loadImage(ContextWrapper contextWrapper, Object model, int placeholder, int error, ImageView imageView) {
        loadImage(contextWrapper, model, placeholder, error, imageView, null);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Activity activity, Object model, int placeholder, int error, ImageView imageView) {
        loadImage(activity, model, placeholder, error, imageView, null);
    }

    /**
     * 加载图片
     */
    public static void loadImage(FragmentActivity activity, Object model, int placeholder, int error, ImageView imageView) {
        loadImage(activity, model, placeholder, error, imageView, null);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Fragment fragment, Object model, int placeholder, int error, ImageView imageView) {
        loadImage(fragment, model, placeholder, error, imageView, null);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Context context, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        loadImage(Glide.with(context), model, placeholder, error, imageView, listener);
    }

    /**
     * 加载图片
     */
    public static void loadImage(ContextWrapper contextWrapper, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        loadImage(Glide.with(contextWrapper), model, placeholder, error, imageView, listener);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Activity activity, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        loadImage(Glide.with(activity), model, placeholder, error, imageView, listener);
    }

    /**
     * 加载图片
     */
    public static void loadImage(FragmentActivity activity, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        loadImage(Glide.with(activity), model, placeholder, error, imageView, listener);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Fragment fragment, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        loadImage(Glide.with(fragment), model, placeholder, error, imageView, listener);
    }

    /**
     * 加载图片
     */
    private static void loadImage(RequestManager requestManager, Object model, int placeholder, int error, ImageView imageView, RequestListener<Drawable> listener) {
        requestManager.load(model)
                .apply(RequestOptions.placeholderOf(placeholder).error(error))
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(listener)
                .into(imageView);
    }
}