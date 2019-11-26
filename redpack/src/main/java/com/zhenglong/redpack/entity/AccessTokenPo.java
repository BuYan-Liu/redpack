package com.zhenglong.redpack.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Lion
 * @描述
 * @date 2019/5/14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenPo implements Serializable {
    private static final long serialVersionUID = 5592021785232009477L;

    private String accessToken;
    private String expiresIn;
    private Long time;
}
