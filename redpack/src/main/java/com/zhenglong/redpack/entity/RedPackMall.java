package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/19
 */
@Getter
@Setter
public class RedPackMall {
    private Long id;
    private String mallId;
    private String amount;
    private int recordId;
    private Integer paymentResultId;
}
