package com.elianshang.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 储存工具
 * */
public class StorageTool {

    /**
     * 判断是否有Sd卡
     */
    public static boolean hasExternalStorage() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }
        return false;
    }

    /**
     * SD卡根目录
     */
    public static File getExternalStorageFile() {
        if (hasExternalStorage()) {
            return Environment.getExternalStorageDirectory();
        }

        return null;
    }

    /**
     * 系统缓存目录 data/data
     */
    public static File getFilesDirFile(Context context) {
        return context.getFilesDir();
    }

    /**
     * 系统外部缓存根目录
     */
    public static File getExternalFilesDirFile(Context context) {
        return context.getExternalFilesDir(null);
    }

    /**
     * 获得外置存储卡,优先外置储存
     */
    public static File getExternalSdCardFile() {
        List<String> mMounts = new ArrayList<String>(10);
        List<String> mVold = new ArrayList<String>(10);

        try {
            File mountFile = new File("/proc/mounts");
            if (mountFile.exists()) {
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];

                        if (!element.equals("/mnt/sdcard")) {
                            mMounts.add(element);
                        }
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File voldFile = new File("/system/etc/vold.fstab");
            if (voldFile.exists()) {
                Scanner scanner = new Scanner(voldFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("dev_mount")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[2];

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"));
                        if (!element.equals("/mnt/sdcard")) {
                            mVold.add(element);
                        }
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get(i);
            if (!mVold.contains(mount))
                mMounts.remove(i--);
        }
        mVold.clear();

        for (String mount : mMounts) {
            File root = new File(mount);
            if (root.exists() && root.isDirectory() && root.canRead()) {
                mMounts.clear();
                return root;
            }
        }

        mMounts.clear();

        return null;
    }
}
