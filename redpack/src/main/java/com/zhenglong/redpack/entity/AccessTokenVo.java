package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
@Setter
@Getter
public class AccessTokenVo {
    private String access_token;
    private String expires_in;
}
