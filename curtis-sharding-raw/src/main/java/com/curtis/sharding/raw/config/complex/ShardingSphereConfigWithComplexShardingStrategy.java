package com.curtis.sharding.raw.config.complex;

import com.curtis.sharding.raw.config.complex.algorithm.ComplexShardingTableAlgorithm;
import com.curtis.sharding.raw.config.complex.algorithm.PreciseShardingDatabaseAlgorithmWithModulo;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.underlying.common.config.properties.ConfigurationPropertyKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author curtis
 * @desc 演示复合分片策略
 * 对应 ComplexShardingStrategy。复合分片策略。提供对 SQL 语句中的 =, >, <, >=, <=, IN 和 BETWEEN AND 的分片操作支持。
 * ComplexShardingStrategy 支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，
 * 而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
@Profile("dev-complex")
@Configuration
public class ShardingSphereConfigWithComplexShardingStrategy {

    @Bean(value = "testDataSource")
    public DataSource getDataSource() throws SQLException {
        // 1.数据源集合配置（该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建）
        Map<String, DataSource> dataSourceMap = this.createDataSourceMap();

        // 2. 分片规则配置，包含各逻辑表的分片策略配置
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        // 2.1 企业日度销售数据表（按城市和年份进行分表）分片规则配置
        TableRuleConfiguration tableRuleConfiguration = this.getTableRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(tableRuleConfiguration);

        // 3. ShardingSphere数据源属性配置（非必须）
        Properties properties = this.getProperties();

        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, properties);
    }

    /**
     * 表分片规则配置，以企业id尾数取模确定分库，以城市code和交易日期所在年份确定分表
     *
     * @return
     */
    private TableRuleConfiguration getTableRuleConfiguration() {
        // 创建表分片规则配置对象，声明逻辑表和实际数据节点(逻辑表只有表名，实际数据节点由数据库和表名组成)
        // 实际数据节点的数据库名需要使用dataSourceMap中的key，也就是数据库（准确是数据源）的别名，数据库分片配置也是如此
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("tb_enterprise_sale", "db_test_${0..1}.tb_enterprise_sale_${0..3}");
        // 数据库分片策略配置：企业id尾数取模
        StandardShardingStrategyConfiguration standardShardingDatabaseStrategyConfiguration
                = new StandardShardingStrategyConfiguration("enterprise_id", new PreciseShardingDatabaseAlgorithmWithModulo());
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(standardShardingDatabaseStrategyConfiguration);

        // 数据表分片策略配置：多分片键使用符合分片策略，这里自定义实现精确以及范围分片算法
        ComplexShardingStrategyConfiguration complexShardingStrategyConfiguration
                = new ComplexShardingStrategyConfiguration("city_code,sale_day", new ComplexShardingTableAlgorithm());
        tableRuleConfiguration.setTableShardingStrategyConfig(complexShardingStrategyConfiguration);

        tableRuleConfiguration.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "id", this.getProperties()));
        return tableRuleConfiguration;
    }

    /**
     * 创建数据源集合（该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建）
     *
     * @return 数据源集合
     */
    private Map<String, DataSource> createDataSourceMap() {
        // 配置真实数据源Map，需要注意的是dataSourceMap的key可以指定为别的名称，之后其他分片配置均要使用该别名
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        // 配置第 1 个数据源
        HikariDataSource dataSource0 = new HikariDataSource();
        dataSource0.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource0.setJdbcUrl("jdbc:mysql://localhost:3306/db_test_0?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        dataSource0.setUsername("root");
        dataSource0.setPassword("root");
        dataSourceMap.put("db_test_0", dataSource0);
        // 配置第 2 个数据源
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://localhost:3306/db_test_1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");
        dataSourceMap.put("db_test_1", dataSource1);
        return dataSourceMap;
    }

    /**
     * ShardingSphere属性属性配置（非必须）
     * 相关配置可参照配置枚举类ConfigurationPropertyKey
     *
     * @return
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        // 相关配置可参照配置枚举类ConfigurationPropertyKey
        properties.setProperty(ConfigurationPropertyKey.SQL_SHOW.getKey(), String.valueOf(Boolean.TRUE));
        return properties;
    }
}
