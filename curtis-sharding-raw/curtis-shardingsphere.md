# 1 数据切分（Data Sharding）概述

部分参考：《Mycat权威指南-Mycat Definitive Guide》、《Sharding Shpere官方文档》

## 1.1 数据切分/分片概念

​		数据切分/分片指的是通过某种特定条件将同一个数据库/表中的数据分散到多个数据库（服务器）或者多张表中，以达到分散单台设备负载或者加快数据查询效率的目的。

## 1.2 数据切分/分片分类

​		数据切分按照切分规则分为两种切分模式：垂直（纵向）切分、水平（横向）切分。

  * 垂直（纵向）切分

    ​	垂直切分指的是同一个库中的多个数据表按照业务进行分组然后切分到多个数据库中（垂直分库），或者将同一个数据表中的多个字段进行分组然后切分到多个数据表中（垂直分表）。

  * 水平（横向）切分

     水平切分指的是将同一个表中的数据按照某种规则切分到多个数据库（水平分库）或者多个数据表中（水平分表）。每个分片仅包含数据的一部分。

## 1.3 数据切分/分片方案对比

​		垂直分库将数据表按照业务进行分组切分到多个数据库中，提高了单机数据库的CPU、内存、网络IO、磁盘等问题，但是当数据无法再根据业务进行切分时还是会存在单机瓶颈，而且也未能解决单一数据表数据量多大导致的查询效率问题。垂直分表一定程度上解决了第二个问题，但是垂直分表通常是切分大字段，或者多个字段在业务上可以再次分组才切分，一旦不满足这些条件通常是不会再进行垂直分表。

​		水平分片（水平分库+水平分表）从根本上解决了单机数据库CPU、内存、网络IO、磁盘等问题，也解决了单一数据表过大导致的查询效率问题，但是要进行额外的数据路由，有时还需要额外的归并操作，提高了复杂度并且不合理的分片还会导致分布式事务。

# 2 Sharding-Shpere相关概念

## 2.1 逻辑表

​		逻辑表是指数据切分后具有相同结构的表的总称。比如：订单数据根据主键尾数拆分为 10 张表，分别是 t_order_0 到 t_order_9，他们的逻辑表名为 t_order。

## 2.2 真实表

​		真实表是指数据切分后真实存在的表。比如：订单数据根据主键尾数拆分为 10 张表，分别是 t_order_0 到 t_order_9，他们就是逻辑表对应的真实表。

## 2.3 绑定表

​		指分片规则一致的主表和子表。例如：t_order 表和 t_order_item 表，均按照 order_id 分片，则此两张表互为绑定表关系。绑定表之间的多表关联查询不会出现笛卡尔积关联，关联查询效率将大大提升。

​		ShardingSphere 将会以From子句最左侧的表作为整个绑定表的主表。 所有路由计算将会只使用主表的策略，那么 t_order_item 表的分片计算将会使用 t_order 的条件。故绑定表之间的分区键要完全相同。

## 2.4 数据节点

​		数据节点是数据分片的最小单元。由数据库和数据表组成，例：ds_0.t_order_0。

## 2.5 广播表

​		指所有的分片数据源中都存在的表，表结构和表中的数据在每个数据库中均完全一致。适用于数据量不大且需要与海量数据的表进行关联查询的场景，例如：字典表。

# 3 Sharding-Shpere分片相关概念

## 3.1 分片键

​		分片键是指用于分片的字段，是将数据库或表进行水平拆分的依据。（例：将订单表中的订单主键的尾数取模分片，则订单主键为分片字段） **执行的SQL 如果无分片字段，将执行全路由，性能较差。 **除了对单分片字段的支持，Apache ShardingSphere 也支持根据多个字段进行分片。

## 3.2 分片算法

​		我们是使用分片键通过特定分片算法来对数据进行分片，ShardingShpere也是使用指定分片算法来将逻辑表路由到实际的真实表。ShardingShpere支持通过 =、>=、<=、>、<、BETWEEN 和 IN 分片。 分片算法需要应用方开发者自行实现，可实现的灵活度非常高。

