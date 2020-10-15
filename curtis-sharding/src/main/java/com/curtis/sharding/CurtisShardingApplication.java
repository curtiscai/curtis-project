package com.curtis.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.curtis.sharding.**.mapper")
@SpringBootApplication
public class CurtisShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurtisShardingApplication.class, args);
    }
}
