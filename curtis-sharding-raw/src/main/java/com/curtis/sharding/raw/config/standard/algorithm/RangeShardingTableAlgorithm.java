package com.curtis.sharding.raw.config.standard.algorithm;

import com.google.common.collect.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author curtis
 * @desc 范围分片算法实现
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class RangeShardingTableAlgorithm implements RangeShardingAlgorithm<Long> {

    private static final Integer MODULO = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger(RangeShardingTableAlgorithm.class);

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        LOGGER.info("**************** enter the table RangeShardingTableAlgorithm," +
                "availableDataSourceNames -> {}", availableTargetNames);
        Set<String> result = new LinkedHashSet<>();
        // Range.closed(1L, 400000000000000000L).encloses(shardingValue.getValueRange())
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
            else {
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
        return result;
    }
}
