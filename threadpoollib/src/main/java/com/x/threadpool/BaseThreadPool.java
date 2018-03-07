package com.x.threadpool;


import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BaseThreadPool {

    private final Lock mLock = new ReentrantLock();

    private final Condition mRunCondition = mLock.newCondition();

    /**
     * 任务队列
     */
    private final ArrayList<BaseTask> mTaskQueue;

    /**
     * 线程单元
     */
    private final ArrayList<ThreadUnit> mThreadUnits;

    /**
     * 线程池设置
     */
    private ThreadPoolOptions options;

    /**
     * 等待计数
     */
    private int mWaitCount = 0;

    /**
     * 构造方法
     */
    BaseThreadPool(ThreadPoolOptions options) {
        if (options == null) {
            throw new NullPointerException("ThreadPoolOptions is null");
        }
        this.options = options;

        mTaskQueue = new ArrayList();

        mThreadUnits = new ArrayList();
        for (int i = 0; i < options.getKeep(); i++) {
            ThreadUnit threadUnit = createThread();
            mThreadUnits.add(threadUnit);
        }
    }

    /**
     * 创建线程
     */
    private ThreadUnit createThread() {
        ThreadUnit mThreadUnits = new ThreadUnit();
        mThreadUnits.setPriority(options.getPriority());
        mThreadUnits.start();

        return mThreadUnits;
    }

    /**
     * 添加新任务
     */
    public boolean addNewTask(BaseTask task) {
        try {
            mLock.lock();
            boolean isSuccess = mTaskQueue.add(task);
            if (isSuccess) {
                if (mWaitCount > 0) {
                    mRunCondition.signalAll();
                } else {
                    if (mThreadUnits.size() < options.getSize()) {
                        ThreadUnit threadUnit = createThread();
                        mThreadUnits.add(threadUnit);
                        mRunCondition.signal();
                    }
                }

                return true;
            }

            return false;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 移除等待中的任务
     */
    public void clearWaitTask() {
        try {
            mLock.lock();
            mTaskQueue.clear();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 等待任务数量
     */
    public int getWaitTaskCount() {
        try {
            mLock.lock();
            return mTaskQueue.size();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 移除任务
     */
    public boolean removeTask(BaseTask task) {
        try {
            mLock.lock();
            boolean isSuccess = mTaskQueue.remove(task);
            return isSuccess;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 销毁线程池
     */
    public void destroyThreadPool() {
        for (ThreadUnit threadUnit : mThreadUnits) {
            if (threadUnit != null) {
                threadUnit.isRunning = false;
                threadUnit.interrupt();
            }
        }

        mTaskQueue.clear();
    }

    class ThreadUnit extends Thread {

        /**
         * 是否在运行
         */
        public boolean isRunning = false;

        /**
         * 任务对象
         */
        private BaseTask task = null;

        /**
         * 是否在等待
         */
        public boolean isWait = true;

        @Override
        public void run() {
            isRunning = true;
            long timeFlag = Long.MAX_VALUE;

            while (isRunning) {
                try {
                    try {
                        mLock.lockInterruptibly();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    /**
                     * 线程池中无任务时每隔一段时间检测是否有新任务被添加
                     */
                    while (mTaskQueue.isEmpty()) {
                        if (mThreadUnits.size() > options.getKeep() && timeFlag <= System.currentTimeMillis()) {
                            isRunning = false;
                            if (mThreadUnits != null && mThreadUnits.contains(this)) {
                                mThreadUnits.remove(this);
                            }
                            return;
                        } else {
                            try {
                                mWaitCount++;
                                isWait = true;
                                timeFlag = System.currentTimeMillis() + options.getWaitPeriod();
                                mRunCondition.await(options.getWaitPeriod(), TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    mWaitCount--;
                    isWait = false;
                    /**
                     * 移除任务
                     */
                    Collections.sort(mTaskQueue);

                    if (options.getExecutionOrder() == ThreadPoolOptions.ExecutionOrder.FIFO) {
                        task = mTaskQueue.remove(0);
                    } else {
                        task = mTaskQueue.remove(mTaskQueue.size() - 1);
                    }
                } finally {
                    mLock.unlock();
                }

                if (task != null && !task.isCancelled()) {
                    /**
                     * 执行移除的任务
                     */
                    try {
                        task.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
