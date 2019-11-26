package com.zhenglong.redpack.operator;

import com.zhenglong.redpack.entity.WxUserInfo;
import com.zhenglong.redpack.entity.AccessTokenPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WxOperator {
    void saveAccessToken(AccessTokenPo accessToken);

    AccessTokenPo getAccessToken();

    void clear();

    int countByOpenId(String openId);

    void updateWxUser(@Param("nickName") String nickName, @Param("headimgUrl") String headimgUrl,
                      @Param("remark") String remark,@Param("subscribe") int subscribe,
                      @Param("subscribeTime")String subscribeTime,@Param("openId") String openId);

    void addWxUserFromTencent(WxUserInfo wxUserInfo);

    List<WxUserInfo> getUserList();

    void saveUpdateRecord(Long timeStamp);

    Long getUpdateRecord();
}
