package com.zhenglong.redpack.service;

import com.zhenglong.redpack.entity.User;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/17
 */
public interface UserService {
    boolean match(Long userId, String mallUserId);

    List<User> userList(int currentPage, int size);

    int getTotalCount(Integer status, Integer subscribe, Integer sex, String mallId, String nickName);

    List<User> userList(String nickName);

    boolean updateWxNumber(Integer id, String wxNumber);

    int getTotalCount();

    List<User> userList(Integer currentPage, Integer size, Integer status, Integer subscribe, Integer sex, String mallId, String nickName);

    boolean updateRemark(Integer id, String remark);
}
