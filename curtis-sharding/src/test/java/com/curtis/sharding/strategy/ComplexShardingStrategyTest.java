package com.curtis.sharding.strategy;

import com.curtis.sharding.CurtisShardingApplication;
import com.curtis.sharding.entity.EnterpriseSale;
import com.curtis.sharding.mapper.EnterpriseSaleMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author curtis
 * @desc 测试复合分片策略（ComplexShardingStrategy）
 * 对应ComplexShardingStrategy。复合分片策略。提供对SQL语句中的 =, >, <, >=, <=, IN 和 BETWEEN AND 的分片操作支持。
 * ComplexShardingStrategy 支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，
 * 而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。
 * @date 2020-09-11
 * @email 397773935@qq.com
 * @reference
 */
@ActiveProfiles("dev-complex")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurtisShardingApplication.class)
public class ComplexShardingStrategyTest {

    @Autowired
    private EnterpriseSaleMapper enterpriseSaleMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ComplexShardingStrategyTest.class);

    /*
     测试之前需要手动清理所有分库以及分表
     TRUNCATE TABLE db_test_0.tb_enterprise_sale_110100_2019;
     TRUNCATE TABLE db_test_0.tb_enterprise_sale_110100_2020;
     TRUNCATE TABLE db_test_0.tb_enterprise_sale_130100_2019;
     TRUNCATE TABLE db_test_0.tb_enterprise_sale_130100_2020;
     */

    /**
     * 测试：
     * 在db_test_0中插入企业10000城市code为110100和130100的日期为2018、2019、2020年第一天和最后一天的销售数据。
     * 在db_test_1中插入企业10001城市code为110100和130100的日期为2018、2019、2020年第一天和最后一天的销售数据。
     */
    @Test
    public void testInsertOrder() throws ParseException {
        List<String> cityCodeList = Lists.newArrayList("110100", "130100");
        List<Long> enterpriseIdList = Lists.newArrayList(10000L, 10001L);
        List<Date> saleDayList = Lists.newArrayList(
                DateUtils.parseDate("2019-01-01", "yyyy-MM-dd"),
                DateUtils.parseDate("2019-12-31", "yyyy-MM-dd"),
                DateUtils.parseDate("2020-01-01", "yyyy-MM-dd"),
                DateUtils.parseDate("2020-12-31", "yyyy-MM-dd"));

        // 虽然这里主键id没有业务意义，但是最好还是要各库各表的主键id不同
        long id = 1L;
        for (Long enterpriseId : enterpriseIdList) {
            for (String cityCode : cityCodeList) {
                for (Date saleDay : saleDayList) {
                    enterpriseSaleMapper.insertEnterpriseSale(
                            new EnterpriseSale(id, enterpriseId, cityCode, saleDay, BigDecimal.valueOf(1000L)));
                    id++; // 插入数据后主键id+1
                }
            }
        }
    }

    /**
     * 测试：查询企业10001L在城市110100的所有销售信息
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 有城市code但是无交易日期则只能确定tb_enterprise_sale_110100_%，
     * 也就是查询db_test_1库中tb_enterprise_sale_110100_2019和tb_enterprise_sale_110100_2020。
     */
    @Test
    public void testGetEnterpriseSaleByEnterpriseAndCityCode() {
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCode(10001L, "110100");
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询所有企业在城市110100的所有销售信息
     * 无企业信息，则不确定在哪个库，查询所有库。
     * 有城市code但是无交易日期则只能确定tb_enterprise_sale_110100_%，
     * 也就是查询数据库db_test_0和db_test_1中tb_enterprise_sale_110100_2019和tb_enterprise_sale_110100_2020。
     */
    @Test
    public void testGetEnterpriseSaleByCityCode() {
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCode(null, "110100");
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询指定企业的所有交易信息
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 城市及日期都为空，也就是当所有表分片键都是空时不会走分片算法而是根据TableRuleConfiguration中的actualDataNodes节点表达式指定表来确定。
     * 由于我们为了简化操作并且实际中很少不加表分片键来查询数据，所以随便使用了"db_test_${0..1}.tb_enterprise_sale_${0..3}"
     * 但是显然没有这个分表，所以抛出表不存在的异常，所以实际使用当中还是要完整拼接出所有分片表来作为actualDataNodes参数的值
     * Table 'db_test_1.tb_enterprise_sale_0' doesn't exist
     */
    @Test
    public void testGetEnterpriseSaleByEnterpriseId() {
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCode(10001L, null);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询所有企业所有交易信息
     * 无企业id则无数据库分片键则查询TableRuleConfiguration中的actualDataNodes节点指定的所有数据库
     * 无城市和日期则无数据表分片键则查询TableRuleConfiguration中的actualDataNodes节点指定的所有表
     * 也就是查询所有数据库和所有表，所以这个节点还是很重要的，如果实际业务场景中有该情况则应该认真编写表达式。
     * 下面的查询将抛出异常：Table 'db_test_1.tb_enterprise_sale_0' doesn't exist
     */
    @Test
    public void testGetEnterpriseSale() {
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCode(null, null);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询指定企业指定交易日期销售数据
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 有交易日期但是无城市code则只能确定tb_enterprise_sale_%_2019，
     * 也就是查询数据库db_test_1中tb_enterprise_sale_110100_2019和tb_enterprise_sale_130100_2019。
     */
    @Test
    public void testGetEnterpriseSaleBySaleDay() throws ParseException {
        Date saleDay = DateUtils.parseDate("2019-12-31", "yyyy-MM-dd");
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleBySaleDay(10001L, saleDay);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询指定企业的所有交易信息
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 城市及日期都为空，也就是当所有表分片键都是空时不会走分片算法而是根据TableRuleConfiguration中的actualDataNodes节点表达式指定表来确定。
     * 由于我们为了简化操作并且实际中很少不加表分片键来查询数据，所以随便使用了"db_test_${0..1}.tb_enterprise_sale_${0..3}"
     * 但是显然没有这个分表，所以抛出表不存在的异常，所以实际使用当中还是要完整拼接出所有分片表来作为actualDataNodes参数的值
     * Table 'db_test_1.tb_enterprise_sale_0' doesn't exist
     */
    @Test
    public void testGetEnterpriseSaleByEnterpriseId2() {
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleBySaleDay(10001L, null);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询指定企业指定城市指定日期的销售数据
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 城市和日期都不为空，则根据城市code和日期定位到tb_enterprise_sale_{110100,130100}_2019
     * 也就是查询数据库db_test_1中tb_enterprise_sale_110100_2019和tb_enterprise_sale_130100_2019。
     *
     * @throws ParseException
     */
    @Test
    public void testGetEnterpriseSaleByCityCodeAndSaleDay() throws ParseException {
        List<String> cityCodes = Lists.newArrayList("110100", "130100");
        Date saleDay = DateUtils.parseDate("2019-12-31", "yyyy-MM-dd");
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCodeAndSaleDay(10001L, cityCodes, saleDay);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }

    /**
     * 测试：查询指定企业指定城市指定日期范围的销售数据
     * 企业id为10001L，根据尾数取模则确定数据库为db_test_1。
     * 城市和日期都不为空，则根据城市code和日期定位到tb_enterprise_sale_{110100}_{2019..2020}
     * 也就是查询数据库db_test_1中tb_enterprise_sale_110100_2019和tb_enterprise_sale_110100_2020。
     *
     * @throws ParseException
     */
    @Test
    public void testGetEnterpriseSaleByCityCodesAndBetweenSaleDay() throws ParseException {
        List<String> cityCodes = Lists.newArrayList("110100");
        Date minSaleDay = DateUtils.parseDate("2019-12-31", "yyyy-MM-dd");
        Date maxSaleDay = DateUtils.parseDate("2020-01-01", "yyyy-MM-dd");
        List<EnterpriseSale> enterpriseSales = enterpriseSaleMapper.getEnterpriseSaleByCityCodesAndBetweenSaleDay(10001L,
                cityCodes, minSaleDay, maxSaleDay);
        LOGGER.info("enterpriseSales = {}", enterpriseSales);
    }
}
