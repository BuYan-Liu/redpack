package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述 用户信息
 * @date 2019/5/11
 */
@Getter
@Setter
public class User {
    private Long id;//1
    private String headimgUrl;//1
    private Integer status;//1
    private Integer sex;//1
    private String openId;//1
    private String mallId;//1
    private String nickName;//1
    private String remark;//1
    private Integer subscribe;
    private String subscribeTime;
    private String wxNumber;//1
    private String country;
    private String province;
    private String city;
    private String amount;
    private Long redPackMallId;
    private PaymentResult paymentResult;
}
