package com.zhenglong.redpack.service.impl;

import com.zhenglong.redpack.entity.Admin;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.entity.Token;
import com.zhenglong.redpack.operator.AdminOperator;
import com.zhenglong.redpack.service.AdminService;
import com.zhenglong.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/23
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    AdminOperator adminOperator;

    @Override
    public List<Admin> admins() {
        return adminOperator.admins();
    }

    @Transactional
    @Override
    public boolean saveAdmin(Admin admin) {
        adminOperator.save(admin);
        //为新生成的admin对象初始化一个token对象
        Token token=new Token();
        token.setAdminId(admin.getId());
        token.setToken("");
        token.setTime(0L);
        adminOperator.saveToken(token);
        return true;
    }

    @Transactional
    @Override
    public boolean updateAdmin(String name,String phone,Integer id) {
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(phone) && id!=null) {
            adminOperator.update(name,phone,id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updatePassword(Integer id, String newPw) {
        if (id!=null && !StringUtils.isEmpty(newPw)) {
            adminOperator.updatePassword(id,newPw);
            adminOperator.updateTokenTime(0,id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean getToken(String token) {
        Token tokenVo=adminOperator.getToken(token);
        if (tokenVo.getToken()!=null && !tokenVo.getToken().isEmpty()) {
            long time = new Date().getTime()/1000;
            if ((time-tokenVo.getTime())<3600) {
                //更新token时间
                adminOperator.updateTokenTime(new Date().getTime()/1000,tokenVo.getId());
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public String login(String phone,String password) {
        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(password)) {
            //查找admin
            Admin admin = adminOperator.getToLogin(phone,password);
            if (admin!=null) {
                //生成新的token值
                String token=StringUtils.getTokenStr(phone+password);
                adminOperator.updateToken(token,new Date().getTime()/1000,admin.getId());
                return token;
            }
        }
        return null;
    }

    @Override
    public Admin getAdmin(String phone, String password) {
        return adminOperator.getToLogin(phone,password);
    }

    @Transactional
    @Override
    public Admin getAdmin(String token) {
        if (!StringUtils.isEmpty(token)) {
            Token token1=adminOperator.getToken(token);
            Admin admin=adminOperator.get(token1.getAdminId());
            return admin;
        }
        return null;
    }

    @Override
    public boolean checkPassword(Integer id, String password) {
        if (id!=null && !StringUtils.isEmpty(password)) {
            Admin admin=adminOperator.get(id);
            if (admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
