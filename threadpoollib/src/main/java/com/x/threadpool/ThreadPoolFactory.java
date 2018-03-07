package com.x.threadpool;

/**
 * 线程池工厂
 */
public class ThreadPoolFactory {

    /**
     * 根据配置创建线程池，如果配置为空，提供默认线程池
     */
    public static BaseThreadPool create(ThreadPoolOptions options) {
        return initialize(options);
    }

    private static BaseThreadPool initialize(ThreadPoolOptions options) {
        if (options == null) {
            options = new ThreadPoolOptions();
        }

        BaseThreadPool threadPool = new BaseThreadPool(options);
        return threadPool;
    }
}
