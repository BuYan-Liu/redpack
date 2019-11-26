package com.zhenglong.global;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */

public class HttpParams {
    private String url;
    private String method;

    private HttpParams() {}

    /**
     * 返回获取AccessToken所需Url
     * @param grantType
     * @param appId
     * @param secret
     * @return
     */
    public static String getAccessTokenUrl(String grantType,String appId,String secret) {
        return "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grantType+"&appid="+appId+"&secret="+secret;
    }

    /**
     * 返回获取openid的url
     * @param accessToken
     * @param nextOpenId 若没有请填写null
     * @return
     */
    public static String getOpenIdsUrl(String accessToken,String nextOpenId) {
        return nextOpenId==null?"https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken:
                "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken+"&next_openid="+nextOpenId;
    }

    /**
     * 返回获取单个用户信息的Url
     * @param accessToken
     * @param openId
     * @return
     */
    public static String getUserInfoUrl(String accessToken,String openId) {
        return "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
    }

    /**
     * 返回批量获取用户信息的Url
     * @param accessToken
     * @return
     */
    public static String batchGetUserInfoUrl(String accessToken) {
        return "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token="+accessToken;
    }

    /**
     * 返回付款url
     * @return
     */
    public static String getPaymentUrl() {
        return "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    }
}
