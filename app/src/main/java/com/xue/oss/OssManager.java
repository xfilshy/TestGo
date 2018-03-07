package com.xue.oss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.elianshang.tools.MD5Tool;
import com.xue.BaseApplication;
import com.xue.asyns.SimpleAsyncTask;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.FileCompressCallableTasks;
import com.zxy.tiny.common.CompressResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class OssManager extends OSSCustomSignerCredentialProvider {

    private OSS mOSS;

    private OssConfig mOssConfig;

    private static class OssManagerBuilder {
        private static OssManager instance = new OssManager();
    }

    private OssManager() {

    }

    public static OssManager get() {
        return OssManagerBuilder.instance;
    }

    @Override
    public String signContent(String content) {
        DataHull<SignContent> dh = HttpApi.signContent(content);
        if (dh.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
            SignContent signContent = dh.getDataEntity();
            if (signContent != null) {
                return signContent.getSign();
            }
        }
        return null;
    }

    private synchronized boolean init(List<String> filePaths, OssManager.Callback callback, boolean deleteOriginal) {
        if (mOSS == null) {
            if (mOssConfig == null) {
                new OssConfigTask(filePaths, callback, deleteOriginal).start();
                return false;
            } else {
                String endpoint = mOssConfig.getEndPoint();

                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

                mOSS = new OSSClient(BaseApplication.get(), endpoint, this, conf);
            }
        }

        return true;
    }

    public void upload(List<String> filePaths, OssManager.Callback callback, boolean deleteOriginal) {
        if (callback != null) {
            callback.onInit();
        }

        if (!init(filePaths, callback, deleteOriginal)) {
            return;
        }

        new UploadTask(mOSS, filePaths, mOssConfig.getBucketName(), mOssConfig.getUploadPath(), deleteOriginal, callback).start();
    }

    public void upload(final String filePath, OssManager.Callback callback, boolean deleteOriginal) {
        List<String> filePaths = new ArrayList();
        filePaths.add(filePath);
        this.upload(filePaths, callback, deleteOriginal);
    }


    private static class UploadTask extends BaseUploadTask {

        private boolean deleteOriginal = false;

        private List<String> paths;

        private OSS oss;

        private String bucket;

        private String uploadPath;

        private Callback callback;

        public UploadTask(OSS oss, List<String> paths, String bucket, String uploadPath, boolean deleteOriginal, Callback callback) {
            this.oss = oss;
            this.paths = paths;
            this.bucket = bucket;
            this.uploadPath = uploadPath;
            this.callback = callback;
        }

        @Override
        public void run() {
            if (callback != null) {
                callback.onStarted();
            }

            for (final String filePath : paths) {
                File file = new File(filePath);
                if (!file.exists()) {
                    if (callback != null) {
                        callback.onFailure(filePath, -5);
                        continue;
                    }
                }

                File tmpFile = null;
                try {
                    Tiny.FileCompressOptions mCompressOptions = new Tiny.FileCompressOptions();
                    mCompressOptions.quality = 75;
                    mCompressOptions.outfile = file.getAbsolutePath() + "tmp";

                    FileCompressCallableTasks.FileAsFileCallable callable = new FileCompressCallableTasks.FileAsFileCallable(mCompressOptions, false, file);
                    CompressResult compressResult = callable.call();

                    if (compressResult.success) {
                        tmpFile = new File(compressResult.outfile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String imageType = filePath.substring(filePath.indexOf(".") + 1);
                if (tmpFile == null) {
                    if ("png".equalsIgnoreCase(imageType)) {
                        tmpFile = pngComPress(file);
                    } else if ("jpg".equalsIgnoreCase(imageType) || "jpeg".equalsIgnoreCase(imageType)) {
                        tmpFile = jpegComPress(file);
                    }
                }

                if (tmpFile == null) {
                    tmpFile = file;
                }

                final String md5Name = uploadPath + MD5Tool.getMd5ByFile(tmpFile);
                PutObjectRequest request = new PutObjectRequest(bucket, md5Name, tmpFile.getPath());
                request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                        if (callback != null) {
                            callback.onProgress(filePath, (float) currentSize / totalSize);
                        }
                    }
                });

                PutObjectResult result = null;
                try {
                    result = oss.putObject(request);
                } catch (ClientException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onFailure(filePath, -1);
                        continue;
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onFailure(filePath, -2);
                        continue;
                    }
                }

                if (tmpFile.getPath().endsWith("tmp")) {
                    tmpFile.delete();
                }

                if (deleteOriginal) {
                    file.delete();
                }

                if (result != null) {
                    if (result.getStatusCode() == 200) {
                        if (callback != null) {
                            callback.onSuccess(filePath, md5Name);
                            continue;
                        }
                    }
                }

                if (callback != null) {
                    callback.onFailure(filePath, -3);
                }
            }

            if (callback != null) {
                callback.onFinish();
            }

            callback = null;
        }
    }


    private class OssConfigTask extends SimpleAsyncTask<OssConfig> {

        private boolean deleteOriginal = false;

        private List<String> filePaths;

        private OssManager.Callback callback;

        OssConfigTask(List<String> filePaths, OssManager.Callback callback, boolean deleteOriginal) {
            this.filePaths = filePaths;
            this.callback = callback;
            this.deleteOriginal = deleteOriginal;
        }

        @Override
        public OssConfig doInBackground() {
            DataHull<OssConfig> dh = HttpApi.ossConfig();
            if (dh.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                return dh.getDataEntity();
            }

            return null;
        }

        @Override
        public void onPostExecute(OssConfig result) {
            if (result != null) {
                mOssConfig = result;
                upload(filePaths, callback, deleteOriginal);
            } else {
                if (callback != null) {
                    callback.onInitFailure();
                }

                if (callback != null) {
                    callback.onFinish();
                }
            }
        }
    }

    private static File pngComPress(File file) {
        try {
            if (file.length() > 0.5f * 1024 * 1024) {
                File tmpFile = new File(file.getAbsolutePath() + "tmp");
                Bitmap bitmap = decodeSampledBitmap(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, new FileOutputStream(tmpFile));
                bitmap.recycle();

                return tmpFile;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static File jpegComPress(File file) {
        try {
            if (file.length() > 0.5f * 1024 * 1024) {
                File tmpFile = new File(file.getAbsolutePath() + "tmp");
                Bitmap bitmap = decodeSampledBitmap(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(tmpFile));
                bitmap.recycle();

                return tmpFile;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap decodeSampledBitmap(File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        options.inSampleSize = calculateInSampleSize(options, 1080, 1080);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public interface Callback {

        public void onInit();

        public void onInitFailure();

        public void onStarted();

        public void onProgress(String file, float progress);

        public void onSuccess(String file, String resultName);

        public void onFailure(String file, int code);

        public void onFinish();
    }
}
