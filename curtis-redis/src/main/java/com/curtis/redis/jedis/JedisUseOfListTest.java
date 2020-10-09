package com.curtis.redis.jedis;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author curtis
 * @desc 使用Jedis执行List指令
 * @date 2020-08-07
 * @email 397773935@qq.com
 * @reference
 */
public class JedisUseOfListTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUseOfListTest.class);

    @Before
    public void before() {
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");
        jedis.flushDB();
    }


    @Test
    public void testJedisUseOfList(){
        String host = "192.168.100.161";
        int port = 6379;
        Jedis jedis = new Jedis(host, port);
        jedis.auth("123456");


        /**
         * 指令格式：LPUSH key value [value …]
         * 时间复杂度：O(1)
         * 指令描述：将一个或多个值value插入到列表key的表头。如果有多个value值，那么各个value 值按从左到右的顺序依次插入到表头.
         * 比如说，对空列表mylist执行命令LPUSH mylist a b c，列表的值将是c b a，这等同于原子性地执行LPUSH mylist a、 LPUSH mylist b和 LPUSH mylist c三个命令。
         * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。当 key 存在但不是列表类型时，返回一个错误。
         * 返回值：执行LPUSH命令后，列表的长度。注意这里返回的不是操作成功的数据个数，而是列表的长度。
         */
        // 等同于：lpush letter1 a b c，注意lpush返回的是当前列表指令命令后的总长度而不是操作成功的数据个数
        Long lpush1 = jedis.lpush("letter1", "a", "b", "c");
        List<String> letter11 = jedis.lrange("letter1", 0, -1);
        // lpush1=3, letter11=[c, b, a]
        LOGGER.info("lpush1={}, letter11={}", lpush1, letter11);
        Long lpush2 = jedis.lpush("letter1", "e", "f");
        List<String> letter12 = jedis.lrange("letter1", 0, -1);
        // lpush2=5, letter12=[f, e, c, b, a]
        LOGGER.info("lpush2={}, letter12={}", lpush2, letter12);


        /**
         * 指令格式：RPUSH key value [value …]
         * 时间复杂度：O(1)
         * 指令描述：将一个或多个值value插入到列表key的表尾(最右边)。如果有多个value值，那么各个value值按从左到右的顺序依次插入到表尾：
         * 比如对一个空列表mylist执行RPUSH mylist a b c，得出的结果列表为a b c，等同于执行命令RPUSH mylist a、 RPUSH mylist b、 RPUSH mylist c。
         * 如果key不存在，一个空列表会被创建并执行RPUSH操作。当key存在但不是列表类型时，返回一个错误。
         * 返回值：执行RPUSH操作后，表的长度。
         */
        Long rpush1 = jedis.rpush("letter2", "a", "b", "c");
        List<String> letter21 = jedis.lrange("letter2", 0, -1);
        // rpush1=3, letter21=[a, b, c]
        LOGGER.info("rpush1={}, letter21={}", rpush1, letter21);
        Long rpush2 = jedis.rpush("letter2", "e", "f");
        List<String> letter22 = jedis.lrange("letter2", 0, -1);
        // rpush2=5, letter22=[a, b, c, e, f]
        LOGGER.info("rpush2={}, letter22={}", rpush2, letter22);


        /**
         * 指令格式：LPOP key
         * 时间复杂度：O(1)
         * 指令描述：移除并返回列表key的头元素。
         * 返回值：列表的头元素。当key不存在时，返回nil 。
         */
        /**
         * 指令格式：RPOP key
         * 时间复杂度：O(1)
         * 指令描述：移除并返回列表key的尾元素。
         * 返回值：列表的尾元素。当key不存在时，返回nil 。
         */
        jedis.rpush("letter3", "a", "b", "c");
        List<String> letter3 = jedis.lrange("letter3", 0, -1);
        // letter3=[a, b, c]
        LOGGER.info("letter3={}", letter3);
        // 等同于：lpop letter3
        String letter31 = jedis.lpop("letter3");
        // 等同于：rpop letter3
        String letter32 = jedis.rpop("letter3");
        letter3 = jedis.lrange("letter3", 0, -1);
        // letter31=a, letter32=c, letter3=[b]
        LOGGER.info("letter31={}, letter32={}, letter3={}", letter31, letter32, letter3);


        /**
         * 指令格式：LREM key count value
         * 时间复杂度：O(N)， N 为列表的长度。
         * 指令描述：根据参数 count 的值，移除列表中与参数 value 相等的元素。
         *     count 的值可以是以下几种：
         *         count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
         *         count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
         *         count = 0 : 移除表中所有与 value 相等的值。
         * 返回值：被移除元素的数量。因为不存在的key被视作空表(empty list)，所以当 key不存在时，LREM命令总是返回 0 。
         */
        jedis.rpush("letter4", "a", "a", "b", "a", "a");
        List<String> letter41 = jedis.lrange("letter4", 0, -1);
        // 等同于：lrem letter4 2 a
        Long lrem41 = jedis.lrem("letter4", 2, "a");
        List<String> letter42 = jedis.lrange("letter4", 0, -1);
        // 等同于：lrem letter4 -2 a
        Long lrem42 = jedis.lrem("letter4", -2, "a");
        List<String> letter43 = jedis.lrange("letter4", 0, -1);
        // letter41=[a, a, b, a, a], letter42=[b, a, a], letter43=[b]
        LOGGER.info("letter41={}, letter42={}, letter43={}", letter41, letter42, letter43);
        // lrem41=2, lrem42=2
        LOGGER.info("lrem41={}, lrem42={}", lrem41, lrem42);


        /**
         * 指令格式：LLEN key
         * 时间复杂度：O(1)
         * 指令描述：返回列表key的长度。如果key不存在，则key被解释为一个空列表，返回0.如果key不是列表类型，返回一个错误。
         * 返回值：列表 key 的长度。
         */
        jedis.rpush("letter5", "a", "a", "b", "a", "a");
        Long llen51 = jedis.llen("letter5");
        // 等同于：lrem letter5 2 a
        Long lrem5 = jedis.lrem("letter5", 2, "a");
        // 等同于：llen letter5
        Long llen52 = jedis.llen("letter5");
        // llen51=5, llen52=3
        LOGGER.info("llen51={}, llen52={}", llen51, llen52);


        /**
         * 指令格式：LINDEX key index
         * 时间复杂度：O(N)， N为到达下标index过程中经过的元素数量。因此，对列表的头元素和尾元素执行LINDEX 命令复杂度为O(1)。
         * 指令描述：返回列表key中，下标为index的元素。下标(index)参数以0开始，也可以使用负数下标，以-1
         *      表示列表的最后一个元素，以此类推。如果key不是列表类型，返回一个错误。
         * 返回值：列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil 。
         */
        jedis.rpush("rank6", "curtis1", "curtis2", "curtis3");
        List<String> rank6 = jedis.lrange("rank6", 0, -1);
        // 等同于：lindex rank6 0
        String rank61 = jedis.lindex("rank6", 0);
        String rank62 = jedis.lindex("rank6", -1);
        // rank6=[curtis1, curtis2, curtis3], rank61=curtis1, rank62=curtis3
        LOGGER.info("rank6={}, rank61={}, rank62={}", rank6, rank61, rank62);


        /**
         * 指令格式：LSET key index value
         * 时间复杂度：对头元素或尾元素进行 LSET 操作，复杂度为 O(1)。其他情况下，为 O(N)， N 为列表的长度。
         * 指令描述：将列表 key 下标为 index 的元素的值设置为 value 。当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
         * 返回值：操作成功返回 ok ，否则返回错误信息。
         */
        jedis.rpush("rank7", "curtis1", "curtis2", "curtis3");
        List<String> rank7 = jedis.lrange("rank7", 0, -1);
        // rank7=[curtis1, curtis2, curtis3]
        LOGGER.info("rank7={}", rank7);
        // 等同于：lset rank7 0 curtis0
        String rank71 = jedis.lset("rank7", 0, "curtis0");
        String rank72 = jedis.lset("rank7", -1, "curtis-1");
        rank7 = jedis.lrange("rank7", 0, -1);
        // rank7=[curtis0, curtis2, curtis-1], rank71=OK, rank72=OK
        LOGGER.info("rank7={}, rank71={}, rank72={}", rank7, rank71, rank72);


        /**
         * 指令格式：LRANGE key start stop
         * 时间复杂度：O(S+N)， S为偏移量 start， N为指定区间内元素的数量。
         * 指令描述：返回列表key中指定区间内的元素，区间以偏移量start和stop指定。下标(index)参数以0开始，以此类推。也可以使用负数下标，
         *      以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
         * 返回值：返回列表key中指定区间内的元素，区间以偏移量start和stop 指定。
         * 注意：LRANGE是左闭右闭。超出范围的下标值不会引起错误。
         */
        jedis.rpush("rank8", "curtis1", "curtis2", "curtis3");
        // 等同于：lrange rank8 0 -1
        List<String> rank8 = jedis.lrange("rank8", 0, -1);
        // rank8=[curtis1, curtis2, curtis3]
        LOGGER.info("rank8={}", rank8);
    }
}
