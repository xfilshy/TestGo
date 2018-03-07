package com.xue.asyns;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.elianshang.tools.NetWorkTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.tools.WeakReferenceHandler;
import com.xue.http.impl.DataHull;
import com.xue.tools.LoadingDialog;


/**
 * 网络请求的异步任务
 */
public abstract class HttpAsyncTask<T> extends BaseTaskImpl implements HttpAsyncTaskInterface<T> {

    /**
     * 上下文对象
     */
    protected final Context context;

    /**
     * 回调主线程 handler
     */
    protected final WeakReferenceHandler<HttpAsyncTask> handler;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 状态
     */
    private int status;

    /**
     * 是否显示toast
     */
    private boolean showToast = false;
    /**
     * 是否显示加载dialog
     */
    private boolean showLoading = false;

    private LoadingDialog loadingDialog;

    protected boolean hasNet = true;

    private boolean cancelable = false;

    public HttpAsyncTask(Context context) {
        this.context = context;
        handler = new WeakReferenceHandler<HttpAsyncTask>(this, Looper.getMainLooper());
    }

    public HttpAsyncTask(Context context, boolean showToast) {
        this(context);
        this.showToast = showToast;
    }

    public HttpAsyncTask(Context context, boolean showToast, boolean showLoading) {
        this(context, showToast);
        this.showLoading = showLoading;
    }

    public HttpAsyncTask(Context context, boolean showToast, boolean showLoading, boolean cancelable) {
        this(context, showToast);
        this.showLoading = showLoading;
        this.cancelable = cancelable;
    }

    @Override
    public final void run() {
        final long start = System.currentTimeMillis();
        try {
            hasNet = NetWorkTool.isNetAvailable(context);

            if (!hasNet) {// 判断网络
                cancel();
                postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                    @Override
                    public void run(HttpAsyncTask httpAsyncTask) {
                        httpAsyncTask.netNull();
                    }
                });
                // 无网
//                UMengEventTool.onRequest(context, "无网络", "-1", "", (int) (System.currentTimeMillis() - start));
                return ;
            }

