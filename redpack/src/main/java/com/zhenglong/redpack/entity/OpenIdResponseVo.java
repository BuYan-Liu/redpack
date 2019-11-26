package com.zhenglong.redpack.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lion
 * @描述
 * @date 2019/5/13
 */
@Getter
@Setter
public class OpenIdResponseVo {
    private int total;
    private int count;
    private String next_openid;
    private Data data;

    @Getter
    @Setter
    private class Data {
        private String[] openid;
    }
}
