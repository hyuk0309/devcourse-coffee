package com.hyuk.coffeeserver.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDto {

    private UUID orderId;
    private String nickName;
    private String orderStatus;
    private LocalDateTime createdAt;

    public OrderDto(UUID orderId, String nickName,
        String orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.nickName = nickName;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
