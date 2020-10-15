package com.curtis.sharding.config.standard.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author curtis
 * @desc 数据表精确分片算法实现，订单id尾数取模
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class PreciseShardingTableAlgorithmWithModulo implements PreciseShardingAlgorithm<Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreciseShardingTableAlgorithmWithModulo.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        LOGGER.info("**************** enter the table PreciseShardingAlgorithm, " +
                "availableTableNames -> {}", availableTargetNames);
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(shardingValue.getValue() % 4 + "")) {
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }
}