​		Sharding-Sphere目前提供4种分片算法。 由于分片算法和业务实现紧密相关，因此并未提供分片算法的具体实现，而是通过分片策略将各种场景提炼出来，提供更高层级的抽象，并提供接口让应用开发者自行实现分片算法。

### 3.2.1 精确分片算法
对应 PreciseShardingAlgorithm，用于处理使用单一键作为分片键的 = 与 IN 进行分片的场景。**需要配合 StandardShardingStrategy 使用**。

### 3.2.2 范围分片算法
对应 RangeShardingAlgorithm，用于处理使用单一键作为分片键的 BETWEEN AND、>、<、>=、<=进行分片的场景。**需要配合 StandardShardingStrategy 使用**。

### 3.2.3 复合分片算法
对应 ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，包含多个分片键的逻辑较复杂，需要应用开发者自行处理其中的复杂度。**需要配合 ComplexShardingStrategy 使用**。

### 3.2.4 Hint分片算法
对应 HintShardingAlgorithm，用于处理使用 Hint 行分片的场景。**需要配合 HintShardingStrategy 使用**。

## 3.3 分片策略

​		分片策略包含分片键和分片算法，由于分片算法的独立性，将其独立抽离。真正可用于分片操作的是分片键 + 分片算法，也就是分片策略。目前提供 5 种分片策略。

### 3.3.1 行表达式分片策略

​	对应 InlineShardingStrategy。使用 Groovy 的表达式，提供对 SQL 语句中的 = 和 IN 的分片操作支持，只支持单分片键。 对于简单的分片算法，可以通过简单的配置使用，从而避免繁琐的 Java 代码开发，如: t_user_$->{u_id % 8} 表示 t_user 表根据 u_id 模 8，而分成 8 张表，表名称为 t_user_0 到 t_user_7。 

### 3.3.2 标准分片策略

​	对应 StandardShardingStrategy。提供对 SQL语句中的 =, >, <, >=, <=, IN 和 BETWEEN AND 的分片操作支持。 StandardShardingStrategy 只支持单分片键，提供 PreciseShardingAlgorithm 和 RangeShardingAlgorithm 两个分片算法。 PreciseShardingAlgorithm 是必选的，用于处理 = 和 IN 的分片。 RangeShardingAlgorithm 是可选的，用于处理 BETWEEN AND, >, <, >=, <=分片，如果不配置 RangeShardingAlgorithm，SQL 中的 BETWEEN AND 将按照全库路由处理。

### 3.3.3 复合分片策略

​	对应 ComplexShardingStrategy。复合分片策略。提供对 SQL 语句中的 =, >, <, >=, <=, IN 和 BETWEEN AND 的分片操作支持。 ComplexShardingStrategy 支持多分片键，由于多分片键之间的关系复杂，因此并未进行过多的封装，而是直接将分片键值组合以及分片操作符透传至分片算法，完全由应用开发者实现，提供最大的灵活度。

### 3.3.4 Hint分片策略

​	对应 HintShardingStrategy。通过 Hint 指定分片值而非从 SQL 中提取分片值的方式进行分片的策略。

### 3.3.5 不分片策略

​	对应 NoneShardingStrategy。不分片的策略。

附：行表达式分片策略都可以通过标准分片策略来实现。只有使用方式的不同。

## 3.3 分片策略和分片算法的关系

分片算法描述数据按照分片键如何分片，

 * 精确分片算法（PreciseShardingAlgorithm）用于对单一分片键进行等值分片（=和In）的情景。
 * 范围分片算法（RangeShardingAlgorithm）用于对单一分片键进行范围分片（BETWEEN AND、>、<、>=、<=）的情景。
 * 符合分片算法（ComplexKeysShardingAlgorithm）用于对多分片键进行分片的情景。可以根据情况实现等值以及范围分片算法的情景。