            if (!isCancel) {// 加载网络数据
                showLoadingDialog();
                DataHull<T> dh = doInBackground();

                if (dh != null && dh.getStatus() == 1020) {
                    // 重试
//                    UMengEventTool.onRequest(context, "时间戳重试", String.valueOf(dh.getStatus()), dh.getFunction(), (int) (System.currentTimeMillis() - start));
                    dh = doInBackground();
                }
                if (dh != null && (dh.getStatus() == 1001 || dh.getStatus() == 1002)) {
                    // token异常后读取一次配置
//                    HttpApi.homeConfigInit();
                }

                final DataHull<T> dataHull = dh;

                if (!isCancel) {
                    postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                        @Override
                        public void run(HttpAsyncTask httpAsyncTask) {
                            try {
                                httpAsyncTask.isCancel = true;
                                if (dataHull == null) {
                                    // datahull为空
//                                    UMengEventTool.onRequest(context, "数据壳为空", "-2", "-2", (int) (System.currentTimeMillis() - start));
                                    netErr(0, null);
                                } else {
                                    httpAsyncTask.message = dataHull.getMessage();
                                    httpAsyncTask.status = dataHull.getStatus();
                                    T t = dataHull.getDataEntity();
                                    if (dataHull.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                                        if (t == null) {
                                            // 实体为空
//                                            UMengEventTool.onRequest(context, "实体为空", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
                                            httpAsyncTask.dataNull(dataHull.getDataId(), message);
                                        } else {
                                            // 正确响应
//                                            UMengEventTool.onRequest(context, "成功响应", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
                                            httpAsyncTask.onPostExecute(dataHull.getDataId(), dataHull.getDataEntity());
                                        }
                                    } else if (dataHull.getDataType() == DataHull.DataType.DATA_NO_UPDATE) {
                                        // 无数据更新
//                                        UMengEventTool.onRequest(context, "无数据更新", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
                                        httpAsyncTask.noUpdate();
                                    } else {
                                        if (dataHull.getDataType() == DataHull.DataType.DATA_CAN_NOT_PARSE) {
                                            if (dataHull.getStatus() == 1001 || dataHull.getStatus() == 1002) {
                                                // token异常
//                                                UMengEventTool.onRequest(context, "token异常", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
//                                                if (context instanceof Activity) {
//                                                    final Activity activity = (Activity) context;
//                                                    DialogTools.showOneButtonDialog(activity, dataHull.getMessage(), "知道了", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            if (!(activity instanceof MainActivity)) {
//                                                                activity.finish();
//                                                            }
//                                                            BaseApplication.get().setUser(null);
//                                                            LoginActivity.launch(activity);
//                                                        }
//                                                    }, false);
//                                                }

                                                httpAsyncTask.netErr(dataHull.getDataId(), message); //token过期时  不显示无网UI
                                            } else if (dataHull.getStatus() == 1111) {
                                                // 接口关闭
//                                                UMengEventTool.onRequest(context, "接口关闭", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
//                                                if (context instanceof Activity) {
//                                                    final Activity activity = (Activity) context;
//                                                    DialogTools.showOneButtonDialog(activity, dataHull.getMessage(), "知道了", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            DBManager.shoppingListDAO.clear();
//                                                            BaseApplication.get().setUser(null);
//                                                            activity.finish();
//                                                            android.os.Process.killProcess(android.os.Process.myPid());
//                                                            JPushInterface.onKillProcess(activity);
//                                                        }
//                                                    }, false);
//                                                }

                                                httpAsyncTask.netErr(dataHull.getDataId(), message);
                                            } else {
                                                // 请求异常
//                                                UMengEventTool.onRequest(context, "请求异常", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
                                                httpAsyncTask.netErr(dataHull.getDataId(), message);
                                            }
                                        } else {
                                            // 接口异常
//                                            UMengEventTool.onRequest(context, "接口异常", String.valueOf(dataHull.getStatus()), dataHull.getFunction(), (int) (System.currentTimeMillis() - start));
                                            httpAsyncTask.netErr(dataHull.getDataId(), message);
                                        }
                                    }
                                }
                                cancel();
                            } catch (Exception e) {
                                // 线程异常
//                                UMengEventTool.onRequest(context, "线程异常", "-1", "", (int) (System.currentTimeMillis() - start));
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    // 取消
//                    UMengEventTool.onRequest(context, "请求取消", "-1", "", (int) (System.currentTimeMillis() - start));

                }
            } else {
                // 取消
//                UMengEventTool.onRequest(context, "请求取消", "-1", "", (int) (System.currentTimeMillis() - start));
            }
        } catch (Exception e) {
            // 线程异常
//            UMengEventTool.onRequest(context, "线程异常", "-1", "", (int) (System.currentTimeMillis() - start));
            e.printStackTrace();
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        httpAsyncTask.netErr(0, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } finally {
            dismissLoadingDialog();
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        httpAsyncTask.finished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean onPreExecute() {
        return true;
    }

    private void postUI(WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask> runnable) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(runnable);
        } else {
            runnable.run(this);
        }
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public final void start() {
        isCancel = !onPreExecute();
        if (isCancel) {
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    httpAsyncTask.preFail();
                }
            });

            return;
        }
        mThreadPool.addNewTask(this);// 加入线程队列，等待执行
    }

    /**
     * 请求前，准备失败回调
     */
    public void preFail() {
    }

    /**
     * 没有网络，回调
     */
    public void netNull() {
        if (showToast && null != context) {
//            ToastTool.show(context, R.string.text_net_null);
        }
    }

    /**
     * 网络异常和数据错误，回调
     */
    public void netErr(int updateId, String errMsg) {
        if (showToast && null != context) {
            if (!TextUtils.isEmpty(errMsg)) {
                if (status != 1001 && status != 1002 && status != 1004 && status != 1111) {
                    ToastTool.show(context, errMsg);
                }
            } else {
                ToastTool.show(context, "网络异常");
            }
        }
    }

    /**
     * 数据为空，回调
     */
    public void dataNull(int updateId, String errMsg) {
        if (showToast && null != context && !TextUtils.isEmpty(errMsg)) {
            ToastTool.show(context, errMsg);
        }
    }

    @Override
    public void finished() {
    }

    /**
     * 数据无更新，回调
     */
    public void noUpdate() {
//        ToastTool.show(context, R.string.text_no_update);
    }

    protected void showLoadingDialog() {
        if (showLoading && null != context) {
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        if (loadingDialog == null) {
                            loadingDialog = LoadingDialog.getLoadingDialog(context);
                            loadingDialog.setCancelable(cancelable);
//                            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                    cancel();
//                                }
//                            });
                        }
                        loadingDialog.show(hashCode());

                        Log.e("xue", "class:" + HttpAsyncTask.this.getClass().getName() + "  hashcode:" + context.hashCode() + " loadingDialog:" + loadingDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void dismissLoadingDialog() {
        if (showLoading && null != context) {
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        if (null != loadingDialog) {
                            loadingDialog.dismiss(hashCode());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}