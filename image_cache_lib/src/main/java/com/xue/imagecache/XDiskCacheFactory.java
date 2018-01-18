package com.xue.imagecache;

import android.content.Context;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

import java.io.File;

public class XDiskCacheFactory extends DiskLruCacheFactory {

    public XDiskCacheFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public XDiskCacheFactory(Context context, int diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public XDiskCacheFactory(final Context context, final String diskCacheName, int diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                File cacheDirectory = context.getExternalCacheDir();
                if (cacheDirectory == null) {
                    cacheDirectory = context.getCacheDir();
                }

                if (cacheDirectory == null) {
                    return null;
                }

                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }

                return cacheDirectory;
            }
        }, diskCacheSize);
    }
}
