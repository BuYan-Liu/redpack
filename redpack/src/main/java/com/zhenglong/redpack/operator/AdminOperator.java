package com.zhenglong.redpack.operator;

import com.zhenglong.redpack.entity.Admin;
import com.zhenglong.redpack.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/23
 */
@Mapper
public interface AdminOperator {
    Admin get(Integer id);

    Admin getToLogin(@Param("phone") String phone, @Param("password") String password);

    void save(Admin admin);

    void saveToken(Token token);

    Token getToken(String token);


    void updateTokenTime(@Param("time") long time,@Param("id") Integer id);

    void updateToken(@Param("token") String token, @Param("time") long time,@Param("adminId") Integer adminId);

    void update(@Param("name") String name, @Param("phone") String phone, @Param("id") Integer id);

    void updatePassword(@Param("id") Integer id, @Param("newPw") String newPw);

    List<Admin> admins();
}
