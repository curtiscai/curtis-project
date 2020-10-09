package com.curtis.redis.springboot.simple;

import com.curtis.redis.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author curtis
 * @desc 使用SpringBoot集成Redis最简示例
 * @date 2020-08-18
 * @email 397773935@qq.com
 * @reference
 */
@Profile("redis-base")
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisSimpleTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSimpleTest.class);

    @Test
    public void test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("curtis1");
        userEntity.setAge(21);
        userEntity.setHeight(BigDecimal.valueOf(1.81));
        userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
        userEntity.setAddress("中国河北石家庄市");

        String key = "user:id:" + userEntity.getId();
        redisTemplate.opsForValue().set(key, userEntity);

        UserEntity userEntityGet = (UserEntity) redisTemplate.opsForValue().get(key);
        LOGGER.info("userEntityGet:{}", userEntityGet);
    }

}
