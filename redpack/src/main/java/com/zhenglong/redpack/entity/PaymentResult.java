package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/20
 */
@Getter
@Setter
public class PaymentResult {
    private Integer id;
    private String returnCode;
    private String returnMsg;
    private String resultCode;
    private String partnerTradeNo;//商户订单号
    private String paymentNo; //微信付款单号
    private String paymentTime;//付款成功时间
    private String errCode;//错误码
    private String errCodeDesc;//错误码描述
}
