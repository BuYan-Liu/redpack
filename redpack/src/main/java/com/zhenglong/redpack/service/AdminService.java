package com.zhenglong.redpack.service;

import com.zhenglong.redpack.entity.Admin;

import java.util.List;

public interface AdminService {

    List<Admin> admins();

    boolean saveAdmin(Admin admin);

    boolean updateAdmin(String name, String phone, Integer id);

    boolean updatePassword(Integer id, String newPw);

    boolean getToken(String token);

    String login(String phone,String password);

    Admin getAdmin(String phone, String password);

    Admin getAdmin(String token);

    boolean checkPassword(Integer id, String password);
}
