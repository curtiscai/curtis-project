package com.curtis.sharding.config.complex.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

/**
 * @author curtis
 * @desc 复合分片算法实现
 * @date 2020-09-10
 * @email 397773935@qq.com
 * @reference
 */
public class ComplexShardingTableAlgorithm implements ComplexKeysShardingAlgorithm<String> {

    private static final Integer START_YEAR_IN_DB = Integer.parseInt("2019");

    /**
     * 对于复合分片算法来说要根据实际业务来实现分片算法，没必要针对每个分片列都进行精确和范围算法实现
     * 根据业务场景分析，对于分片键城市code只可能是等值查询，而分片键交易日期则可能是等值查询也可能是范围查询。
     * 需要注意某个分片键不是等值则分片List是null而不是empty，同理分片键不是范围则分片Range是null而不是empty，
     * ShardingSphere这个设计真不是一个好设计...
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<String> shardingValue) {
        Set<String> result = new LinkedHashSet<>();
        // 处理精确匹配（这里使用字符串来获取分片键的值，这是因为多个分片键的类型不一致所以只能这么处理）
        // 注意如果分片值不是等值条件或者缺少某个分片值，则List是null
        Map<String, Collection<String>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        Collection<String> saleDayList = columnNameAndShardingValuesMap.get("sale_day");
        Collection<String> cityCodeList = columnNameAndShardingValuesMap.get("city_code");

        // 处理范围匹配
        Map<String, Range<String>> columnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        // 注意如果分片键是等值查询而不是范围查询则Range不是empty而是null
        Range<String> saleDayRange = columnNameAndRangeValuesMap.get("sale_day");
        // 根据业务实际情况来看，city_code不可能使用范围值，只可能是等值=或者in的查询，所以没必要考虑
        Range<String> cityCodeRange = columnNameAndRangeValuesMap.get("city_code");

        // 根据业务实际情况来看，city_code不可能使用范围值，只可能是等值=或者in的查询，而交易日期可能是等值也可能是范围
        // 1 如果城市为空则查询所有城市，具体就看交易日期的情况
        if (cityCodeList == null || cityCodeList.isEmpty()) {
            // 这里模拟查询所有城市的code，或者有业务数据的城市的code集合
            List<String> cityCodeAllList = Lists.newArrayList("110100", "130100");
            // 1.1 城市为空，交易日期为空则返回所有城市所有日期的表
            if (saleDayList == null && saleDayRange == null) {
                // 条件中如果没有城市也没有日期则返回所有城市所有日期，
                // 而根据实际情况，日期肯定是从一个已知的开始时间（开展业务开始时间或者系统上线时间）到当年时间
                for (String cityCode : cityCodeAllList) {
                    for (int year = START_YEAR_IN_DB; year <= Year.now().getValue(); year++) {
                        result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                    }
                }
                return result;
            }
            // 1.2 城市为空，交易日期不为空，则需要考虑等值和范围情况。
            else {
                // 理论上日期不会出现等值和范围同时出现的情况，这里我们严谨一点，都处理一下
                // 1.2.1 城市为空，交易日期等值情况。
                if (saleDayList != null) {
                    for (String cityCode : cityCodeAllList) {
                        for (Object saleDay : saleDayList) {
                            Timestamp timestamp = (Timestamp) saleDay;
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            int year = localDateTime.getYear();
                            result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                        }
                    }
                }
                // 1.2.2 城市为空，交易日期范围情况。
                if (saleDayRange != null) {
                    // 有最小最大边界
                    if (saleDayRange.hasLowerBound() && saleDayRange.hasUpperBound()) {
                        Object minSaleDay = saleDayRange.lowerEndpoint();
                        Object maxSaleDay = saleDayRange.upperEndpoint();
                        int minSaleYear = Math.max(START_YEAR_IN_DB, ((Timestamp) minSaleDay).toLocalDateTime().getYear());
                        int maxSaleYear = Math.min(Year.now().getValue(), ((Timestamp) maxSaleDay).toLocalDateTime().getYear());
                        for (String cityCode : cityCodeAllList) {
                            for (int year = minSaleYear; year <= maxSaleYear; year++) {
                                result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                            }
                        }
                    }
                    // 只有最小边界
                    else if (saleDayRange.hasLowerBound()) {
                        // TODO 待实现
                    }
                    // 只有最大边界
                    else if (saleDayRange.hasUpperBound()) {
                        // TODO 待实现
                    }
                }
                return result;
            }
        }

        // 2 如果城市不为空则同时处理城市和日期情况
        if (cityCodeList != null) {
            for (String cityCode : cityCodeList) {
                // 2.1 城市不为空，日期为空则返回指定城市的所有日期的表
                // 这里需要注意如果范围是空则saleDayRange是null而不是empty
                if (saleDayList == null && saleDayRange == null) {
                    for (int year = START_YEAR_IN_DB; year <= Year.now().getValue(); year++) {
                        result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                    }
                }
                // 2.2 城市不为空，日期不为空则处理等值和范围情况
                else {
                    // 2.2.1 城市不为空，日期不为空，日期是等值则返回指定城市指定日期的表
                    if (saleDayList != null) {
                        // 如果使用String来遍历会报错，why？why？这可是Collection<String>，理解不了
                        for (Object saleDay : saleDayList) {
                            Timestamp timestamp = (Timestamp) saleDay;
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            int year = localDateTime.getYear();
//                        // tb_enterprise_sale_110100_2020
                            result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                        }
                    }
                    // 2.2.2 城市不为空，日期不为空，日期是范围则返回指定城市指定范围日期的表
                    if (saleDayRange != null) {
                        // 有最小最大边界
                        if (saleDayRange.hasLowerBound() && saleDayRange.hasUpperBound()) {
                            Object minSaleDay = saleDayRange.lowerEndpoint();
                            Object maxSaleDay = saleDayRange.upperEndpoint();
                            int minSaleYear = Math.max(START_YEAR_IN_DB, ((Timestamp) minSaleDay).toLocalDateTime().getYear());
                            int maxSaleYear = Math.min(Year.now().getValue(), ((Timestamp) maxSaleDay).toLocalDateTime().getYear());
                            for (int year = minSaleYear; year <= maxSaleYear; year++) {
                                result.add(String.format("tb_enterprise_sale_%s_%s", cityCode, year));
                            }
                        }
                        // 只有最小边界
                        else if (saleDayRange.hasLowerBound()) {
                            // TODO 待实现
                        }
                        // 只有最大边界
                        else if (saleDayRange.hasUpperBound()) {
                            // TODO 待实现
                        }
                    }
                }
            }
        }
        return result;
    }
}
