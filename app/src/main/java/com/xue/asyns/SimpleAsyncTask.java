package com.xue.asyns;

import android.os.Looper;

import com.elianshang.tools.WeakReferenceHandler;

/**
 * 普通异步任务，用来做查询数据库或者读取本地文件并需要更新UI的操作
 */
public abstract class SimpleAsyncTask<T> extends BaseTaskImpl implements SimpleAsyncTaskInterface<T> {

    private WeakReferenceHandler<SimpleAsyncTask> handler;

    public SimpleAsyncTask() {
        handler = new WeakReferenceHandler(this, Looper.getMainLooper());
    }

    @Override
    public final void run() {
        try {
            postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<SimpleAsyncTask>() {
                @Override
                public void run(SimpleAsyncTask simpleAsyncTask) {
                    simpleAsyncTask.onPreExecute();
                }
            });

            if (!isCancel) {
                T result = null;

                try {
                    result = doInBackground();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final T r = result;
                if (!isCancel) {
                    postUI(new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<SimpleAsyncTask>() {
                        @Override
                        public void run(SimpleAsyncTask simpleAsyncTask) {
                            if (!isCancel) {
                                simpleAsyncTask.onPostExecute(r);
                            }
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

    @Override
    public boolean onPreExecute() {
        return true;
    }

    private void postUI(WeakReferenceHandler.WeakReferenceHandlerRunnalbe<SimpleAsyncTask> runnable) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(runnable);
        } else {
            runnable.run(this);
        }
    }

    public synchronized void start() {
        isCancel = !onPreExecute();
        mThreadPool.addNewTask(this);// 加入线程队列，等待执行
    }


}
