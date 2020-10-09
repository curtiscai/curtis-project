package com.curtis.redis.springboot.customer;

import com.curtis.redis.entity.UserEntity;
import com.curtis.redis.utils.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author curtis
 * @desc
 * @date 2020-08-18
 * @email 397773935@qq.com
 * @reference https://www.jianshu.com/p/071bae3834b0
 */
@Profile("redis-customer")
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisCustomerTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCustomerTest.class);

    /**
     * 测试：GenericJackson2JsonRedisSerializer和Jackson2JsonRedisSerializer序列化和反序列化效率对比
     * 100w条数据进行序列化和反序列化效率对比
     * 使用GenericJackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：5494
     * 使用Jackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：3388
     * <p>
     * 使用GenericJackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：5505
     * 使用Jackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：3470
     * <p>
     * 使用GenericJackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：5248
     * 使用Jackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：3206
     */
    @Test
    public void testSerializerEfficiency() {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        LocalTime start1 = LocalTime.now();
        for (int i = 0; i < 100_0000; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setName("curtis" + i);
            userEntity.setAge(20 + i % 10);
            userEntity.setHeight(BigDecimal.valueOf(1.81));
            userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
            userEntity.setAddress("中国河北石家庄市");
            byte[] serialize = genericJackson2JsonRedisSerializer.serialize(userEntity);
            UserEntity deserialize = genericJackson2JsonRedisSerializer.deserialize(serialize, UserEntity.class);
        }
        LocalTime end1 = LocalTime.now();
        Duration between1 = Duration.between(start1, end1);
        LOGGER.info("使用GenericJackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：{}", between1.toMillis());

        Jackson2JsonRedisSerializer<UserEntity> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(UserEntity.class);
        LocalTime start2 = LocalTime.now();
        for (int i = 0; i < 100_0000; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setName("curtis" + i);
            userEntity.setAge(20 + i % 10);
            userEntity.setHeight(BigDecimal.valueOf(1.81));
            userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
            userEntity.setAddress("中国河北石家庄市");
            byte[] serialize = jackson2JsonRedisSerializer.serialize(userEntity);
            UserEntity deserialize = jackson2JsonRedisSerializer.deserialize(serialize);
        }
        LocalTime end2 = LocalTime.now();
        Duration between2 = Duration.between(start2, end2);
        LOGGER.info("使用Jackson2JsonRedisSerializer进行序列化反序列化100w个对象耗时：{}", between2.toMillis());
    }

    @Test
    public void testRedisWithGenericJackson2JsonRedisSerializer() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("curtis1");
        userEntity.setAge(21);
        userEntity.setHeight(BigDecimal.valueOf(1.81));
        userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
        userEntity.setAddress("中国河北石家庄市");

        String key = "user:id:" + userEntity.getId();
        redisTemplate.opsForValue().set(key, userEntity);

        UserEntity userEntity11 = (UserEntity) redisTemplate.opsForValue().get(key);
        LOGGER.info("userEntity11:{}", userEntity11);

        redisTemplate.opsForValue().set("strKey1", 100);
        Integer strKey1 = (Integer) redisTemplate.opsForValue().get("strKey1");
        LOGGER.info("strKey1:{}", strKey1);

        redisTemplate.opsForHash().put("hash", "hashKey1", userEntity);
        UserEntity UserEntity21 = (UserEntity) redisTemplate.opsForHash().get("hash", "hashKey1");
        LOGGER.info("UserEntity21:{}", UserEntity21);

        Long listKey11 = redisTemplate.opsForList().leftPush("listKey1", userEntity);
        UserEntity listKey12 = (UserEntity) redisTemplate.opsForList().leftPop("listKey1");
        LOGGER.info("listKey11={},listKey12:{}", listKey11, listKey12);


        UserEntity userEntity1 = new UserEntity(1, "curtis1", 21, BigDecimal.valueOf(180.1), null, "male", "河北省石家庄市");
        UserEntity userEntity2 = new UserEntity(2, "curtis2", 22, BigDecimal.valueOf(180.2), null, "male", "河北省保定市");
        Long setKey1 = redisTemplate.opsForSet().add("setKey1", userEntity1, userEntity2);
        List<Object> setKey11 = redisTemplate.opsForSet().pop("setKey1", 2);
        List<UserEntity> userEntityList = setKey11 != null ? setKey11.parallelStream().map(item -> (UserEntity) item).collect(Collectors.toList()) : null;
        LOGGER.info("setKey1={},userEntityList:{}", setKey1, userEntityList);
    }



    @Test
    public void testRedisWithJackson2JsonRedisSerializer() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("curtis1");
        userEntity.setAge(21);
        userEntity.setHeight(BigDecimal.valueOf(1.81));
        userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
        userEntity.setAddress("中国河北石家庄市");

        String key = "user:id:" + userEntity.getId();
        redisTemplate.opsForValue().set(key, userEntity);

        Object userEntity11 = redisTemplate.opsForValue().get(key);
        LOGGER.info("userEntity11:{}", userEntity11);

        redisTemplate.opsForValue().set("strKey1", 100);
        Integer strKey1 = (Integer) redisTemplate.opsForValue().get("strKey1");
        LOGGER.info("strKey1:{}", strKey1);

        redisTemplate.opsForHash().put("hash", "hashKey1", userEntity);
        UserEntity UserEntity21 = (UserEntity) redisTemplate.opsForHash().get("hash", "hashKey1");
        LOGGER.info("UserEntity21:{}", UserEntity21);

        Long listKey11 = redisTemplate.opsForList().leftPush("listKey1", userEntity);
        UserEntity listKey12 = (UserEntity) redisTemplate.opsForList().leftPop("listKey1");
        LOGGER.info("listKey11={},listKey12:{}", listKey11, listKey12);


        UserEntity userEntity1 = new UserEntity(1, "curtis1", 21, BigDecimal.valueOf(180.1), null, "male", "河北省石家庄市");
        UserEntity userEntity2 = new UserEntity(2, "curtis2", 22, BigDecimal.valueOf(180.2), null, "male", "河北省保定市");
        Long setKey1 = redisTemplate.opsForSet().add("setKey1", userEntity1, userEntity2);
        List<Object> setKey11 = redisTemplate.opsForSet().pop("setKey1", 2);
        List<UserEntity> userEntityList = setKey11 != null ? setKey11.parallelStream().map(item -> (UserEntity) item).collect(Collectors.toList()) : null;
        LOGGER.info("setKey1={},userEntityList:{}", setKey1, userEntityList);
    }

    @Test
    public void testRedis2() {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("curtis1");
        userEntity.setAge(21);
        userEntity.setHeight(BigDecimal.valueOf(1.81));
        userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
        userEntity.setAddress("中国河北石家庄市");

        String key = "user:id:" + userEntity.getId();
        stringRedisTemplate.opsForValue().set(key, JacksonUtil.obj2json(userEntity));

        Object userEntity11 = stringRedisTemplate.opsForValue().get(key);
        LOGGER.info("userEntity11:{}", userEntity11);

        stringRedisTemplate.opsForValue().set("strKey1", "100");
        String strKeyStr1 = stringRedisTemplate.opsForValue().get("strKey1");
        Integer strKey1 = Integer.valueOf(strKeyStr1);
        LOGGER.info("strKey1:{}", strKey1);

        stringRedisTemplate.opsForHash().put("hash", "hashKey1", JacksonUtil.obj2json(userEntity));
        Object UserEntityObj21 = stringRedisTemplate.opsForHash().get("hash", "hashKey1");
        UserEntity userEntity21 = JacksonUtil.json2pojo(String.valueOf(UserEntityObj21),UserEntity.class);
        LOGGER.info("userEntity21:{}", userEntity21);

        Long listKey11 = stringRedisTemplate.opsForList().leftPush("listKey1", JacksonUtil.obj2json(userEntity));
        String listKey11Str = stringRedisTemplate.opsForList().leftPop("listKey1");
        UserEntity listKey12 = JacksonUtil.json2pojo(listKey11Str, UserEntity.class);
        LOGGER.info("listKey11={},listKey12:{}", listKey11, listKey12);


        UserEntity userEntity1 = new UserEntity(1, "curtis1", 21, BigDecimal.valueOf(180.1), null, "male", "河北省石家庄市");
        UserEntity userEntity2 = new UserEntity(2, "curtis2", 22, BigDecimal.valueOf(180.2), null, "male", "河北省保定市");
        Long setKey1 = stringRedisTemplate.opsForSet().add("setKey1", JacksonUtil.obj2json(userEntity1),  JacksonUtil.obj2json(userEntity2));
        List<String> setKey11 = stringRedisTemplate.opsForSet().pop("setKey1", 2);
        List<UserEntity> userEntityList = setKey11 != null ? setKey11.parallelStream().map(item -> JacksonUtil.json2pojo(item,UserEntity.class)).collect(Collectors.toList()) : null;
        LOGGER.info("setKey1={},userEntityList:{}", setKey1, userEntityList);
    }


    @Test
    public void testRedisEfficiency() {
        LocalTime start1 = LocalTime.now();
        for (int i = 0; i < 1_0000; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setName("curtis" + i);
            userEntity.setAge(20 + i % 10);
            userEntity.setHeight(BigDecimal.valueOf(1.81));
            userEntity.setBirth(Date.from(LocalDate.parse("1991-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)));
            userEntity.setAddress("中国河北石家庄市");
            String key = "user:id:" + userEntity.getId();
            redisTemplate.opsForValue().set(key, userEntity);
//            UserEntity userEntityResult = (UserEntity) redisTemplate.opsForValue().get(key);
        }
        LocalTime end1 = LocalTime.now();
        Duration between1 = Duration.between(start1, end1);
        LOGGER.info("使用GenericJackson2JsonRedisSerializer写入10w个对象耗时：{}", between1.toMillis());
    }
}
