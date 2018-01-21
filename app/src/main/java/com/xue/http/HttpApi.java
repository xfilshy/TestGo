package com.xue.http;

import android.content.Context;

import com.elianshang.tools.DeviceTool;
import com.xue.BaseApplication;
import com.xue.bean.UserBase;
import com.xue.bean.UserMinorList;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;
import com.xue.http.parse.BaseParser;
import com.xue.parsers.UserBaseParser;
import com.xue.parsers.UserMinorListParser;
import com.xue.tools.AppTool;
import com.xue.tools.ConfigTool;
import com.xue.tools.SecretTool;
import com.xue.tools.TimestampTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfilshy on 2018/1/17.
 */

public class HttpApi {

    public static String base_url;

    /**
     * 公钥
     */
    public static String secretKey = null;

    static {
        build();
    }

    private static List<BaseKVP> default_headers = null;

    private interface Header {

        /**
         * 用户ID
         */
        String uid = "uid";

        /**
         * 用户token
         */
        String token = "token";

        /**
         * app版本
         */
        String version = "version";

        /**
         * 渠道号
         */
        String pcode = "pcode";

        /**
         * 平台(Android)
         */
        String platform = "platform";

        /**
         * 设备id
         */
        String device_id = "device-id";

        /**
         * 签名
         */
        String sign = "sign";

        /**
         * 服务器数据key
         */
        String ckey = "ckey";

        /**
         * imei
         */
        String imei = "imei";

        /**
         * 品牌
         */
        String brand = "brand";

        /**
         * 设备名
         */
        String device_name = "device-name";

        /**
         * 系统版本
         */
        String os_version = "os-version";

        /**
         * 区域ID
         */
        String zone_id = "zone-id";

        /**
         * 网络类型
         */
        String network_type = "network-type";

    }


    /**
     * 公有参数
     */
    private interface PublicParameter {

        String timestampt = "timestamp";
    }

    private interface PhoneLogin {
        String _funcation = "/account/user/login";

        String cellphone = "cellphone";

        String verifyCode = "verify_code";
    }

    private interface RecommendList {
        String _funcation = "/home/info/recommendlist";
    }

    private static void build() {
        base_url = ConfigTool.getHttpBaseUrl();
        secretKey = SecretTool.getHttpSecretKey(BaseApplication.get());
        OkHttpHandler.setOpensslSecret(SecretTool.getOpensslSecretKey(BaseApplication.get()));
    }


    private synchronized static void setDefaultHeaders(BaseKVP... kvPairs) {
        if (default_headers == null) {
            default_headers = new ArrayList<>();
            for (int i = 0; i < kvPairs.length; i++) {
                default_headers.add(kvPairs[i]);
            }
        }
    }

    private static void setDefaultHeaders(Context context) {
        setDefaultHeaders(
                new DefaultKVPBean(Header.version, AppTool.getAppVersion(context)),
                new DefaultKVPBean(Header.pcode, ConfigTool.getPcode()),
                new DefaultKVPBean(Header.platform, "Android"),
                new DefaultKVPBean(Header.device_id, BaseApplication.get().getDeviceId()),
                new DefaultKVPBean(Header.imei, BaseApplication.get().getImei()),
                new DefaultKVPBean(Header.brand, DeviceTool.getBrandName()),
                new DefaultKVPBean(Header.device_name, DeviceTool.getDeviceName()),
                new DefaultKVPBean(Header.os_version, DeviceTool.getOSVersionName())
        );
    }

    /**
     * 获取默认Header
     *
     * @return
     */
    private static List<BaseKVP> getDefaultHeaders() {
        if (default_headers == null) {
            setDefaultHeaders(BaseApplication.get());
        }

        List<BaseKVP> headerList = new ArrayList();
        headerList.addAll(default_headers);
        /**时间戳是动态值，每次请求需要重新获取，其余参数都是固定值，只需获取一次**/
        headerList.add(new DefaultKVPBean(Header.uid, BaseApplication.get().getUserId()));
        headerList.add(new DefaultKVPBean(Header.token, BaseApplication.get().getUserToken()));
        return headerList;
    }

    /**
     * 获取默认Header
     *
     * @return
     */
    private static List<BaseKVP> getStatisticHeaders() {
        if (default_headers == null) {
            setDefaultHeaders(BaseApplication.get());
        }

        List<BaseKVP> headerList = new ArrayList();
        headerList.addAll(default_headers);
        /**时间戳是动态值，每次请求需要重新获取，其余参数都是固定值，只需获取一次**/
        headerList.add(new DefaultKVPBean(Header.uid, BaseApplication.get().getUserId()));
        headerList.add(new DefaultKVPBean(Header.token, BaseApplication.get().getUserToken()));
        headerList.add(new DefaultKVPBean(Header.network_type, String.valueOf(DeviceTool.getNetWorkType(BaseApplication.get()))));
        return headerList;
    }

    /**
     * 添加Header
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(BaseKVP... kvPairs) {
        List<BaseKVP> headers = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }

    /**
     * 添加Header
     *
     * @param headers
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(List<BaseKVP> headers, BaseKVP... kvPairs) {
        if (headers == null) {
            headers = addHeaders(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }

    /**
     * 添加请求参数
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(BaseKVP... kvPairs) {
        List<BaseKVP> params = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }

    /**
     * 添加请求参数
     *
     * @param params
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(List<BaseKVP> params, BaseKVP... kvPairs) {
        if (params == null) {
            params = addParams(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }

    /**
     * 根据参数，调起请求
     */
    public static <B extends BaseBean, D, PR extends BaseParser<B, D>> DataHull<B> request(HttpDynamicParameter<PR> httpParameter) {
        httpParameter.addParameter(new DefaultKVPBean(PublicParameter.timestampt, String.valueOf(TimestampTool.currentTimeMillis() / 1000)));
        OkHttpHandler<B> handler = new OkHttpHandler();
        DataHull<B> dataHull = handler.requestData(httpParameter);
        return dataHull;
    }


    /**
     * 手机登录
     */
    public static DataHull<UserBase> phoneLogin(String cellphone, String verifyCode) {
        String url = base_url + PhoneLogin._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(PhoneLogin.cellphone, cellphone),
                new DefaultKVPBean(PhoneLogin.verifyCode, verifyCode)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserBaseParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new UserBaseParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 首页推荐列表
     */
    public static DataHull<UserMinorList> recommendList() {
        String url = base_url + RecommendList._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<UserMinorListParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), null, type, new UserMinorListParser(), 0, secretKey);

        return request(parameter);
    }
}
