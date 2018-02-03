package com.xue.http;

import android.content.Context;

import com.elianshang.tools.DeviceTool;
import com.xue.BaseApplication;
import com.xue.bean.CityList;
import com.xue.bean.User;
import com.xue.bean.UserInfoDetail;
import com.xue.bean.UserMinorList;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;
import com.xue.http.parse.BaseParser;
import com.xue.oss.OssConfig;
import com.xue.oss.OssConfigParser;
import com.xue.oss.SignConentParser;
import com.xue.oss.SignContent;
import com.xue.parsers.CityListParser;
import com.xue.parsers.UserInfoDetailParser;
import com.xue.parsers.UserMinorListParser;
import com.xue.parsers.UserParser;
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

    /**
     * 请求地址
     */
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

    private interface PhoneLoginParameter {
        String _funcation = "/account/user/login";

        String cellphone = "cellphone";

        String verifyCode = "verify_code";
    }

    /**
     * 获取用户信息 自己的
     */
    private interface UserInfoParameter {

        String _funcation = "/user/info/getinfo";
    }

    /**
     * 更新用户 UserInfoDetail
     */
    private interface UpdateUserInfoDetailParameter {
        String _funcation = "/user/detail/update";

        String gender = "gender";

        String region_id = "region_id";

        String realname = "realname";

        String profile = "profile";

        String cover = "cover";

        String intro = "intro";
    }

    private interface RecommendListParameter {
        String _funcation = "/home/info/recommendlist";
    }

    private interface OssConfigParameter {
        String _funcation = "/app/init/config";
    }

    private interface SignConetntParameter {
        String _funcation = "/res/oss/signconent";

        String content = "content";
    }

    private interface ResCityParameter {
        String _funcation = "/res/city/getall";

        String content = "content";
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
    public static DataHull<User> phoneLogin(String cellphone, String verifyCode) {
        String url = base_url + PhoneLoginParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(PhoneLoginParameter.cellphone, cellphone),
                new DefaultKVPBean(PhoneLoginParameter.verifyCode, verifyCode)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new UserParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 获取个人用户信息  全量
     */
    public static DataHull<User> userInfo() {
        String url = base_url + UserInfoParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<UserParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), null, type, new UserParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新UserInfoDetail
     */
    public static DataHull<UserInfoDetail> updateUserInfoDetail(String realName, String gender, String regionId, String profile, String cover, String intro) {
        String url = base_url + UpdateUserInfoDetailParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserInfoDetailParameter.realname, realName),
                new DefaultKVPBean(UpdateUserInfoDetailParameter.gender, gender),
                new DefaultKVPBean(UpdateUserInfoDetailParameter.region_id, regionId),
                new DefaultKVPBean(UpdateUserInfoDetailParameter.profile, profile),
                new DefaultKVPBean(UpdateUserInfoDetailParameter.cover, cover),
                new DefaultKVPBean(UpdateUserInfoDetailParameter.intro, intro)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserInfoDetailParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new UserInfoDetailParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 首页推荐列表
     */
    public static DataHull<UserMinorList> recommendList() {
        String url = base_url + RecommendListParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<UserMinorListParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), null, type, new UserMinorListParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取OSS配置
     */
    public static DataHull<OssConfig> ossConfig() {
        String url = base_url + OssConfigParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<OssConfigParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), null, type, new OssConfigParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取OSS 签名
     */
    public static DataHull<SignContent> signContent(String content) {
        String url = base_url + SignConetntParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(SignConetntParameter.content, content)
        );
        HttpDynamicParameter<SignConentParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new SignConentParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取城市列表
     */
    public static DataHull<CityList> resCityList() {
        String url = base_url + ResCityParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<CityListParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), null, type, new CityListParser(), 0, secretKey);

        return request(parameter);
    }
}
