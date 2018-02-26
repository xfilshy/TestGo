package com.xue.asyns;


import com.xue.http.hook.BaseBean;
import com.xue.http.impl.DataHull;

/**
 * 异步任务接口类（网络任务）
 */
public interface HttpAsyncTaskInterface<T extends BaseBean> {

    /**
     * 异步任务开始前
     */
    public boolean onPreExecute();

    /**
     * 异步任务执行
     */
    public DataHull<T> doInBackground();

    /**
     * 异步任务完成
     */
    public void onPostExecute(int updateId, T result);

    /**
     * 结束最终回调
     */
    public void finish();

}