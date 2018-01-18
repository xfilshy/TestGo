package com.xue.http.parse;

import android.text.TextUtils;

import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;
import com.xue.http.hook.BaseBean;

/**
 * 解析器接口类
 */
public abstract class BaseParser<T extends BaseBean, D> {

    /**
     * 服务器信息
     */
    private String message;

    /**
     * 接口状态码
     */
    private int status;

    /**
     * 数据key，数据文件唯一key
     */
    private String dataKey;

    /**
     * 解析入口
     */
    public T initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        if (TextUtils.isEmpty(data)) {
            throw new DataIsNullException();
        }

        D od = null;
        try {
            od = initData(data);
        } catch (Exception e) {
            throw new DataIsErrException();
        }

        if (canParse(od)) {
            D d = null;
            try {
                d = getData(data);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataIsErrException();
            }
            if (d == null) {
                throw new DataIsErrException();
            }

            T t;
            try {
                t = parse(d);
                if (t != null) {
                    t.setDataKey(dataKey);
                }
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParseException();
            }

        } else {
            boolean hasUpdate = hasUpdate();
            if (!hasUpdate) {
                throw new DataNoUpdateException();
            } else {
                throw new JsonCanNotParseException();
            }
        }
    }

    protected abstract D initData(String data) throws Exception;

    public abstract T parse(D data) throws Exception;

    /**
     * 针对不同的接口类型（如：移动端接口，主站接口，支付接口等）进行不同实现，
     * 如果独立接口，请实现为返回  true，否则不会进入解析方法，并抛出JsonCanNotParseException
     */
    protected abstract boolean canParse(D data);

    /**
     * 针对不同的接口类型，给parse方法吐出不同的数据
     */
    protected abstract D getData(String data) throws Exception;

    /**
     * 设置服务器消息
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    /**
     * 得到服务器信息
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 判断接口是否有更新
     */
    public boolean hasUpdate() {
        return true;
    }

    /**
     * 得到数据唯一key
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * 设置数据唯一key
     */
    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    /**
     * 得到接口状态码
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置接口状态码
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
