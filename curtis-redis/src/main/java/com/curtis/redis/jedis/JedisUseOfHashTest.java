package com.curtis.redis.jedis;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author curtis
 * @desc 使用Jedis执行Hash指令
 * @date 2020-08-07
 * @email 397773935@qq.com
 * @reference
 */
public class JedisUseOfHashTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUseOfHashTest.class);

    @Before
    public void before() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");
        jedis.flushDB();
    }


    @Test
    public void testJedisUseOfHash(){
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");


        /**
         * 指令格式：HSET hash field value
         * 时间复杂度：O(1)
         * 指令描述：将哈希表hash中域field的值设置为value。如果给定的哈希表不存在则先创建并执行HSET，如果域field存在则新值覆盖旧值
         * 返回值：当哈希表hash或者域field不存在时，值设置成功返回1，如果域field存在，则值设置成功返回0.
         */
        // Jedis提供HSET指令两种写法：
        // public Long hset(final String key, final String field, final String value)
        // public Long hset(final String key, final Map<String, String> hash)


        // 等同于：hset rank curtis 10
        Long hset11 = jedis.hset("rank", "curtis", "10");
        // 等同于：hget rank curtis
        String hget11 = jedis.hget("rank", "curtis");
        // hset11=1, hget11=10
        LOGGER.info("hset11={}, hget11={}", hset11, hget11);
        Long hset12 = jedis.hset("rank", "curtis", "20");
        String hget12 = jedis.hget("rank", "curtis");
        // hset12=0, hget12=20
        LOGGER.info("hset12={}, hget12={}", hset12, hget12);


        Map<String, String> fieldValueMap = Maps.newHashMap();
        fieldValueMap.put("curtis2", "22");
        Long hset13 = jedis.hset("rank", fieldValueMap);
        String hget13 = jedis.hget("rank", "curtis");
        // hset13=1, hget13=20
        LOGGER.info("hset13={}, hget13={}", hset13, hget13);


        /**
         * 指令格式：HGET hash field
         * 时间复杂度：O(1)
         * 指令描述：返回指定哈希表中给定域的值。
         * 返回值：HGET命令返回指定哈希表给定域的值。如果哈希表或者域不存在，那么命令返回 nil 。
         */
        String hget31 = jedis.hget("rank3", "curtis3");
        Long hset31 = jedis.hset("rank3", "curtis3", "v3");
        String hget32 = jedis.hget("rank3", "curtis3");
        // hget31=null, hset31=1, hget32=v3
        LOGGER.info("hget31={}, hset31={}, hget32={}", hget31, hset31, hget32);


        /**
         * 指令格式：HEXISTS hash field
         * 时间复杂度：O(1)
         * 指令描述：检查给定域 field 是否存在于哈希表 hash 当中。
         * 返回值：HEXISTS 命令在给定域存在时返回1，在给定域不存在时返回0。
         */
        Boolean hexists41 = jedis.hexists("rank4", "curtis4");
        jedis.hset("rank4", "curtis4", "v4");
        // 等同于：hexists rank4 curtis4
        Boolean hexists42 = jedis.hexists("rank4", "curtis4");
        // hexists41=false, hexists42=true
        LOGGER.info("hexists41={}, hexists42={}", hexists41, hexists42);


        /**
         * 指令格式：HDEL key field [field …]
         * 时间复杂度：O(N)， N为要删除的域的数量。
         * 指令描述：删除哈希表 key中的一个或多个指定域，不存在的域将被忽略。
         * 返回值：被成功移除的域的数量，不包括被忽略的域。
         */
        jedis.hset("rank5", "curtis51", "v51");
        jedis.hset("rank5", "curtis52", "v52");
        // 等同于：hdel rank5 curtis51 curtis52 curtis53
        Long hdel5 = jedis.hdel("rank5", "curtis51", "curtis52", "curtis53");
        // hdel5=2
        LOGGER.info("hdel5={}", hdel5);


        /**
         * 指令格式：HLEN key
         * 时间复杂度：O(1)
         * 指令描述：返回哈希表key中域的数量。
         * 返回值：哈希表中域的数量。当key不存在时，返回0 。
         */
        jedis.hset("rank6", "curtis61", "v61");
        jedis.hset("rank6", "curtis62", "v62");
        jedis.hset("rank6", "curtis63", "v63");
        // 等同于：hlen rank6
        Long rank6 = jedis.hlen("rank6");
        // rank6=3
        LOGGER.info("rank6={}", rank6);


        /**
         * 指令格式：HINCRBY key field increment
         * 时间复杂度：O(1)
         * 指令描述：为哈希表key中的域field的值加上增量increment。增量可以为负数相当于减法操作。
         * 如果key不存在则创建新哈希表并将field的值初始为0然后执行增量操作，值被限制在 64 位(bit)有符号数字表示之内。
         * 返回值：执行HINCRBY命令之后，哈希表key中域field的值。
         */
        Long incrBy71 = jedis.hincrBy("rank7", "curtis7", 2);
        // 等同于：hincrby rank7 curtis7 2
        Long incrBy72 = jedis.hincrBy("rank7", "curtis7", 2);
        Long incrBy73 = jedis.hincrBy("rank7", "curtis7", -5);
        // incrBy71=2, incrBy72=4, incrBy73=-1
        LOGGER.info("incrBy71={}, incrBy72={}, incrBy73={}", incrBy71, incrBy72, incrBy73);


        /**
         * 指令格式：HMSET key field value [field value …]
         * 时间复杂度：O(N)， N 为 field-value 对的数量。
         * 指令描述：同时将多个field-value(域-值)对设置到哈希表key中。如果key不存在则创建哈希表key并执行HMSET操作。
         * 返回值：如果命令执行成功，返回OK。当 key不是哈希表(hash)类型时，返回一个错误。
         */
        Map<String, String> fieldValueMap8 = Maps.newHashMap();
        fieldValueMap8.put("curtis81", "v81");
        fieldValueMap8.put("curtis82", "v82");
        // 等同于：hmset rank8 curtis81 v81 curtis82 v82
        String rank8 = jedis.hmset("rank8", fieldValueMap8);
        // rank8=OK
        LOGGER.info("rank8={}", rank8);


        /**
         * 指令格式：HMGET key field [field …]
         * 时间复杂度：O(N)， N 为给定域的数量。
         * 指令描述：返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表则返回一个 nil 值。如果哈希表key不存在则返回一个只带有 nil 值的表。
         * 返回值：一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
         */
        jedis.hset("rank9", "curtis91", "v91");
        jedis.hset("rank9", "curtis92", "v92");
        // 等同于：hmget rank9 curtis curtis91 curtis92
        List<String> hmget9 = jedis.hmget("rank9", "curtis", "curtis91", "curtis92");
        // hmget9=[null, v91, v92]
        LOGGER.info("hmget9={}", hmget9);


        /**
         * 指令格式：HKEYS key
         * 时间复杂度：O(N)， N 为哈希表的大小。
         * 指令描述：返回哈希表 key 中的所有域。
         * 返回值：一个包含哈希表中所有域的表。当 key 不存在时，返回一个空表。
         */
        jedis.hset("rank10", "curtis101", "v101");
        jedis.hset("rank10", "curtis102", "v102");
        // 等同于：hkeys rank10
        Set<String> rank10 = jedis.hkeys("rank10");
        // rank10=[curtis102, curtis101]
        LOGGER.info("rank10={}", rank10);


        /**
         * 指令格式：HGETALL key
         * 时间复杂度：O(N)， N 为哈希表的大小。
         * 指令描述：返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
         * 返回值：以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
         */
        jedis.hset("rank11", "curtis111", "v111");
        jedis.hset("rank11", "curtis112", "v112");
        // 等同于：hgetall rank11
        Map<String, String> rank11 = jedis.hgetAll("rank11");
        // rank11={curtis111=v111, curtis112=v112}
        LOGGER.info("rank11={}", rank11);
    }
}
