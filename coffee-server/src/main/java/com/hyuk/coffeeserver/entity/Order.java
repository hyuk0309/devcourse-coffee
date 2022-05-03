package com.hyuk.coffeeserver.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {

    private final UUID orderId;
    private final NickName nickName;
    private final List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, NickName nickName, List<OrderItem> orderItems,
        OrderStatus orderStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.nickName = nickName;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public NickName getNickName() {
        return nickName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
