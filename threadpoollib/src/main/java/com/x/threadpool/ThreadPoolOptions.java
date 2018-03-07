package com.x.threadpool;

/**
 * 线程池配置类
 */
public class ThreadPoolOptions {

    /**
     * 执行顺序
     */
    public enum ExecutionOrder {
        FIFO, FILO
    }

    /**
     * 保持线程数
     */
    private int keep = 1;

    /**
     * 线程池数量
     */
    private int size = 1;

    /**
     * 线程池内线程的级别
     */
    private int priority = Thread.NORM_PRIORITY;

    /**
     * 线程等待时间
     */
    private int waitPeriod = 5000;

    /**
     * 执行顺序，FIFO先入先出，FILO先入后出
     */
    private ExecutionOrder executionOrder = ExecutionOrder.FIFO;

    /**
     * 得到线程池保持数
     */
    public int getKeep() {
        return keep;
    }

    /**
     * 设置线程池保持数
     */
    public void setKeep(int keep) {
        this.keep = keep;
    }

    /**
     * 得到线程池大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置线程池大小
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 得到线程池执行优先级
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 设置线程池执行优先级
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * 得到等待唤醒时间
     */
    public int getWaitPeriod() {
        return waitPeriod;
    }

    /**
     * 设置等待唤醒时间
     */
    public void setWaitPeriod(int waitPeriod) {
        this.waitPeriod = waitPeriod;
    }

    /**
     * 得到线程执行顺序
     */
    public ExecutionOrder getExecutionOrder() {
        return executionOrder;
    }

    /**
     * 设置线程执行顺序
     */
    public void setExecutionOrder(ExecutionOrder executionOrder) {
        this.executionOrder = executionOrder;
    }
}
