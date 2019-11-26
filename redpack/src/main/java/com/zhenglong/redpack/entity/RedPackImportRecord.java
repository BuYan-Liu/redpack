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
public class RedPackImportRecord {
    private Integer id;
    private String date;
    private String remark;
    private int sended;
}
