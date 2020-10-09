package com.curtis.redis.jedis;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

/**
 * @author curtis
 * @desc 使用Jedis执行Sorted Set指令
 * @date 2020-08-07
 * @email 397773935@qq.com
 * @reference
 */
public class JedisUseOfSortedSetTest {

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
    public void testJedisUseOfSortedSet() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");


        /**
         * 指令格式：ZADD key score member [[score member] [score member] …]
         * 时间复杂度：O(M*log(N))， N是有序集的基数， M为成功添加的新成员的数量。
         * 指令描述：将一个或多个member元素及其score值加入到有序集key中。如果某个member已经是有序集的成员，那么更新这个 member的score值，
         *  并通过重新插入这个 member 元素，来保证该member在正确的位置上。score值可以是整数值或双精度浮点数。
         *  如果key不存在，则创建一个空的有序集并执行ZADD操作。当 key存在但不是有序集类型时，返回一个错误。
         * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
         */
        jedis.zadd("sortedSetKey1", 10, "curtis10");
        jedis.zadd("sortedSetKey1", 11, "curtis11");
        jedis.zadd("sortedSetKey1", 1, "curtis1");
        Set<String> sortedSetKey1 = jedis.zrange("sortedSetKey1", 0, -1);
        LOGGER.info("sortedSetKey1={}", sortedSetKey1);

        Map<String, Double> sortedSetMap = Maps.newHashMap();
        sortedSetMap.put("curtis11", 11d);
        sortedSetMap.put("curtis10", 10d);
        sortedSetMap.put("curtis1", 1d);
        Long result1 = jedis.zadd("sortedSetKey2", sortedSetMap);
        Set<String> sortedSetKey2 = jedis.zrange("sortedSetKey2", 0, -1);
        LOGGER.info("result1={},sortedSetKey2={}", result1, sortedSetKey2);


        /**
         * 指令格式：ZSCORE key member
         * 时间复杂度：O(1)
         * 指令描述：返回有序集key中，成员member的score值。如果member元素不是有序集key的成员，或key不存在，返回nil。
         * 返回值：member成员的score值，以字符串形式表示。
         */
        Map<String, Double> sortedSetMap2 = Maps.newHashMap();
        sortedSetMap2.put("curtis1", 5000d);
        sortedSetMap2.put("curtis2", 6000d);
        sortedSetMap2.put("curtis3", 4000d);
        Long result2 = jedis.zadd("sortedSetKey3", sortedSetMap2);
        Double zscore = jedis.zscore("sortedSetKey3", "curtis2");
        LOGGER.info("result3={},zscore={}", result2, zscore);


        /**
         * 指令格式：ZINCRBY key increment member
         * 时间复杂度：O(log(N))
         * 指令描述：为有序集key的成员member的score值加上增量increment。
         * 可以通过传递一个负数值increment，让score减去相应的值，比如ZINCRBY key -5 member，就是让member的score值减去5。
         * 当key不存在，或member不是key的成员时，ZINCRBY key increment member 等同于ZADD key increment member 。当key不是有序集类型时，返回一个错误。score值可以是整数值或双精度浮点数。
         * 返回值：member成员的新score值，以字符串形式表示。
         */
        Map<String, Double> sortedSetMap3 = Maps.newHashMap();
        sortedSetMap3.put("curtis1", 5000d);
        sortedSetMap3.put("curtis2", 6000d);
        sortedSetMap3.put("curtis3", 4000d);
        Long result3 = jedis.zadd("sortedSetKey3", sortedSetMap3);
        Double zincrby3 = jedis.zincrby("sortedSetKey3", 2000d, "curtis1");
        Double zscore3 = jedis.zscore("sortedSetKey3", "curtis1");
        LOGGER.info("result3={},zincrby3={},zscore3={}", result3, zincrby3, zscore3);


        /**
         * 指令格式：ZINCRBY key increment member
         * 时间复杂度：O(1)
         * 指令描述：返回有序集key的基数。
         * 返回值：当 key存在且是有序集类型时，返回有序集的基数。当key不存在时，返回0。
         */
        Map<String, Double> sortedSetMap4 = Maps.newHashMap();
        sortedSetMap4.put("curtis1", 5000d);
        sortedSetMap4.put("curtis2", 6000d);
        Long result4 = jedis.zadd("sortedSetKey4", sortedSetMap4);
        Long sortedSetKey41 = jedis.zcard("sortedSetKey4");
        Long zadd4 = jedis.zadd("sortedSetKey4", 40000d, "curtis3");
        Long sortedSetKey42 = jedis.zcard("sortedSetKey4");
        LOGGER.info("result4={},sortedSetKey41={},zadd4={},sortedSetKey42={}", result4, sortedSetKey41, zadd4, sortedSetKey42);


