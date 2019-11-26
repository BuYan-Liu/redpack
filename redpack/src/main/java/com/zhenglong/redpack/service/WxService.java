package com.zhenglong.redpack.service;

import com.zhenglong.redpack.entity.WxUserInfo;
import com.zhenglong.redpack.entity.AccessTokenPo;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface WxService {
    void saveAccessToken(AccessTokenPo accessToken);

    AccessTokenPo getAccessToken(RestTemplate restTemplate);

    List<WxUserInfo> getWxUserList(RestTemplate restTemplate);

    void saveUsers(List<WxUserInfo> wxUserInfoList);

    List<WxUserInfo> getUserList();

    String getUpdateRecord();
}
