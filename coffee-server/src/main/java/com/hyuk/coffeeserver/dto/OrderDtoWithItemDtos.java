package com.hyuk.coffeeserver.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDtoWithItemDtos {

    private UUID orderId;
    private String nickName;
    private List<OrderItemDto> orderItemDtos;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDtoWithItemDtos(UUID orderId, String nickName,
        List<OrderItemDto> orderItemDtos, String orderStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.nickName = nickName;
        this.orderItemDtos = orderItemDtos;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getNickName() {
        return nickName;
    }

    public List<OrderItemDto> getOrderItemDtos() {
        return orderItemDtos;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