        /**
         * 指令格式：ZCOUNT key min max
         * 时间复杂度：O(log(N))， N为有序集的基数。
         * 指令描述：返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员的数量。
         * 返回值：score值在min和max之间的成员的数量。
         */
        Map<String, Double> sortedSetMap5 = Maps.newHashMap();
        sortedSetMap5.put("curtis1", 5000d);
        sortedSetMap5.put("curtis2", 6000d);
        sortedSetMap5.put("curtis3", 4000d);
        sortedSetMap5.put("curtis4", 7000d);
        Long result5 = jedis.zadd("sortedSetKey5", sortedSetMap5);
        Long zcount = jedis.zcount("sortedSetKey5", 4000d, 6000d);
        LOGGER.info("result5={},zcount={}", result5, zcount);


        /**
         * 指令格式：ZRANGE key start stop [WITHSCORES]
         * 时间复杂度：O(log(N)+M)， N为有序集的基数，而M为结果集的基数。
         * 指令描述：返回有序集key中，指定区间内的成员。其中成员的位置按score值递增(从小到大)来排序。具有相同score值的成员按字典序(lexicographical order )来排列。
         * 如果你需要成员按score值递减(从大到小)来排列，请使用ZREVRANGE key start stop [WITHSCORES] 命令。
         * 下标参数 start和stop都以0为底，也就是说，以0表示有序集第一个成员，以1表示有序集第二个成员，以此类推。 你也可以使用负数下标，以-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
         * 超出范围的下标并不会引起错误。 比如说，当start的值比有序集的最大下标还要大，或是start > stop时，ZRANGE命令只是简单地返回一个空列表。
         * 另一方面，假如stop参数的值比有序集的最大下标还要大，那么Redis将stop当作最大下标来处理。
         * 可以通过使用 WITHSCORES 选项，来让成员和它的score值一并返回，返回列表以value1,score1, ..., valueN,scoreN 的格式表示。客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
         */
        Map<String, Double> sortedSetMap6 = Maps.newHashMap();
        sortedSetMap6.put("curtis1", 5000d);
        sortedSetMap6.put("curtis2", 6000d);
        sortedSetMap6.put("curtis3", 4000d);
        sortedSetMap6.put("curtis4", 7000d);
        Long result6 = jedis.zadd("sortedSetKey6", sortedSetMap6);
        Set<String> sortedSetKey61 = jedis.zrange("sortedSetKey6", 0, -1);
        Set<String> sortedSetKey62 = jedis.zrange("sortedSetKey6", 0, 2);
        LOGGER.info("result6={},sortedSetKey61={},sortedSetKey62={}", result6, sortedSetKey61, sortedSetKey62);


        /**
         * 指令格式：ZRANK key member
         * 时间复杂度： O(log(N))
         * 指令描述：返回有序集key中成员member的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，score 值最小的成员排名为0。
         * 使用ZREVRANK key member命令可以获得成员按score值递减(从大到小)排列的排名。
         * 返回值：如果member是有序集key的成员，返回member的排名。 如果member不是有序集key的成员，返回nil。
         */
        Map<String, Double> sortedSetMap7 = Maps.newHashMap();
        sortedSetMap7.put("curtis1", 5000d);
        sortedSetMap7.put("curtis2", 6000d);
        sortedSetMap7.put("curtis3", 4000d);
        sortedSetMap7.put("curtis4", 7000d);
        Long result7 = jedis.zadd("sortedSetKey7", sortedSetMap7);
        Long zrank71 = jedis.zrank("sortedSetKey7", "curtis3");
        Long zrank72 = jedis.zrank("sortedSetKey7", "curtis4");
        Long zrank73 = jedis.zrank("sortedSetKey7", "curtis8");
        LOGGER.info("result7={},zrank71={},zrank72={},zrank73={}", result7, zrank71, zrank72, zrank73);


        /**
         * 指令格式：ZREM key member [member …]
         * 时间复杂度：O(M*log(N))，N为有序集的基数，M为被成功移除的成员的数量。
         * 指令描述：移除有序集key中的一个或多个成员，不存在的成员将被忽略。当key存在但不是有序集类型时，返回一个错误。
         * 返回值：被成功移除的成员的数量，不包括被忽略的成员。
         */
        Map<String, Double> sortedSetMap8 = Maps.newHashMap();
        sortedSetMap8.put("curtis1", 5000d);
        sortedSetMap8.put("curtis2", 6000d);
        sortedSetMap8.put("curtis3", 4000d);
        Long result8 = jedis.zadd("sortedSetKey8", sortedSetMap8);
        Long zrem8 = jedis.zrem("sortedSetKey8", "curtis1", "curtis2", "curtis4");
        Set<String> zrange8 = jedis.zrange("sortedSetKey8", 0, -1);
        LOGGER.info("result8={},zrem8={},zrange8={}", result8, zrem8, zrange8);
    }
}
