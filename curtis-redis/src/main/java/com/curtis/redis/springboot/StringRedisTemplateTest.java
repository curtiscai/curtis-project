package com.curtis.redis.springboot;

import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author curtis
 * @desc
 * @date 2020-08-13
 * @email 397773935@qq.com
 * @reference
 */
@Profile("redis-base")
@SpringBootTest
@RunWith(SpringRunner.class)
public class StringRedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(StringRedisTemplateTest.class);

    @Test
    public void testOpsForValue() {
        stringRedisTemplate.opsForValue().set("strKey1", "strValue1");
        String strValue11 = stringRedisTemplate.opsForValue().get("strKey11");
        String strValue12 = stringRedisTemplate.opsForValue().get("strKey12");
        LOGGER.info("strValue11 = {}, strValue12 = {}", strValue11, strValue12);

        Map<String, String> keyValueMap1 = Maps.newHashMap();
        keyValueMap1.put("strKey21", "strValue21");
        keyValueMap1.put("strKey22", "strValue22");
        keyValueMap1.put("strKey23", "strValue23");
        stringRedisTemplate.opsForValue().multiSet(keyValueMap1);
        String strValue21 = stringRedisTemplate.opsForValue().get("strKey21");
        LOGGER.info("strValue21 = {}", strValue21);

        List<String> strKey3List = Lists.newArrayList("strKey21", "strKey22", "strKey23");
        List<String> strValue3List = stringRedisTemplate.opsForValue().multiGet(strKey3List);
        LOGGER.info("strValue3List = {}", strValue3List);

        Long strValue41 = stringRedisTemplate.opsForValue().increment("strKey4");
        Long strValue42 = stringRedisTemplate.opsForValue().increment("strKey4");
        LOGGER.info("strValue41 = {}, strValue42 = {}", strValue41, strValue42);
    }

    @Test
    public void testOpsForValue2() {
        stringRedisTemplate.opsForHash().put("hash1", "hashKey11", "hashValue11");
        stringRedisTemplate.opsForHash().put("hash1", "hashKey12", "hashValue12");
        Object hashKey11 = stringRedisTemplate.opsForHash().get("hash1", "hashKey11");
        LOGGER.info("hashKey11 = {}", hashKey11);


        Map<String, String> keyValueMap2 = Maps.newHashMap();
        keyValueMap2.put("hashKey21", "strValue21");
        keyValueMap2.put("hashKey22", "strValue22");
        keyValueMap2.put("hashKey23", "strValue23");
        stringRedisTemplate.opsForHash().putAll("hash2", keyValueMap2);
        List<Object> hashKey2 = stringRedisTemplate.opsForHash().multiGet("hash2", Lists.newArrayList("hashKey21", "hashKey22"));
        LOGGER.info("hashKey2 = {}", hashKey2);
        
    }
}
