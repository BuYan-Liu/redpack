package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Lion
 * @描述 商城用户信息
 * @date 2019/5/11
 */
@Getter
@Setter
public class MallUser implements Serializable {

    private Long id;
    private Integer status; //1可用2删除
    private String mallId;
    private String nickName;
    private String createTime;
    private String superMallId;
    private int commission;//是否分佣
    private String amount;
}
