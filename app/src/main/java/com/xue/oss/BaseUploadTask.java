package com.xue.oss;

import com.elianshang.threadpool.BaseTask;
import com.elianshang.threadpool.BaseThreadPool;
import com.elianshang.threadpool.ThreadPoolFactory;
import com.elianshang.threadpool.ThreadPoolOptions;

public abstract class BaseUploadTask implements BaseTask {

    /**
     * 线程池
     */
    protected static final BaseThreadPool mThreadPool;

    static {// 初始化线程池
        ThreadPoolOptions options = new ThreadPoolOptions();
        options.setPriority(Thread.NORM_PRIORITY);
        options.setSize(1);
        options.setKeep(0);
        options.setWaitPeriod(1000);
        options.setReplayFailTask(false);
        options.setExecutionOrder(ThreadPoolOptions.ExecutionOrder.FIFO);
        mThreadPool = ThreadPoolFactory.create(options);
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public int compareTo(BaseTask another) {
        return 0;
    }

    public final void start() {
        mThreadPool.addNewTask(this);// 加入线程队列，等待执行
    }
}
