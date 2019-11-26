package com.zhenglong.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhenglong.global.HttpParams;
import com.zhenglong.global.WxParams;
import com.zhenglong.redpack.entity.WxUserInfo;
import com.zhenglong.redpack.entity.AccessTokenPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
public class WxUtil {
    private static Logger log = LoggerFactory.getLogger(WxUtil.class);
    /**
     * 排序方法
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = { token, timestamp, nonce };
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
    }

    /**
     * 加密SHA1
     * @param decript
     * @return
     */
    public static String SHA1(String decript) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest
                .getInstance("SHA-1");
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    /**
     * 请求access-token
     * @param restTemplate
     * @return
     */
    public static AccessTokenPo getAccessToken(RestTemplate restTemplate) {
        String url= HttpParams.getAccessTokenUrl(WxParams.GrantType_AccessToken,
                WxParams.AppId,WxParams.AppSecret);
        System.out.println("url:"+url);
        String jsonData=restTemplate.getForObject(url,String.class);
        System.out.println("获取accesstoken返回数据："+jsonData);
        String accessToken=JSON.parseObject(jsonData).getString("access_token");
        if (accessToken==null) {
            return null;
        }
        AccessTokenPo accessTokenPo=new AccessTokenPo(accessToken,
                JSON.parseObject(jsonData).getString("expires_in"),new Date().getTime()/1000);
        return accessTokenPo;
    }

    /**
     * 请求用户列表
     * @param restTemplate
     * @param accessToken
     * @return
     */
    public static List<String> getOpenId(RestTemplate restTemplate,String accessToken) {
        String nextOpenId=null;
        String url;
        boolean tag=true;
        String jsonData;
        int localTotal=0;
        List<String> results=new ArrayList<>();
        while (tag) {
            url=HttpParams.getOpenIdsUrl(accessToken,nextOpenId);
            jsonData=restTemplate.getForObject(url,String.class);
//            System.out.println("请求openid返回数据："+jsonData);
            JSONObject object=JSON.parseObject(jsonData);
            localTotal=localTotal+object.getInteger("count");
            if (localTotal==0) {
                return null;
            }
            if (localTotal>=object.getInteger("total")) {
                tag=false;
            }
            List<String> a=(object.getJSONObject("data").getJSONArray("openid")).toJavaList(String.class);
            results.addAll(a);
        }
        return results;
    }

    public static List<WxUserInfo> batchGetWxUserInfo(String accessToken,List<String> openids,RestTemplate restTemplate) {
        boolean tag=true;
        List<WxUserInfo> wxUserInfoList = new ArrayList<>();
        String url = HttpParams.batchGetUserInfoUrl(accessToken);
        int start=0;
        int size=100;
        List<String> subOpenids;
        System.out.println("数据拉取中");
        while (tag) {
            int end=start+size;
            if (end>openids.size()) {
                end=openids.size()-1;
                tag=false;
            }
            subOpenids=openids.subList(start,end);
            start=end;
            String jsonData = restTemplate.postForObject(url, getRequest(subOpenids), String.class);
//            System.out.println("请求用户信息返回数据："+jsonData);
            JSONArray arrayData = JSON.parseObject(jsonData).getJSONArray("user_info_list");
            for (int i = 0; i < arrayData.size(); i++) {
                WxUserInfo wxUserInfo = JSON.parseObject(arrayData.getString(i), WxUserInfo.class);
                wxUserInfo.setSubscribe_time(timeStamp2Date(wxUserInfo.getSubscribe_time(),null));
                wxUserInfoList.add(wxUserInfo);
            }
            System.out.print("---");
        }
        System.out.println("总共拉取数量："+wxUserInfoList.size());
        return wxUserInfoList;
    }

    private static String getRequest(List<String> openids) {
        String prefix="{\"user_list\": [";
        String suffix="]}";
        String body="{\"openid\":\""+openids.get(0)+"\",\"lang\":\"zh_CN\"}";
        for (int i=1; i<openids.size(); i++) {
            body=body+",{\"openid\":\""+openids.get(i)+"\",\"lang\":\"zh_CN\"}";
        }
        return (prefix+body+suffix);
    }

    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

}
