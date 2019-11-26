package com.zhenglong.redpack;

import com.alibaba.fastjson.JSON;
import com.zhenglong.redpack.entity.Admin;
import com.zhenglong.redpack.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/7/26
 */
@Component
public class StartupInitRunner implements CommandLineRunner {

    @Resource
    AdminService adminService;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<Admin> admins=adminService.admins();
        for (Admin admin : admins) {
            ValueOperations<Serializable,String> operations=redisTemplate.opsForValue();
            operations.set(admin.getId(), JSON.toJSONString(admin));
        }
    }
}
