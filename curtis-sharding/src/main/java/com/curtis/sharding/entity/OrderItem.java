package com.curtis.sharding.entity;

/**
 * @author curtis
 * @desc
 * @date 2020-09-04
 * @email 397773935@qq.com
 * @reference
 */
public class OrderItem {

    private Long orderItemId;

    private Long orderId;

    private Integer userId;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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
}
