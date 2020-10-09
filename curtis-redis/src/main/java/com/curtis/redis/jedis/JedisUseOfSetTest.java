package com.curtis.redis.jedis;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author curtis
 * @desc 使用Jedis执行Set指令
 * @date 2020-08-07
 * @email 397773935@qq.com
 * @reference
 */
public class JedisUseOfSetTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUseOfSetTest.class);

    @Before
    public void before() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");
        jedis.flushDB();
    }


    @Test
    public void testJedisUseOfSet(){
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");


        /**
         * 指令格式：SADD key member [member …]
         * 时间复杂度：O(N)， N是被添加的元素的数量。
         * 指令描述：将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略。
         *      假如key不存在，则创建一个只包含member元素作成员的集合。当key不是集合类型时，返回一个错误。
         * 返回值：被添加到集合中的新元素的数量，不包括被忽略的元素。
         */
        Long sadd11 = jedis.sadd("db1", "redis", "mysql");
        // 等同于sadd db1 redis mongodb
        Long sadd12 = jedis.sadd("db1", "redis", "mongodb");
        Set<String> db1 = jedis.smembers("db1");
        // sadd11=2, sadd12=1, rank72=[redis, mysql, mongodb]
        LOGGER.info("sadd11={}, sadd12={}, rank72={}", sadd11, sadd12, db1);


        /**
         * 指令格式：SISMEMBER key member
         * 时间复杂度：O(1)
         * 指令描述：判断member元素是否集合key的成员。
         * 返回值：如果member元素是集合的成员，返回1。如果member元素不是集合的成员，或key不存在返回0 。
         */
        jedis.sadd("db2", "redis", "mysql", "mongodb");
        // 等同于：sismember db2 redis
        Boolean sismember21 = jedis.sismember("db2", "redis");
        Boolean sismember22 = jedis.sismember("db2", "SQLServer");
        // sismember21=true, sismember22=false
        LOGGER.info("sismember21={}, sismember22={}", sismember21, sismember22);


        /**
         * 指令格式：SPOP key [count]
         * 时间复杂度：O(1)
         * 指令描述：移除并返回集合中的一个或多个随机元素。
         *      如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用SRANDMEMBER key [count] 命令。
         * 返回值：被移除的随机元素。当key不存在或key是空集时，返回nil。
         */
        jedis.sadd("db3", "redis", "mysql", "mongodb", "SQLServer", "oracle");
        // 等同于：spop db3 2
        Set<String> db31 = jedis.spop("db3", 2);
        String db32 = jedis.spop("db3");
        // db31=[mysql, oracle], db32=redis
        // db31=[mysql, mongodb], db32=SQLServer
        LOGGER.info("db31={}, db32={}", db31, db32);


        /**
         * 指令格式：SREM key member [member …]
         * 时间复杂度：O(N)，N 为给定 member 元素的数量。
         * 指令描述：移除集合key中的一个或多个member 元素，不存在的member元素会被忽略。当key不是集合类型，返回一个错误。
         * 返回值：被成功移除的元素的数量，不包括被忽略的元素。
         */
        jedis.sadd("db4", "redis", "mysql", "mongodb", "SQLServer");
        // 等同于：srem db4 redis oracle mysql
        Long srem = jedis.srem("db4", "redis", "oracle", "mysql");
        Set<String> db4 = jedis.smembers("db4");
        // srem=2, db4=[SQLServer, mongodb]
        LOGGER.info("srem={}, db4={}", srem, db4);


        /**
         * SMOVE
         */


        /**
         * 指令格式：SCARD key
         * 时间复杂度：O(1)
         * 指令描述：返回集合key的基数(集合中元素的数量)。
         * 返回值：集合的基数。当key不存在时，返回0。
         */
        jedis.sadd("db6", "redis", "mysql", "mongodb", "SQLServer");
        // 等同于：scard db6
        Long db6 = jedis.scard("db6");
        // db6=4
        LOGGER.info("db6={}", db6);


        /**
         * 指令格式：SMEMBERS key
         * 时间复杂度：O(N)，N为集合的基数。
         * 指令描述：返回集合key中的所有成员。不存在的key被视为空集合。
         * 返回值：集合中的所有成员。
         */
        jedis.sadd("db7", "redis", "mysql", "mongodb", "SQLServer");
        // 等同于：smembers db7
        Set<String> db7 = jedis.smembers("db7");
        // db7=[redis, mysql, SQLServer, mongodb]
        LOGGER.info("db7={}", db7);


        /**
         * 指令格式：SINTER key [key …]
         * 时间复杂度：O(N * M)，N为给定集合当中基数最小的集合，M为给定集合的个数。
         * 指令描述：返回一个集合的全部成员，该集合是所有给定集合的交集。不存在的key被视为空集。当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
         * 返回值：交集成员的列表。
         */
        jedis.sadd("db81", "redis", "mysql", "mongodb");
        jedis.sadd("db82", "redis", "mongodb", "SQLServer");
        // 等同于：sinter db81 db82
        Set<String> sinter = jedis.sinter("db81", "db82");
        // sinter=[redis, mongodb]
        LOGGER.info("sinter={}", sinter);


        /**
         * 指令格式：SINTERSTORE destination key [key …]
         * 时间复杂度：O(N * M)，N为给定集合当中基数最小的集合，M为给定集合的个数。
         * 指令描述：这个命令类似于SINTER key [key …] 命令，但它将结果保存到destination集合，而不是简单地返回结果集。
         *      如果destination集合已经存在，则将其覆盖。destination可以是key本身。
         * 返回值：结果集中的成员数量。
         */
        jedis.sadd("db91", "redis", "mysql", "mongodb");
        jedis.sadd("db92", "redis", "mongodb", "SQLServer");
        // 等同于：sinterstore db9 db91 db92
        Long sinterstore = jedis.sinterstore("db9", "db91", "db92");
        Set<String> db9 = jedis.smembers("db9");
        // sinterstore=2, db9=[redis, mongodb]
        LOGGER.info("sinterstore={}, db9={}", sinterstore, db9);


        /**
         * 指令格式：SUNION key [key …]
         * 时间复杂度：O(N)，N是所有给定集合的成员数量之和。
         * 指令描述：返回一个集合的全部成员，该集合是所有给定集合的并集。不存在的key被视为空集。
         * 返回值：并集成员的列表。
         */
        jedis.sadd("db101", "redis", "mysql", "mongodb");
        jedis.sadd("db102", "redis", "mongodb", "SQLServer");
        // 等同于：sunion db101 db102
        Set<String> sunion = jedis.sunion("db101", "db102");
        // sunion=[redis, mysql, SQLServer, mongodb]
        LOGGER.info("sunion={}", sunion);


        /**
         * 指令格式：SUNIONSTORE destination key [key …]
         * 时间复杂度：O(N)，N是所有给定集合的成员数量之和。
         * 指令描述：这个命令类似于SUNION key [key …]命令，但它将结果保存到destination集合，而不是简单地返回结果集。如果destination已经存在，则将其覆盖。destination可以是 key 本身。
         * 返回值：结果集中的元素数量。
         */
        jedis.sadd("db111", "redis", "mysql", "mongodb");
        jedis.sadd("db112", "redis", "mongodb", "SQLServer");
        // 等同于：sunionstore db11 db111 db112
        Long sunionstore = jedis.sunionstore("db11", "db111", "db112");
        Set<String> db11 = jedis.smembers("db11");
        // sunionstore=4, db11=[redis, mysql, SQLServer, mongodb]
        LOGGER.info("sunionstore={}, db11={}", sunionstore, db11);


        /**
         * 指令格式：SDIFF key [key …]
         * 时间复杂度：O(N)，N是所有给定集合的成员数量之和。
         * 指令描述：返回一个集合的全部成员，该集合是所有给定集合之间的差集。不存在的key被视为空集。
         * 返回值：一个包含差集成员的列表。
         */
        jedis.sadd("db121", "redis", "mysql", "mongodb");
        jedis.sadd("db122", "redis", "mongodb", "SQLServer");
        // 等同于：sdiff=[mysql]
        Set<String> sdiff = jedis.sdiff("db121", "db122");
        // sunionstore=4, db11=[redis, mysql, SQLServer, mongodb]
        LOGGER.info("sdiff={}", sdiff);


        /**
         * 指令格式：SDIFFSTORE destination key [key …]
         * 时间复杂度：O(N)，N是所有给定集合的成员数量之和。
         * 指令描述：这个命令的作用和SDIFF key [key …] 类似，但它将结果保存到destination集合，而不是简单地返回结果集。
         *      如果destination集合已经存在，则将其覆盖。destination可以是key本身。
         * 返回值：结果集中的元素数量。
         */
        jedis.sadd("db131", "redis", "mysql", "mongodb");
        jedis.sadd("db132", "redis", "mongodb", "SQLServer");
        // 等同于：sdiffstore db13 db131 db132
        Long sdiffstore = jedis.sdiffstore("db13", "db131", "db132");
        Set<String> db13 = jedis.smembers("db13");
        // sdiffstore=1, db13=[mysql]
        LOGGER.info("sdiffstore={}, db13={}", sdiffstore, db13);
    }
}
