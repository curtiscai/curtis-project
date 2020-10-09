package com.curtis.sharding.raw.config.inline;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
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
 * @desc 演示行表达式分片策略
 * 对应InlineShardingStrategy。使用Groovy的表达式，提供对SQL语句中的 = 和 IN 的分片操作支持，只支持单分片键。
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
@Profile("dev-inline")
@Configuration
public class ShardingSphereConfigWithInlineShardingStrategy {

    @Bean(value = "testDataSource")
    public DataSource getDataSource() throws SQLException {
        // 1.数据源集合配置（该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建）
        Map<String, DataSource> dataSourceMap = this.createDataSourceMap();

        // 2. 分片规则配置，包含各逻辑表的分片策略配置
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        // 2.1 order表分片规则配置
        TableRuleConfiguration orderTableRuleConfiguration = this.getOrderTableRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(orderTableRuleConfiguration);

        // 3. ShardingSphere数据源属性配置（非必须）
        Properties properties = this.getProperties();
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, properties);
    }

    /**
     * 表分片规则配置，以用户id尾数取模确定分库，以订单id尾数取模确定分表
     *
     * @return
     */
    private TableRuleConfiguration getOrderTableRuleConfiguration() {
        // 创建表分片规则配置对象，声明逻辑表和实际数据节点(逻辑表只有表名，实际数据节点由数据库和表名组成)
        // 实际数据节点的数据库名需要使用dataSourceMap中的key，也就是数据库（准确是数据源）的别名，数据库分片配置也是如此
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("tb_order",
                "ds_${0..1}.tb_order_${0..3}");
        // 数据库分片策略配置：用户id尾数取模。shardingColumn是分片列名，algorithmExpression是分片算法表达式。
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id", "ds_${user_id % 2}"));
        // 数据表分片策略配置：订单id尾数取模。shardingColumn是分片列名，algorithmExpression是分片算法表达式。
        tableRuleConfiguration.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("order_id", "tb_order_${order_id % 4}"));
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
        dataSourceMap.put("ds_0", dataSource0);
        // 配置第 2 个数据源
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://localhost:3306/db_test_1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");
        dataSourceMap.put("ds_1", dataSource1);
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