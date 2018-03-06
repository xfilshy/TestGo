package com.xue.parsers;

import com.elianshang.tools.DateTool;
import com.xue.http.parse.MainParser;
import com.xue.tools.TimestampTool;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 移动端接口，解析器父类 ｛ code , msg , timestamp, body:{...} ｝ 针对返回模式这样的解析
 */
public abstract class MasterParser<T> extends MainParser<T, JSONObject> {

    /**
     * 接口返回状态 0正常
     */
    protected final String CODE = "code";

    /**
     * 接口返回的信息
     */
    protected final String MSG = "msg";

    /**
     * 接口返回数据节点
     */
    protected final String DATA = "data";

    /**
     * 时间戳
     */
    protected final String TIMESTAMP = "timestamp";

    /**
     * 接口状态类型
     */
    public interface STATE {

        /**
         * 接口正常返回
         */
        int NORMAL = 0;

        /**
         * 接口无更新
         */
        int NOUPDATE = 2;

        /**
         * 无数据
         */
        int NODATA = 3;

        /**
         * 数据异常
         */
        int DATAEXCEPTION = 4;

        /**
         * 参数错误
         */
        int PARAMETERSERR = 5;

        /**
         * 系统异常
         */
        int SYSTEMEXCEPTION = 6;

        /**
         * token非法
         */
        int TOKENILLEGAL = 7;
    }

    public MasterParser() {
        super();
    }

    @Override
    protected JSONObject initData(String data) throws Exception {
        JSONObject jsonObject = new JSONObject(data);
        return jsonObject;
    }

    @Override
    protected final boolean canParse(JSONObject data) {
        try {
            if (!data.has(CODE)) {
                return false;
            }

            setStatus(getInt(data, CODE));

            if (has(data, TIMESTAMP)) {
                long timestamp = getLong(data, TIMESTAMP);
                long cur = DateTool.getDateLong();
                TimestampTool.timeOffset = timestamp * 1000 - cur;
            }

            setMessage(getString(data, MSG));

            if (isNormalData()) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected JSONObject getData(String data) throws JSONException {
        JSONObject object = null;
        if (getStatus() == STATE.NORMAL) {
            object = new JSONObject(data);
            object = getJSONObject(object, DATA);
        }

        return object;
    }

    @Override
    public boolean hasUpdate() {
        return getStatus() != STATE.NOUPDATE;
    }

    /**
     * 是否是正常
     */
    public boolean isNormalData() {
        return getStatus() == STATE.NORMAL;
    }
}
