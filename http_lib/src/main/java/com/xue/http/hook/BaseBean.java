package com.xue.http.hook;

import java.io.Serializable;

/**
 * 所有项目中对象都必须实现这个接口
 */
public interface BaseBean extends Serializable {

    /**
     * 设置dataKey的方法，自己实现
     */
    void setDataKey(String dataKey) ;

    /**
     * 得到dataKey的方法，自己实现
     */
    String getDataKey();
}
