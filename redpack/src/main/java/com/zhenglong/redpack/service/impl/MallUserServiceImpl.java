package com.zhenglong.redpack.service.impl;

import com.zhenglong.redpack.entity.MallUser;
import com.zhenglong.redpack.operator.MallUserOperator;
import com.zhenglong.redpack.service.MallUserService;
import com.zhenglong.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
@Service
public class MallUserServiceImpl implements MallUserService {
    @Resource
    MallUserOperator mallUserOperator;
    @Override
    public MallUser get(Long id) {
        return mallUserOperator.get(id);
    }

    @Override
    public List<MallUser> getList(MallUser condition, int currentPage, int size) {
        int start=(currentPage-1)*size;
        return mallUserOperator.getList(condition,start,size);
    }

    @Override
    public List<MallUser> getAll(int currentPage, int size, String mallId, String nickName) {
        if (StringUtils.isEmpty(mallId)) mallId="0";
        if (StringUtils.isEmpty(nickName)) nickName="ascii201907032016";
        int start=(currentPage-1)*size;
        return mallUserOperator.getAll(start,size, mallId, nickName);
    }

    @Transactional
    @Override
    public boolean add(MallUser mallUser) {
        return mallUserOperator.add(mallUser)>0?true:false;
    }

    @Transactional
    @Override
    public boolean addList(List<MallUser> mallUsers) {
        for (MallUser mallUser : mallUsers) {
            //判断是否存在
            if (mallUserOperator.getWithMallId(mallUser.getMallId())!=null) {
                mallUserOperator.update(mallUser);
            }else {
                mallUserOperator.add(mallUser);
            }
        }
        return true;
    }

    @Override
    public int getTotalCount(String mallId, String nickName) {
        if (StringUtils.isEmpty(mallId)) mallId="0";
        if (StringUtils.isEmpty(nickName)) nickName="ascii201907032016";
        return mallUserOperator.getTotalCount(mallId, nickName);
    }

    @Override
    public int getTotalCountWithCondition(MallUser condition) {
        return mallUserOperator.getTotalCountWithCondition(condition);
    }

    @Override
    public List<MallUser> getListWithStatus(Integer[] statusArray, int currentPage, int size, String mallId, String nickName) {
        if (StringUtils.isEmpty(mallId)) mallId="0";
        if (StringUtils.isEmpty(nickName)) nickName="ascii201907032016";
        int start=(currentPage-1)*size;
        if (statusArray[0]==-1) {
            return mallUserOperator.getAll(start,size,mallId,nickName);
        }
        return mallUserOperator.getListWithStatus(statusArray,start,size,mallId,nickName);
    }

    @Override
    public int getCountWithStatus(Integer[] statusArray, String mallId, String nickName) {
        if (StringUtils.isEmpty(mallId)) mallId="0";
        if (StringUtils.isEmpty(nickName)) nickName="ascii201907032016";
        return mallUserOperator.getTotalCountWithStatus(statusArray, mallId, nickName);
    }

    @Override
    public int getTotalCountWithStatus(Integer[] statusArray, String mallId, String nickName) {
        if (StringUtils.isEmpty(mallId)) mallId="0";
        if (StringUtils.isEmpty(nickName)) nickName="ascii201907032016";
        if (statusArray[0]==-1) {
            return mallUserOperator.getTotalCount(mallId,nickName);
        }
        return mallUserOperator.getTotalCountWithStatus(statusArray,mallId,nickName);
    }

}
