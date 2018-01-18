package com.elianshang.threadpool;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class BaseThreadPool {

    /**
     * 任务队列
     */
    private final LinkedList<BaseTask> mTaskQueue;

    /**
     * 执行中的任务
     */
    private final HashSet<BaseTask> runningSet;

    /**
     * 线程单元
     */
    private final ArrayList<ThreadUnit> mThreadUnits;

    /**
     * 线程池设置
     */
    private ThreadPoolOptions options;

    /**
     * 线程池是否加锁
     */
    private boolean lock;

    /**
     * 构造方法
     */
    BaseThreadPool(ThreadPoolOptions options) {
        if (options == null) {
            throw new NullPointerException("ThreadPoolOptions is null");
        }
        this.options = options;

        mTaskQueue = new LinkedList<BaseTask>();
        runningSet = new HashSet<BaseTask>();

        mThreadUnits = new ArrayList<ThreadUnit>();
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
        //Thread thread = new Thread();
        Thread thread = new Thread(mThreadUnits);
        thread.setPriority(options.getPriority());
        thread.start();

        return mThreadUnits;
    }

    /**
     * 添加新任务
     */
    public int addNewTask(BaseTask task) {
        synchronized (runningSet) {
            if (runningSet.contains(task)) {
                return 0;
            }
        }
        synchronized (mTaskQueue) {
            if (mTaskQueue.contains(task)) {
                return 1;
            }

            boolean isSuccess = mTaskQueue.offer(task);
            if (isSuccess && !lock) {
                for (ThreadUnit threadUnit : mThreadUnits) {
                    if (threadUnit.isWait && !threadUnit.isSchedule) {
                        threadUnit.isSchedule = true ;
                        mTaskQueue.notifyAll();
                        return 2;
                    }
                }
                if (mThreadUnits.size() < options.getSize()) {
                    ThreadUnit threadUnit = createThread();
                    mThreadUnits.add(threadUnit);
                    mTaskQueue.notify();
                }
                return 2;
            }

            return 3;
        }
    }

    /**
     * 移除等待中的任务
     */
    public void clearWaitTask() {
        synchronized (mTaskQueue) {
            mTaskQueue.clear();
        }
    }

    /**
     * 等待任务数量
     */
    public int getWaitTaskCount() {
        synchronized (mTaskQueue) {
            return mTaskQueue.size();
        }
    }

    /**
     * 执行中任务数量
     */
    public int getRunningTaskCount() {
        synchronized (runningSet) {
            return runningSet.size();
        }
    }

    /**
     * 等待任务数量
     */
    public boolean hasTaskInPool() {
        synchronized (BaseThreadPool.class) {
            if (getWaitTaskCount() > 0 || getRunningTaskCount() > 0) {
                return true;
            }

            return false;
        }
    }

    /**
     * 移除任务
     */
    public boolean removeTask(BaseTask task) {
        synchronized (mTaskQueue) {
            boolean isSuccess = mTaskQueue.remove(task);
            return isSuccess;
        }
    }

    /**
     * 销毁线程池
     */
    public synchronized void destroyThreadPool() {
        for (ThreadUnit threadUnit : mThreadUnits) {
            if (threadUnit != null) {
                threadUnit.isRunning = false;
            }
        }

        mTaskQueue.clear();
        runningSet.clear();
    }

    /**
     * 停止执行任务队列
     */
    public void lock() {
        lock = true;
    }

    /**
     * 解锁，开始执行任务
     */
    public void unlock() {
        lock = false;
        synchronized (mTaskQueue) {
            mTaskQueue.notifyAll();
        }
    }

    class ThreadUnit implements Runnable {

        public boolean isRunning = false;
        private BaseTask task = null;
        public boolean isWait = true;
        public boolean isSchedule = false;

        @Override
        public void run() {
            isRunning = true;
            while (isRunning) {
                synchronized (mTaskQueue) {

                    /**
                     * 线程池中无任务时每隔一段时间检测是否有新任务被添加
                     */
                    while (mTaskQueue.isEmpty() || lock) {
                        if (mThreadUnits.size() > options.getKeep()) {
                            isRunning = false;
                            if (mThreadUnits != null && mThreadUnits.contains(this)) {
                                mThreadUnits.remove(this);
                            }
                            return;
                        } else {
                            try {
                                isWait = true;
                                mTaskQueue.wait(options.getWaitPeriod());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    isWait = false;
                    /**
                     * 移除任务
                     */
                    if (mTaskQueue.size() > 0) {
                        //对任务排序后再执行
                        Collections.sort(mTaskQueue);

                        if (options.getExecutionOrder() == ThreadPoolOptions.ExecutionOrder.FIFO) {
                            task = mTaskQueue.removeFirst();
                        } else {
                            task = mTaskQueue.removeLast();
                        }
                    }

                }
                if (task != null && !task.isCancelled()) {
                    synchronized (runningSet) {
                        runningSet.add(task);
                    }

                    /**
                     * 执行移除的任务
                     */
                    try {
                        int isSucceed = task.run();
                        synchronized (runningSet) {
                            runningSet.remove(task);
                        }
                        if (BaseTask.FAIL == isSucceed && options.isReplayFailTask()) {
                            synchronized (mTaskQueue) {
                                mTaskQueue.addFirst(task);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                isSchedule = false ;
            }
        }
    }
}