分片策略包含分片键和分片算法，是实际应用在分片规则配置中的策略，需要配合分片键和分片算法。

* 行表达式分片策略（InlineShardingStrategy）使用Groovy 的表达式，仅用于对单一分片键进行等值分片（=和In）的情景。不支持范围分片。不使用以上任何分片算法。
* 标准分片策略（StandardShardingStrategy）使用PreciseShardingAlgorithm 和 RangeShardingAlgorithm 两个分片算法。其中PreciseShardingAlgorithm 是必选的，用于处理 `=` 和 `IN` 的分片。 RangeShardingAlgorithm 是可选的，用于处理 `BETWEEN AND`, `>`, `<`, `>=`, `<=`分片。
* 复合分片策略（ComplexShardingStrategy）使用ComplexKeysShardingAlgorithm算法，用于处理等值以及范围分片。

# 4 Sharding-Sphere的分片配置-使用Java API

## 4.1 引入Maven依赖

截止到2020.9，Sharding-Sphere的最新发布版本是4.1.1

```xml
<dependency>
	<groupId>org.apache.shardingsphere</groupId>
	<artifactId>sharding-jdbc-core</artifactId>
	<version>4.1.1</version>
</dependency>
```

## 4.2 创建数据源DataSource

​	Sharding-Shpere通过 ShardingSphereDataSourceFactory 工厂的createDataSource方法来获取实现标准DataSource接口的ShardingSphereDataSource对象。通过实现标准javax.sql.DataSource接口的对象来接入系统。之后就可以向原来一样使用原生JDBC接口或者使用JPA、MyBatis等ORM框架。这个就是Sharding-Sphere接入项目的核心所在。

```java
public final class ShardingDataSourceFactory {
    
    /**
     * Create sharding data source.
     *
     * @param dataSourceMap data source map
     * @param shardingRuleConfig rule configuration for databases and tables sharding
     * @param props properties for data source
     * @return sharding data source
     * @throws SQLException SQL exception
     */
    public static DataSource createDataSource(
            final Map<String, DataSource> dataSourceMap, final ShardingRuleConfiguration shardingRuleConfig, final Properties props) throws SQLException {
        return new ShardingDataSource(dataSourceMap, new ShardingRule(shardingRuleConfig, dataSourceMap.keySet()), props);
    }
}
```

通过查看ShardingDataSourceFactory的createDataSource源码，我们可以看到要完成Sharding-Shpere分片配置需要数据源Map（dataSourceMap）、分片规则配置（ShardingRuleConfiguration）以及属性配置（Properties）。

### 4.2.1 创建数据源集合（Map<String, DataSource>）

​	数据源集合包括所有分库的数据源，如果未分库则数据源集合中只包含一个数据源，该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建。

​	我们以两个分库db_test_0和db_test_1为例来创建数据源集合（这里为了简化演示，没有把配置提出来也没有对公共代码重构）

```java
		/**
     * 创建数据源集合（该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建）
     * @return 数据源集合
     */
    private static Map<String, DataSource> createDataSourceMap() {
        // 配置真实数据源
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
```

### 4.2.2 创建数据源属性配置

```java
		/**
     * ShardingSphere分片属性配置（非必须）
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
```

### 4.2.3 创建分片规则配置（ShardingRuleConfiguration）

ShardingRuleConfiguration类包含若干配置，源码如下：

```java
public final class ShardingRuleConfiguration implements RuleConfiguration {
    // 数据表分片配置
    private Collection<TableRuleConfiguration> tableRuleConfigs = new LinkedList<>();
    
    private Collection<String> bindingTableGroups = new LinkedList<>();
    
    private Collection<String> broadcastTables = new LinkedList<>();
    
    private String defaultDataSourceName;
    
    private ShardingStrategyConfiguration defaultDatabaseShardingStrategyConfig;
    
    private ShardingStrategyConfiguration defaultTableShardingStrategyConfig;
    
    private KeyGeneratorConfiguration defaultKeyGeneratorConfig;
    
    private Collection<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs = new LinkedList<>();
    
    private EncryptRuleConfiguration encryptRuleConfig;
}
```

