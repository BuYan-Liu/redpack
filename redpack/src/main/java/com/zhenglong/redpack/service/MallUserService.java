package com.zhenglong.redpack.service;

import com.zhenglong.redpack.entity.MallUser;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
public interface MallUserService {
    MallUser get(Long id);

    List<MallUser> getList(MallUser condition, int  currentPage, int size);

    List<MallUser> getListWithStatus(Integer[] statusArray, int currentPage, int size, String mallId, String nickName);

    int getTotalCount(String mallId, String nickName);

    int getTotalCountWithCondition(MallUser condition);

    int getCountWithStatus(Integer[] statusArray, String mallId, String nickName);

    int getTotalCountWithStatus(Integer[] statusArray, String mallId, String nickName);

    List<MallUser> getAll(int currentPage, int size, String mallId, String nickName);

    boolean add(MallUser mallUser);

    boolean addList(List<MallUser> mallUsers);

}
