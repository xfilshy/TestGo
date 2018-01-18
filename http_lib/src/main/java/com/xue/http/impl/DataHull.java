package com.xue.http.impl;

import com.xue.http.hook.BaseBean;

/**
 * 数据壳对象，携带请求状态，基础数据状态
 */
public class DataHull<T extends BaseBean> {

    /**
     * 请求数据到分派给解析器解析，所有状态
     * <p/>
     * DATA_IS_NULL                                 请求返回数据是空串或NULL时
     * <p/>
     * DATA_PARSE_EXCEPTION                         在进入解析器后，parse方法在解析异常时
     * <p/>
     * CONNECTION_FAIL                              请求网络时，连接失败或超时，IO异常时
     * <p/>
     * RESPONSE_CODE_ERR                            请求返回状态码非200时
     * <p/>
     * DATA_IS_INTEGRITY                            没有出现任何异常，完整完成整个过程时
     * <p/>
     * PARAMS_IS_NULL                               参数为空时
     * <p/>
     * METHOD_IS_ERR                                请求不是get或post时
     * <p/>
     * DATA_PARSER_IS_NULL                          传入的解析器对象为空时
     * <p/>
     * DATA_CAN_NOT_PARSE                           数据对象不满足解析器 canParse验证时
     * <p/>
     * DATA_IS_ERR                                  为解析方法提供源数据时，错误
     * <p/>
     * DATA_NO_UPDATE                               接口数据无更新
     */
    public interface DataType {

        /**
         * 数据为空
         */
        public int DATA_IS_NULL = 0x100;

        /**
         * 数据解析错误
         */
        public int DATA_PARSE_EXCEPTION = 0x101;

        /**
         * 连接失败
         */
        public int CONNECTION_FAIL = 0x102;

        /**
         * 连接返回状态非200
         */
        public int RESPONSE_CODE_ERR = 0x103;

        /**
         * 数据完整
         */
        public int DATA_IS_INTEGRITY = 0x104;

        /**
         * 请求参数为空
         */
        public int PARAMS_IS_NULL = 0x105;

        /**
         * 请求方式不正确
         */
        public int METHOD_IS_ERR = 0x106;

        /**
         * 解析器为空
         */
        public int DATA_PARSER_IS_NULL = 0x107;

        /**
         * 数据不符合解析头文件判断
         */
        public int DATA_CAN_NOT_PARSE = 0x108;

        /**
         * 为解析方法提供源数据时，错误
         */
        public int DATA_IS_ERR = 0x109;

        /**
         * 接口数据无更新
         */
        public int DATA_NO_UPDATE = 0x110;
    }

    /**
     * 数据状态
     */
    private int dataType;

    /**
     * 请求返回的实体
     */
    private T dataEntity;

    /**
     * 更新视图的ID
     */
    private int dataId;

    /**
     * 服务器信息
     */
    private String message;

    /**
     * 接口状态码
     */
    private int status = -2;

    /**
     * 原始数据
     */
    private String originalData;

    /**
     * 功能
     */
    private String function;

    /**
     * 得到数据状态
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * 设置数据状态
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * 得到数据实体
     */
    public T getDataEntity() {
        return dataEntity;
    }

    /**
     * 设置数据实体
     */
    public void setDataEntity(T dataEntity) {
        this.dataEntity = dataEntity;
    }

    /**
     * 得到数据ID
     */
    public int getDataId() {
        return dataId;
    }

    /**
     * 设置数据D
     */
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    /**
     * 得到服务器信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置服务器信息
     */
    public void setMessage(String message) {
        this.message = message;
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

    /**
     * 得到原始数据
     */
    public String getOriginalData() {
        return originalData;
    }

    /**
     * 得到原始数据
     */
    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }

    /**
     * 设置功能
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * 得到功能
     */
    public String getFunction() {
        return function;
    }
}
