package com.zhenglong.redpack.controller;

import com.zhenglong.redpack.entity.Result;
import com.zhenglong.redpack.entity.User;
import com.zhenglong.redpack.service.UserService;
import com.zhenglong.util.ExportTools;
import com.zhenglong.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/17
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @RequestMapping("/users/{nickName}")
    public Result users(@PathVariable String nickName) {
        List<User> users=userService.userList(nickName);
        return ResponseUtil.success(users);
    }

    @CrossOrigin
    @RequestMapping("/users/{currentPage}/{size}")
    public Result userList(@PathVariable Integer currentPage,@PathVariable Integer size) {
        List<User> users=userService.userList(currentPage,size);
        int totalCount=userService.getTotalCount(-1, -1, -1, "001", "ascii201907032016");
        Result result=ResponseUtil.success(users);
        result.setTotalCount(totalCount);
        return result;
    }

    @CrossOrigin
    @RequestMapping("/users/{currentPage}/{size}/{status}/{subscribe}/{sex}/{mallId}/{nickName}")
    public Result userList(@PathVariable Integer currentPage,@PathVariable Integer size,
                           @PathVariable Integer status,@PathVariable Integer subscribe,
                           @PathVariable Integer sex,@PathVariable String mallId,@PathVariable String nickName) {
        List<User> users=userService.userList(currentPage,size,status,subscribe,sex,mallId,nickName);
        int totalCount=userService.getTotalCount(status,subscribe,sex,mallId,nickName);
        Result result=ResponseUtil.success(users);
        result.setTotalCount(totalCount);
        return result;
    }

    @CrossOrigin
    @RequestMapping("/update/{userId}/{mallUserId}")
    public Result update(@PathVariable String userId,@PathVariable Integer mallUserId) {
        if (userService.match(Long.parseLong(userId),mallUserId+"")) return ResponseUtil.success();
        return ResponseUtil.error("信息错误，匹配失败！");
    }

    @CrossOrigin
    @RequestMapping("/wxNumber/{id}/{wxNumber}")
    public Result updateWxNumber(@PathVariable Integer id,@PathVariable String wxNumber) {
        if (userService.updateWxNumber(id,wxNumber)) return ResponseUtil.success();
        return ResponseUtil.error("信息错误，绑定失败！");
    }

    @RequestMapping("/users/export")
    public void export(HttpServletResponse response) {
        List<User> users;
        users=userService.userList(1, 100000, -1, -1, -1, "001", "ascii201907032016");
        String[] titles={"头像","昵称","状态","性别","微信号","openId","商城Id","备注","是否订阅","订阅时间"};
        String[] propertys={"headimgUrl","nickName","status","sex","wxNumber","openId","mallId","remark",
                "subscribe","subscribeTime"};
        ExportTools.downloadCsvFile("公众号用户信息",response,titles,propertys,users);
    }

    @CrossOrigin
    @RequestMapping("/remark/{id}/{remark}")
    public Result addRemark(@PathVariable Integer id, @PathVariable String remark) {
        if (userService.updateRemark(id,remark)) return ResponseUtil.success();
        return ResponseUtil.error("备注失败");
    }
}
