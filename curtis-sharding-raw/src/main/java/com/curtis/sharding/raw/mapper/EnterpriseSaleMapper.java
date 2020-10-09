package com.curtis.sharding.raw.mapper;

import com.curtis.sharding.raw.entity.EnterpriseSale;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author curtis
 * @desc
 * @date 2020-09-10
 * @email 397773935@qq.com
 * @reference
 */
public interface EnterpriseSaleMapper {

    /**
     * 插入企业销售信息
     *
     * @param enterpriseSale
     * @return
     */
    int insertEnterpriseSale(EnterpriseSale enterpriseSale);


    /**
     * 获取指定企业指定城市销售信息
     *
     * @param enterpriseId
     * @param cityCode
     * @return
     */
    List<EnterpriseSale> getEnterpriseSaleByCityCode(@Param("enterpriseId") Long enterpriseId, @Param("cityCode") String cityCode);


    /**
     * 获取指定企业指定日期销售信息
     *
     * @param enterpriseId
     * @param saleDay
     * @return
     */
    List<EnterpriseSale> getEnterpriseSaleBySaleDay(@Param("enterpriseId") Long enterpriseId, @Param("saleDay") Date saleDay);

    /**
     * 获取指定企业指定城市和日期的销售信息
     *
     * @param enterpriseId
     * @param cityCodes
     * @param saleDay
     * @return
     */
    List<EnterpriseSale> getEnterpriseSaleByCityCodeAndSaleDay(@Param("enterpriseId") Long enterpriseId,
                                                               @Param("cityCodes") List<String> cityCodes, @Param("saleDay") Date saleDay);


    /**
     * 获取指定企业指定城市指定城市范围的销售数据
     *
     * @param enterpriseId
     * @param cityCodes
     * @param minSaleDay
     * @param maxSaleDay
     * @return
     */
    List<EnterpriseSale> getEnterpriseSaleByCityCodesAndBetweenSaleDay(@Param("enterpriseId") Long enterpriseId, @Param("cityCodes") List<String> cityCodes,
                                                         @Param("minSaleDay") Date minSaleDay, @Param("maxSaleDay") Date maxSaleDay);

}