我们要做的就是在分片规则配置对象ShardingRuleConfiguration的tableRuleConfigs中添加表分片规则配置对象TableRuleConfiguration。TableRuleConfiguration源码如下：

```java
public final class TableRuleConfiguration {
    // 逻辑表表名
    private final String logicTable;
    // 真实数据节点
    private final String actualDataNodes;
    // 数据库分片策略配置
    private ShardingStrategyConfiguration databaseShardingStrategyConfig;
    // 数据表分片策略配置
    private ShardingStrategyConfiguration tableShardingStrategyConfig;
    // 自增列生成器配置
    private KeyGeneratorConfiguration keyGeneratorConfig;
    
    public TableRuleConfiguration(final String logicTable) {
        this(logicTable, null);
    }
    
    public TableRuleConfiguration(final String logicTable, final String actualDataNodes) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(logicTable), "LogicTable is required.");
        this.logicTable = logicTable;
        this.actualDataNodes = actualDataNodes;
    }
}
```

#### 4.2.3.1 使用行表达式分片策略创建分片规则

​	行表达式分片策略（InlineShardingStrategy）使用Groovy 的表达式，仅用于对单一分片键进行等值分片（=和In）的情景。不支持范围分片。不使用以上任何分片算法。

```java
		@Bean(value = "userDataSource")
    public DataSource getDataSource() throws SQLException {
        // 1.数据源集合配置（该数据源可以使用任何连接池（c3p0、druid、Hikari等）来创建）
        Map<String, DataSource> dataSourceMap = this.createDataSourceMap();

        // 2. 分片规则配置，包含各逻辑表的分片策略配置
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        // order表分片规则配置
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
        // 创建表分片规则配置对象，声明逻辑表和实际数据节点
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("tb_order",
                "db_test_${0..1}.tb_order_${0..3}");
        // 数据库分片策略配置：用户id尾数取模
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id", "db_test_${user_id % 2}"));
        // 数据表分片策略配置：订单id尾数取模
        tableRuleConfiguration.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("order_id", "tb_order_${order_id % 4}"));
        return tableRuleConfiguration;
    }
```

#### 4.2.3.2 使用标准分片策略创建分片规则

标准分片策略（StandardShardingStrategy）使用PreciseShardingAlgorithm 和 RangeShardingAlgorithm 两个分片算法。其中PreciseShardingAlgorithm 是必选的，用于处理 `=` 和 `IN` 的分片。 RangeShardingAlgorithm 是可选的，用于处理 `BETWEEN AND`, `>`, `<`, `>=`, `<=`分片。

我们分别看下PreciseShardingAlgorithm和RangeShardingAlgorithm源码

```java
/**
 * Precise sharding algorithm.
 * 
 * @param <T> class type of sharding value
 */
public interface PreciseShardingAlgorithm<T extends Comparable<?>> extends ShardingAlgorithm {
    
    /**
     * Sharding.
     * 
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue sharding value
     * @return sharding result for data source or table's name
     */
    String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<T> shardingValue);
}
```

```java
/**
 * Range sharding algorithm.
 * 
 * @param <T> class type of sharding value
 */
public interface RangeShardingAlgorithm<T extends Comparable<?>> extends ShardingAlgorithm {
    
    /**
     * Sharding.
     * 
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue sharding value
     * @return sharding results for data sources or tables's names
     */
    Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<T> shardingValue);
}
```

其中availableTargetNames是所有可用物理节点，对应创建表分片规则配置对象TableRuleConfiguration时声明的实际物理数据节点，其中T为分片值的类型，从这里我们也看出无论是精确分片算法还是范围分片算法，都仅支持单分片值。

看完源码我们再看下标准分片策略配置代码：

