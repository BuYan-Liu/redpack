package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
@Getter
@Setter
@ToString
public class WxUserInfo {
    private String country;
    private String unionid;
    private int subscribe;//是否订阅
    private String city;
    private String openid;
    private int sex;
    private String remark;
    private String subscribe_time;
    private String province;
    private String nickname;
    private String headimgurl;
}
