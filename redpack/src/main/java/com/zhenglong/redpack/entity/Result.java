package com.zhenglong.redpack.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
@AllArgsConstructor
@Getter
@Setter
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;
    private int totalCount;
}
