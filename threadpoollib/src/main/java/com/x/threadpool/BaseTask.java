package com.x.threadpool;

public interface BaseTask extends Comparable<BaseTask> {

    /**
     * 执行方法，执行业务代码，并给出执行结果
     */
    public void run();

    /**
     * 终止，非异常终止，存在延时
     */
    public void cancel();

    /**
     * 是否取消
     */
    public boolean isCancelled();
}