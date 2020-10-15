package com.curtis.sharding.config.complex.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author curtis
 * @desc 数据库精确分片算法实现，企业id尾数取模
 * @date 2020-09-07
 * @email 397773935@qq.com
 * @reference
 */
public class PreciseShardingDatabaseAlgorithmWithModulo implements PreciseShardingAlgorithm<Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreciseShardingDatabaseAlgorithmWithModulo.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        LOGGER.info("**************** enter the database PreciseShardingAlgorithm, " +
                "availableDataSourceNames -> {}", availableTargetNames);
        for (String databaseName : availableTargetNames) {
            if (databaseName.endsWith(shardingValue.getValue() % 2 + "")) {
                return databaseName;
            }
        }
        throw new UnsupportedOperationException();
    }
}
