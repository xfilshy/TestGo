package com.xue.http;

import android.content.Context;

import com.elianshang.tools.DeviceTool;
import com.xue.BaseApplication;
import com.xue.bean.AcademicList;
import com.xue.bean.CityList;
import com.xue.bean.FollowResult;
import com.xue.bean.IndustryList;
import com.xue.bean.MomentInfoList;
import com.xue.bean.OrderCommentInfo;
import com.xue.bean.OrderCommentList;
import com.xue.bean.User;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserEducationInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.bean.UserList;
import com.xue.bean.UserTagInfo;
import com.xue.bean.UserWorkInfo;
import com.xue.bean.WalletDecorator;
import com.xue.bean.WalletTradeList;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;
import com.xue.oss.OssConfig;
import com.xue.oss.OssConfigParser;
import com.xue.oss.SignConentParser;
import com.xue.oss.SignContent;
import com.xue.parsers.AcademicListParser;
import com.xue.parsers.CityListParser;
import com.xue.parsers.FollowResultParser;
import com.xue.parsers.IndustryListParser;
import com.xue.parsers.MomentInfoListParser;
import com.xue.parsers.MomentInfoParser;
import com.xue.parsers.OrderCommentInfoParser;
import com.xue.parsers.OrderCommentParser;
import com.xue.parsers.UserDetailInfoParser;
import com.xue.parsers.UserEducationInfoParser;
import com.xue.parsers.UserExpertInfoParser;
import com.xue.parsers.UserParser;
import com.xue.parsers.UserTagInfoParser;
import com.xue.parsers.UserWorkInfoParser;
import com.xue.parsers.UsetListParser;
import com.xue.parsers.WalletDecoratorParser;
import com.xue.parsers.WalletTradeListParser;
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
     * 获取用户信息 他人的
     */
    private interface GetShowInfoParameter {

        String _funcation = "/user/info/getshowinfo";

        String uid = "uid";
    }

    /**
     * 更新用户 UserDetailInfo
     */
    private interface UpdateUserDetailInfoParameter {
        String _funcation = "/user/detail/update";

        String gender = "gender";

        String homeTown = "hometown";

        String realname = "realname";

        String profile = "profile";

        String cover = "cover";

        String intro = "intro";
    }

    /**
     * 更新用户 UserExpertInfo
     */
    private interface UpdateUserExpertInfoParameter {
        String _funcation = "/user/expert/update";

        String signature = "signature";

        String service_fee = "service_fee";

        String work_card_img = "work_card_img";

        String busine_card_img = "busine_card_img";

    }

    /**
     * 创建用户 UserEducation 一条
     */
    private interface CreateUserEducationParameter {
        String _funcation = "/user/education/create";

        String school_name = "school_name";

        String major_name = "major_name";

        String academic_type = "academic_type";

        String describe = "describe";

        String begin_at = "begin_at";

        String end_at = "end_at";

    }

    /**
     * 更新用户 UserEducation 一条
     */
    private interface UpdateUserEducationParameter {
        String _funcation = "/user/education/update";

        String id = "id";

        String school_name = "school_name";

        String major_name = "major_name";

        String academic_type = "academic_type";

        String describe = "describe";

        String begin_at = "begin_at";

        String end_at = "end_at";

    }

    /**
     * 删除用户 UserEducation 一条
     */
    private interface DeleteUserEducationParameter {
        String _funcation = "/user/education/delete";

        String id = "id";

    }

    /**
     * 创建用户 UserWork 一条
     */
    private interface CreateUserWorkParameter {

        String _funcation = "/user/work/create";

        String company_name = "company_name";

        String industry_id = "industry_id";

        String direction_name = "direction_name";

        String position_name = "position_name";

        String describe = "describe";

        String begin_at = "begin_at";

        String end_at = "end_at";

    }


    /**
     * 更新用户 UserWork 一条
     */
    private interface UpdateUserWorkParameter {

        String _funcation = "/user/work/update";

        String id = "id";

        String company_name = "company_name";

        String industry_id = "industry_id";

        String direction_name = "direction_name";

        String position_name = "position_name";

        String describe = "describe";

        String begin_at = "begin_at";

        String end_at = "end_at";

    }


    /**
     * 删除用户 UserWork 一条
     */
    private interface DeleteUserWorkParameter {

        String _funcation = "/user/work/delete";

        String id = "id";

    }

    /**
     * 创建用户 UserTag 一条
     */
    private interface CreateUserTagParameter {

        String _funcation = "/user/tag/create";

        String tag_name = "tag_name";

    }

    /**
     * 更新用户 UserTag 一条
     */
    private interface UpdateUserTagParameter {

        String _funcation = "/user/tag/replace";

        String tag_id = "tag_id";

        String tag_name = "tag_name";

    }

    /**
     * 删除用户 UserTag 一条
     */
    private interface DeleteUserTagParameter {

        String _funcation = "/user/tag/delete";

        String tag_id = "tag_id";

    }

    /**
     * 获取学历列表
     */
    private interface GetAcademicListParameter {
        String _funcation = "/user/education/getacademiclist";
    }

    /**
     * 获取行业列表
     */
    private interface GetIndustryListParameter {
        String _funcation = "/user/work/getindustrylist";
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

    /**
     * 二级城市列表
     */
    private interface ResCityParameter {
        String _funcation = "/res/city/getall";

        String content = "content";
    }

    /**
     * 获取朋友圈列表
     */
    private interface GetMomentInfoList {

        String _funcation = "/moment/info/getlist";

        String uid = "uid";

        String page = "page";

        String page_size = "page_size";

        String order = "order";
    }

    /**
     * 获取最近一条朋友圈
     */
    private interface GetLastMomentInfo {

        String _funcation = "/moment/info/getlastinfo";

        String uid = "uid";

    }

    /**
     * 创建朋友圈 一条
     */
    private interface CreateMomentInfo {

        String _funcation = "/moment/info/create";

        String uid = "uid";

        String text = "text";

        String res_list = "res_list";
    }


    /**
     * 更新朋友圈 一条
     */
    private interface UpdateMomentInfo {

        String _funcation = "/moment/info/update";

        String id = "id";

        String text = "text";

        String res_list = "res_list";
    }


    /**
     * 删除朋友圈 一条
     */
    private interface DeleteMomentInfo {

        String _funcation = "/moment/info/delete";

        String id = "id";
    }

    /**
     * 添加关注 一条
     */
    private interface CreateFollow {

        String _funcation = "/friend/follow/create";

        String uid = "uid";
    }

    /**
     * 删除关注 一条
     */
    private interface DeleteFollow {

        String _funcation = "/friend/follow/delete";

        String uid = "uid";
    }

    /**
     * 创建评论
     */
    private interface CreateOrderComment {

        String _funcation = "/order/comment/create";

        String order_id = "order_id";

        String uid = "uid";

        String score = "score";

        String content = "content";
    }

    /**
     * 获取用户评论列表
     */
    private interface GetOrderComment {

        String _funcation = "/order/comment/getlist";

        String uid = "uid";

        String offset = "offset";

        String limit = "limit";
    }

    /**
     * 获取用户最近的一条评论
     */
    private interface GetLastOrderComment {

        String _funcation = "/order/comment/getlastinfo";

        String uid = "uid";

        String page = "page";

        String page_size = "page_size";
    }

    /**
     * 获取交易记录
     */
    private interface GetWalletTradeList {

        String _funcation = "/wallet/trade/getlist";

        String currency_type = "currency_type";

        String offset = "offset";

        String limit = "limit";

    }

    /**
     * 获取钱包数据  这个接口暂时不管
     */
    private interface GetWalletInfo {

        String _funcation = "/wallet/info/getinfo";

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
    public static <B> DataHull<B> request(HttpDynamicParameter<B> httpParameter) {
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
        HttpDynamicParameter<User> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 获取个人用户信息  自己的
     */
    public static DataHull<User> userInfo() {
        String url = base_url + UserInfoParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<User> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new UserParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取个人用户信息  他人的
     */
    public static DataHull<User> getDetailInfo(String uid) {
        String url = base_url + GetShowInfoParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetShowInfoParameter.uid, uid)
        );
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<User> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新UserDetailInfo
     */
    public static DataHull<UserDetailInfo> updateUserDetailInfo(String realName, String gender, String homeTown, String profile, String cover, String intro) {
        String url = base_url + UpdateUserDetailInfoParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserDetailInfoParameter.realname, realName),
                new DefaultKVPBean(UpdateUserDetailInfoParameter.gender, gender),
                new DefaultKVPBean(UpdateUserDetailInfoParameter.homeTown, homeTown),
                new DefaultKVPBean(UpdateUserDetailInfoParameter.profile, profile),
                new DefaultKVPBean(UpdateUserDetailInfoParameter.cover, cover),
                new DefaultKVPBean(UpdateUserDetailInfoParameter.intro, intro)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserDetailInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserDetailInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新UserExpertInfo
     */
    public static DataHull<UserExpertInfo> updateUserExpertInfo(String signature, String serviceFee, String workCardImg, String businessCardImg) {
        String url = base_url + UpdateUserExpertInfoParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserExpertInfoParameter.signature, signature),
                new DefaultKVPBean(UpdateUserExpertInfoParameter.service_fee, serviceFee),
                new DefaultKVPBean(UpdateUserExpertInfoParameter.work_card_img, workCardImg),
                new DefaultKVPBean(UpdateUserExpertInfoParameter.busine_card_img, businessCardImg)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserExpertInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserExpertInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 创建 一条 UserEducation
     */
    public static DataHull<UserEducationInfo> createUserEducation(String schoolName, String majorName, String academicType, String describe, String beginAt, String endAt) {
        String url = base_url + CreateUserEducationParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateUserEducationParameter.school_name, schoolName),
                new DefaultKVPBean(CreateUserEducationParameter.major_name, majorName),
                new DefaultKVPBean(CreateUserEducationParameter.academic_type, academicType),
                new DefaultKVPBean(CreateUserEducationParameter.describe, describe),
                new DefaultKVPBean(CreateUserEducationParameter.begin_at, beginAt),
                new DefaultKVPBean(CreateUserEducationParameter.end_at, endAt)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserEducationInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserEducationInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新 一条 UserEducation
     */
    public static DataHull<UserEducationInfo> updateUserEducation(String id, String schoolName, String majorName, String academicType, String describe, String beginAt, String endAt) {
        String url = base_url + UpdateUserEducationParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserEducationParameter.id, id),
                new DefaultKVPBean(UpdateUserEducationParameter.school_name, schoolName),
                new DefaultKVPBean(UpdateUserEducationParameter.major_name, majorName),
                new DefaultKVPBean(UpdateUserEducationParameter.academic_type, academicType),
                new DefaultKVPBean(UpdateUserEducationParameter.describe, describe),
                new DefaultKVPBean(UpdateUserEducationParameter.begin_at, beginAt),
                new DefaultKVPBean(UpdateUserEducationParameter.end_at, endAt)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserEducationInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserEducationInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除 一条 UserEducation
     */
    public static DataHull<UserEducationInfo> deleteUserEducation(String id) {
        String url = base_url + DeleteUserEducationParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(DeleteUserEducationParameter.id, id)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserEducationInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserEducationInfoParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 创建 一条 UserEducation
     */
    public static DataHull<UserWorkInfo> createUserWork(String companyName, String industryId, String positionName, String directionName, String describe, String beginAt, String endAt) {
        String url = base_url + CreateUserWorkParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateUserWorkParameter.company_name, companyName),
                new DefaultKVPBean(CreateUserWorkParameter.industry_id, industryId),
                new DefaultKVPBean(CreateUserWorkParameter.position_name, positionName),
                new DefaultKVPBean(CreateUserWorkParameter.direction_name, directionName),
                new DefaultKVPBean(CreateUserWorkParameter.describe, describe),
                new DefaultKVPBean(CreateUserWorkParameter.begin_at, beginAt),
                new DefaultKVPBean(CreateUserWorkParameter.end_at, endAt)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserWorkInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserWorkInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新 一条 UserEducation
     */
    public static DataHull<UserWorkInfo> updateUserWork(String id, String companyName, String industryId, String positionName, String directionName, String describe, String beginAt, String endAt) {
        String url = base_url + UpdateUserWorkParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserWorkParameter.id, id),
                new DefaultKVPBean(UpdateUserWorkParameter.company_name, companyName),
                new DefaultKVPBean(UpdateUserWorkParameter.industry_id, industryId),
                new DefaultKVPBean(UpdateUserWorkParameter.position_name, positionName),
                new DefaultKVPBean(UpdateUserWorkParameter.direction_name, directionName),
                new DefaultKVPBean(UpdateUserWorkParameter.describe, describe),
                new DefaultKVPBean(UpdateUserWorkParameter.begin_at, beginAt),
                new DefaultKVPBean(UpdateUserWorkParameter.end_at, endAt)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserWorkInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserWorkInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除 一条 UserEducation
     */
    public static DataHull<UserWorkInfo> deleteUserWork(String id) {
        String url = base_url + DeleteUserWorkParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(DeleteUserWorkParameter.id, id)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserWorkInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserWorkInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除 一条 UserEducation
     */
    public static DataHull<UserTagInfo> createUserTag(String tagName) {
        String url = base_url + CreateUserTagParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateUserTagParameter.tag_name, tagName)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserTagInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserTagInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除 一条 UserEducation
     */
    public static DataHull<UserTagInfo> updateUserTag(String tagId, String tagName) {
        String url = base_url + UpdateUserTagParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateUserTagParameter.tag_id, tagId),
                new DefaultKVPBean(UpdateUserTagParameter.tag_name, tagName)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserTagInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserTagInfoParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 删除 一条 UserEducation
     */
    public static DataHull<UserTagInfo> deleteUserTag(String tagId) {
        String url = base_url + DeleteUserTagParameter._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(DeleteUserTagParameter.tag_id, tagId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserTagInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new UserTagInfoParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 获取 一条 MomentInfoList
     */
    public static DataHull<MomentInfoList> getMomentInfoList(String uid, String page, String pageSize, String order) {
        String url = base_url + GetMomentInfoList._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetMomentInfoList.uid, uid),
                new DefaultKVPBean(GetMomentInfoList.page, page),
                new DefaultKVPBean(GetMomentInfoList.page_size, pageSize),
                new DefaultKVPBean(GetMomentInfoList.order, order)
        );
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<MomentInfoList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new MomentInfoListParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取最近 一条 MomentInfo
     */
    public static DataHull<MomentInfoList.MomentInfo> getLastMomentInfo(String uid) {
        String url = base_url + GetLastMomentInfo._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetLastMomentInfo.uid, uid)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<MomentInfoList.MomentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new MomentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 创建 一条 MomentInfo
     */
    public static DataHull<MomentInfoList.MomentInfo> createMomentInfo(String text, String resList) {
        String url = base_url + CreateMomentInfo._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateMomentInfo.text, text),
                new DefaultKVPBean(CreateMomentInfo.res_list, resList)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<MomentInfoList.MomentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new MomentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 更新 一条 MomentInfo
     */
    public static DataHull<MomentInfoList.MomentInfo> updateMomentInfo(String id, String text, String resList) {
        String url = base_url + UpdateMomentInfo._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UpdateMomentInfo.id, id),
                new DefaultKVPBean(UpdateMomentInfo.text, text),
                new DefaultKVPBean(UpdateMomentInfo.res_list, resList)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<MomentInfoList.MomentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new MomentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除 一条 MomentInfo
     */
    public static DataHull<MomentInfoList.MomentInfo> deleteMomentInfo(String id) {
        String url = base_url + DeleteMomentInfo._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(DeleteMomentInfo.id, id)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<MomentInfoList.MomentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new MomentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 添加关注
     */
    public static DataHull<FollowResult> createFollow(String uid) {
        String url = base_url + CreateFollow._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateFollow.uid, uid)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<FollowResult> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new FollowResultParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 删除关注
     */
    public static DataHull<FollowResult> deleteFollow(String uid) {
        String url = base_url + DeleteFollow._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(DeleteFollow.uid, uid)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<FollowResult> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new FollowResultParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 创建关注
     */
    public static DataHull<OrderCommentList.Comment> createOrderComment(String uid, String orderId, String score, String content) {
        String url = base_url + CreateOrderComment._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(CreateOrderComment.uid, uid),
                new DefaultKVPBean(CreateOrderComment.order_id, orderId),
                new DefaultKVPBean(CreateOrderComment.score, score),
                new DefaultKVPBean(CreateOrderComment.content, content)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<OrderCommentList.Comment> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new OrderCommentParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取评论列表
     */
    public static DataHull<OrderCommentInfo> getOrderCommentInfo(String uid, String offset, String limit) {
        String url = base_url + GetOrderComment._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetOrderComment.uid, uid),
                new DefaultKVPBean(GetOrderComment.offset, offset),
                new DefaultKVPBean(GetOrderComment.limit, limit)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<OrderCommentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new OrderCommentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取最近评论
     */
    public static DataHull<OrderCommentInfo> getLastOrderCommentInfo(String uid) {
        String url = base_url + GetLastOrderComment._funcation;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetLastOrderComment.uid, uid)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<OrderCommentInfo> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new OrderCommentInfoParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取学历列表
     */
    public static DataHull<AcademicList> getAcademicList() {
        String url = base_url + GetAcademicListParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<AcademicList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new AcademicListParser(), 0, secretKey);

        return request(parameter);
    }


    /**
     * 获取行业列表
     */
    public static DataHull<IndustryList> getIndustryList() {
        String url = base_url + GetIndustryListParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<IndustryList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new IndustryListParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 首页推荐列表
     */
    public static DataHull<UserList> recommendList() {
        String url = base_url + RecommendListParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<UserList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new UsetListParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取OSS配置
     */
    public static DataHull<OssConfig> ossConfig() {
        String url = base_url + OssConfigParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<OssConfig> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new OssConfigParser(), 0, secretKey);

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
        HttpDynamicParameter<SignContent> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new SignConentParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取城市列表
     */
    public static DataHull<CityList> resCityList() {
        String url = base_url + ResCityParameter._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<CityList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new CityListParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取钱包信息
     */
    public static DataHull<WalletDecorator> getWalletInfo() {
        String url = base_url + GetWalletInfo._funcation;
        int type = BaseHttpParameter.Type.GET;
        HttpDynamicParameter<WalletDecorator> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), null, type, new WalletDecoratorParser(), 0, secretKey);

        return request(parameter);
    }

    /**
     * 获取钱包交易记录
     */
    public static DataHull<WalletTradeList> getWalletTradeList(String currencyType, String offset, String limit) {
        String url = base_url + GetWalletTradeList._funcation;
        int type = BaseHttpParameter.Type.GET;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(GetWalletTradeList.currency_type, currencyType),
                new DefaultKVPBean(GetWalletTradeList.offset, offset),
                new DefaultKVPBean(GetWalletTradeList.limit, limit)
        );
        HttpDynamicParameter<WalletTradeList> parameter = new HttpDynamicParameter(url, getDefaultHeaders(), params, type, new WalletTradeListParser(), 0, secretKey);

        return request(parameter);
    }
}
