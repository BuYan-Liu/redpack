package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/23
 */
@Getter
@Setter
public class Token {
    private Integer id;
    private Integer adminId;
    private String token;
    private Long time;
}
