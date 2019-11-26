package com.zhenglong.redpack.controller;

import com.zhenglong.redpack.entity.Admin;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.service.AdminService;
import com.zhenglong.util.ResponseUtil;
import com.zhenglong.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lion
 * @描述
 * @date 2019/5/23
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping("/login")
    public Result login(String phone,String password) {
        String token=adminService.login(phone,password);
        if (!StringUtils.isEmpty(token)) return ResponseUtil.success(token);
        return ResponseUtil.error("登录失败,请检查账号密码是否正确");
    }

    @RequestMapping("/admin")
    public Result getAdminInfo(HttpServletRequest request) {
        String token=request.getHeader("token");
        Admin admin=adminService.getAdmin(token);
        if (admin!=null) {
            return ResponseUtil.success(admin);
        }
        return ResponseUtil.error("请求用户信息失败");
    }

    @RequestMapping("/update")
    public Result updateAdmin(Integer id, String name, String phone) {
        if (adminService.updateAdmin(name,phone,id)) return ResponseUtil.success();
        return ResponseUtil.error("修改失败");
    }


    @RequestMapping("/check")
    public Result checkPassword(Integer id,String password) {
        if (adminService.checkPassword(id,password)) return ResponseUtil.success();
        return ResponseUtil.error("验证使用中的密码失败");
    }

    @RequestMapping("/update/password")
    public Result updatePassword(Integer id, String newPassword) {
        if (adminService.updatePassword(id,newPassword)) return ResponseUtil.success();
        return ResponseUtil.error("修改失败");
    }
}
