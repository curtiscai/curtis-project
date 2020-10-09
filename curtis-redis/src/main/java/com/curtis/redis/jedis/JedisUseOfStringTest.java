package com.curtis.redis.jedis;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.List;

/**
 * @author curtis
 * @desc 使用Jedis执行String指令
 * @date 2020-08-07
 * @email 397773935@qq.com
 * @reference
 */
public class JedisUseOfStringTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUseOfStringTest.class);

    @Before
    public void before() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");
        jedis.flushDB();
    }


    @Test
    public void testJedisUseOfString() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");


        /**
         * 指令格式：SET key value [EX seconds] [PX milliseconds] [NX|XX]
         * 时间复杂度：O(1)
         * 指令描述：将字符串值value关联到 key。如果 key已经持有其他值，SET 就覆写旧值，无视类型。
         * 返回值：Redis 2.6.12版本开始，SET命令只在设置操作成功完成时才返回OK。
         */
        // 等同于：set k1 1
        String result11 = jedis.set("strKey1", "1"); // 对不存在的键进行设置
        String result12 = jedis.set("strKey1", "v1"); // 对已存在的键进行设置，直接覆盖之前的值，无视类型
        // 使用SetParams指定特殊参数：[EX seconds] [PX milliseconds] [NX|XX]
        SetParams setParams = new SetParams().ex(10); // 设置超时时间10秒
        // 等同于：set strKey2 v2 EX 10
        String result13 = jedis.set("strKey2", "v2", setParams); // 10秒后k2过期
        // result1=OK, result2=OK, result3=OK
        LOGGER.info("result11={}, result12={}, result13={}", result11, result12, result13);


        /**
         * 指令格式：SETNX key value
         * 时间复杂度：O(1)
         * 指令描述：只在键key不存在的情况下，将键key的值设置为value。若键key已经存在，则SETNX命令不做任何动作。
         * 返回值：命令在设置成功时返回1 ，设置失败时返回0 。
         */
        Long result21 = jedis.setnx("strKey21", "21");
        Long result22 = jedis.setnx("strKey21", "22");
        String strKey21 = jedis.get("strKey21");
        // result21=1, result22=0, strKey21=21
        LOGGER.info("result21={}, result22={}, strKey21={}", result21, result22, strKey21);


        /**
         * 指令格式：GET key
         * 时间复杂度：O(1)
         * 指令描述：返回与键 key 相关联的字符串值。
         * 返回值：如果键key不存在，那么返回特殊值nil ；否则，返回键 key 的值。如果键key的值并非字符串类型，那么返回一个错误， 因为GET命令只能用于字符串值。
         */
        jedis.set("strKey31", "v3");
        // 等同于：get strKey31
        String strKey31 = jedis.get("strKey31");
        // 如果指定key不存在则get返回null值而不是nil字符串(很友好)，If the key does not exist null is returned.
        String strKey32 = jedis.get("strKey32");
        // strKey31=v3, strKey32=null
        LOGGER.info("strKey31={}, strKey32={}", strKey31, strKey32);


        /**
         * 指令格式：GETSET key value
         * 时间复杂度：O(1)
         * 指令描述：将键key的值设为value ，并返回键key在被设置之前的旧值。
         * 返回值：返回给定键key的旧值。如果键key没有旧值，那么命令返回nil。当键key存在但不是字符串类型时，命令返回一个错误。
         */
        // 等同于：getset strKey4 v41
        String OldValOfStrKey4 = jedis.getSet("strKey4", "v41");
        String strKey4 = jedis.get("strKey4");
        // OldValOfStrKey4=null, strKey4=v41
        LOGGER.info("OldValOfStrKey4={}, strKey4={}", OldValOfStrKey4, strKey4);
        OldValOfStrKey4 = jedis.getSet("strKey4", "v42");
        strKey4 = jedis.get("strKey4");
        // OldValOfStrKey4=v41, strKey4=v42
        LOGGER.info("OldValOfStrKey4={}, strKey4={}", OldValOfStrKey4, strKey4);


        /**
         * 指令格式：MSET key value [key value …]
         * 时间复杂度：O(N)，其中N为被设置的键数量。
         * 指令描述：同时为多个键设置值。如果某个给定键已经存在，那么MSET将使用新值去覆盖旧值，MSETNX在所有给定键都不存在的情况下进行设置。
         *     MSET是一个原子性(atomic)操作，所有给定键都会在同一时间内被设置，不会出现某些键被设置了但是另一些键没有被设置的情况。
         * 返回值：MSET命令总是返回OK 。
         */
        // 等同于：mset year1 2019 month1 12 day1 23
        String mset1 = jedis.mset("year1", "2019", "month1", "12", "day1", "23");
        List<String> mget1 = jedis.mget("year1", "month1", "day1");
        // mset1=OK, mget1=[2019, 12, 23]
        LOGGER.info("mset1={}, mget1={}", mset1, mget1);


        /**
         * 指令格式：MGET key [key …]
         * 时间复杂度：O(N) ，其中N为给定键的数量。
         * 指令描述：返回给定的一个或多个字符串键的值。如果给定的字符串键里面，有某个键不存在，那么这个键的值将以特殊值nil表示。
         * 返回值：MGET命令将返回一个列表，列表中包含了所有给定键的值。
         */
        List<String> mget = jedis.mget("year1", "month1", "month", "day1");
        // mget=[2019, 12, null, 23]
        LOGGER.info("mget={}", mget);

        /**
         * 指令格式：INCR key（DECR类似）
         * 时间复杂度：O(1)
         * 指令描述：为键 key 储存的数字值加上一。如果键 key不存在，那么它的值会先被初始化为0，然后再执行INCR命令。
         *  如果键key储存的值不能被解释为数字，那么INCR命令将返回一个错误。本操作的值限制在 64 位(bit)有符号数字表示之内。
         * 返回值：INCR 命令会返回键 key 在执行加一操作之后的值。
         */
        Long count1 = jedis.incr("count");
        String count2 = jedis.get("count");
        LOGGER.info("count1={}, count2={}", count1, count2);


        /**
         * 指令格式：INCRBY key increment（DECRBY类似）
         * 时间复杂度：O(1)
         * 指令描述：为键key储存的数字值加上增量increment。如果键key不存在，那么键key的值会先被初始化为0，然后再执行INCRBY命令。
         *  如果键key储存的值不能被解释为数字，那么INCRBY命令将返回一个错误。
         * 返回值：在加上增量increment之后，键key当前的值。
         */
        Long incrBy1 = jedis.incrBy("incrBy", 100);
        String incrBy2 = jedis.get("incrBy");
        LOGGER.info("incrBy1={}, incrBy2={}", incrBy1, incrBy2);
    }
}