```java
/**
 * @author curtis
 * @desc 数据库精确分片算法实现，用户id尾数取模
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class PreciseShardingDatabaseAlgorithmWithModulo implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        for (String databaseName : availableTargetNames) {
            if (databaseName.endsWith(shardingValue.getValue() % 2 + "")) {
                return databaseName;
            }
        }
        throw new UnsupportedOperationException();
    }
}

/**
 * @author curtis
 * @desc 数据表精确分片算法实现，订单id尾数取模
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class PreciseShardingTableAlgorithmWithModulo implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(shardingValue.getValue() % 4 + "")) {
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }
}

/**
 * @author curtis
 * @desc 范围分片算法实现
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class RangeShardingTableAlgorithm implements RangeShardingAlgorithm<Long> {

    private static final Integer MODULO = 4;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        Set<String> result = new LinkedHashSet<>();
        if (Range.closed(1L, 400000000000000000L).encloses(shardingValue.getValueRange())) {
            Range<Long> valueRange = shardingValue.getValueRange();
            if (valueRange.hasLowerBound() && valueRange.hasUpperBound()) {
                Long lowerEndpoint = valueRange.lowerEndpoint();
                Long upperEndPoint = valueRange.upperEndpoint();
                // 如果边界最大值和最小值跨过一个模，说明跨过一个周期则应该遍历所有表
                if (upperEndPoint - lowerEndpoint >= MODULO) {
                    return availableTargetNames;
                }
                // 取模来判断
                long lowerEndpointModulo = lowerEndpoint % MODULO;
                long upperEndPointModulo = upperEndPoint % MODULO;
                // 一个完整取模周期内
                if (upperEndPointModulo >= lowerEndpointModulo) {
                    for (String tableName : availableTargetNames) {
                        String endChar = StringUtils.substring(tableName, tableName.length() - 1);
                        long endCharLong = Long.parseLong(endChar);
                        if (endCharLong >= lowerEndpointModulo && endCharLong <= upperEndPointModulo) {
                            result.add(tableName);
                        }
                    }
                }
                // 两个取模周期内，跨过了周期最大值
                else{
                    for (String tableName : availableTargetNames) {
                        String endChar = StringUtils.substring(tableName, tableName.length() - 1);
                        long endCharLong = Long.parseLong(endChar);
                        if (endCharLong >= lowerEndpointModulo || endCharLong <= upperEndPointModulo) {
                            result.add(tableName);
                        }
                    }
                }
            }
            // 如果只有最小边界或者最大边界，由于循环取模所有要遍历所有分表
            else {
                return availableTargetNames;
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return result;
    }
}

 		/**
     * 表分片规则配置，以用户id尾数取模确定分库，以订单id尾数取模确定分表
     *
     * @return
     */
    private TableRuleConfiguration getOrderTableRuleConfiguration() {
        // 创建表分片规则配置对象，声明逻辑表和实际数据节点
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("tb_order",
                "db_test_${0..1}.tb_order_${0..3}");
        // 数据库分片策略配置：用户id尾数取模
        StandardShardingStrategyConfiguration standardShardingDatabaseStrategyConfiguration
                = new StandardShardingStrategyConfiguration("user_id", new PreciseShardingDatabaseAlgorithmWithModulo());
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(standardShardingDatabaseStrategyConfiguration);

        // 数据表分片策略配置：订单id尾数取模，这里实现精确以及范围分片算法
        StandardShardingStrategyConfiguration standardShardingTableStrategyConfiguration
                = new StandardShardingStrategyConfiguration("order_id", new PreciseShardingTableAlgorithmWithModulo(), new RangeShardingTableAlgorithm());

        // 不配置RangeShardingAlgorithm然后使用范围查询提示：### Cause: java.lang.UnsupportedOperationException: Cannot find range sharding strategy in sharding rule.
        tableRuleConfiguration.setTableShardingStrategyConfig(standardShardingTableStrategyConfiguration);

        tableRuleConfiguration.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id", getProperties()));
        return tableRuleConfiguration;
    }
```

#### 4.2.3.3 使用复合分片策略创建分片规则







# Sharding-Sphere分库分表实例



