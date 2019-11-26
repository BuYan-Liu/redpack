package com.zhenglong.redpack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhenglong.redpack.operator")
public class RedpackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedpackApplication.class, args);
    }

}
