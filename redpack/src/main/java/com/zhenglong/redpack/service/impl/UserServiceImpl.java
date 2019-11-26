package com.zhenglong.redpack.service.impl;

import com.zhenglong.global.GlobalCode;
import com.zhenglong.redpack.entity.User;
import com.zhenglong.redpack.operator.UserOperator;
import com.zhenglong.redpack.service.UserService;
import com.zhenglong.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/17
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserOperator userOperator;

    @Override
    public List<User> userList(int currentPage, int size) {
        int start=(currentPage-1)*size;
        return userOperator.userList(start,size);
    }

    @Override
    public int getTotalCount() {
        return userOperator.getTotalCount();
    }

    @Override
    public List<User> userList(Integer currentPage, Integer size, Integer status, Integer subscribe, Integer sex,
                               String mallId,String nickName) {
        int start=(currentPage-1)*size;
        return userOperator.userListFilter(start,size,status,subscribe,sex,mallId,nickName);
    }

    @Override
    public int getTotalCount(Integer status, Integer subscribe, Integer sex,String mallId, String nickName) {
        return userOperator.getTotalCountFilter(status,subscribe,sex,mallId,nickName);
    }

    @Override
    public List<User> userList(String nickName) {
        if (nickName!=null) {
            return userOperator.userListNickName(nickName);
        }
        return null;
    }

    @Transactional
    @Override
    public boolean match(Long userId, String mallUserId) {
        if (userId!=null && mallUserId!=null) {
            //判断是否已经绑定
            Long existId=userOperator.getWithMallId(mallUserId);
            if (existId!=null && existId>0L) {
                //如果已存在与该商城id绑定的user则将该user的mallId清空
                userOperator.updateMallId(existId,"", GlobalCode.AStatus.UNMATCHED);
            }
            //修改商城Id
            userOperator.updateMallId(userId,mallUserId, GlobalCode.AStatus.MATCHED);
            //修改状态
            userOperator.updateMallStatus(GlobalCode.AStatus.MATCHED,mallUserId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateWxNumber(Integer id, String wxNumber) {
        if (id>0 && !wxNumber.isEmpty()) {
            userOperator.updateWxNumber(id,wxNumber);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRemark(Integer id, String remark) {
        if (id>0) {
            if (StringUtils.isEmpty(remark)) remark="";
            userOperator.updateRemark(id,remark);
            return true;
        }
        return false;
    }

}
