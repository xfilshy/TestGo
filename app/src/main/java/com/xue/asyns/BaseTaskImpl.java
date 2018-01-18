package com.xue.asyns;


import com.elianshang.threadpool.BaseTask;
import com.elianshang.threadpool.BaseThreadPool;
import com.elianshang.threadpool.ThreadPoolFactory;
import com.elianshang.threadpool.ThreadPoolOptions;

public abstract class BaseTaskImpl implements BaseTask {
	
	/**
	 * 是否取消加载
	 * */
	protected boolean isCancel = false ;
	
	/**
	 * 线程池
	 * */
	protected static final BaseThreadPool mThreadPool;

	static {// 初始化线程池
		ThreadPoolOptions options = new ThreadPoolOptions();
		options.setPriority(Thread.NORM_PRIORITY + 1);
		options.setSize(10);
		options.setKeep(1);
		options.setWaitPeriod(1000);
		options.setReplayFailTask(false);
		options.setExecutionOrder(ThreadPoolOptions.ExecutionOrder.FIFO);
		mThreadPool = ThreadPoolFactory.create(options);
	}
	
	@Override
	public void cancel() {
		this.isCancel = true;
		if(mThreadPool != null){
			mThreadPool.removeTask(this);
		}
	}

	@Override
	public boolean isCancelled() {
		return this.isCancel;
	}
	
	@Override
	public int compareTo(BaseTask another) {
		return 0;
	}
}
