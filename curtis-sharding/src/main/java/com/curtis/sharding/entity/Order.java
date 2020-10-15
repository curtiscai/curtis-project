package com.curtis.sharding.entity;

/**
 * @author curtis
 * @desc
 * @date 2020-09-04
 * @email 397773935@qq.com
 * @reference
 */
public class Order {

    private Long orderId;

    private Integer userId;

    private Long addressId;

    public Order() {
    }

    public Order(Long orderId, Integer userId, Long addressId) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", addressId=" + addressId +
                '}';
    }
}
