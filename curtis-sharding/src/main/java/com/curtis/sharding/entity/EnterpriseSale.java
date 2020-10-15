package com.curtis.sharding.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author curtis
 * @desc
 * @date 2020-09-10
 * @email 397773935@qq.com
 * @reference
 */
public class EnterpriseSale {

    private Long id;

    private Long enterpriseId;

    private String cityCode;

    private Date saleDay;

    private BigDecimal saleValue;

    public EnterpriseSale() {
    }

    public EnterpriseSale(Long id, Long enterpriseId, String cityCode, Date saleDay, BigDecimal saleValue) {
        this.id = id;
        this.enterpriseId = enterpriseId;
        this.cityCode = cityCode;
        this.saleDay = saleDay;
        this.saleValue = saleValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Date getSaleDay() {
        return saleDay;
    }

    public void setSaleDay(Date saleDay) {
        this.saleDay = saleDay;
    }

    public BigDecimal getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(BigDecimal saleValue) {
        this.saleValue = saleValue;
    }

    @Override
    public String toString() {
        return "EnterpriseSale{" +
                "id=" + id +
                ", cityCode='" + cityCode + '\'' +
                ", enterpriseId=" + enterpriseId +
                ", saleDay=" + saleDay +
                ", saleValue=" + saleValue +
                '}';
    }
}
