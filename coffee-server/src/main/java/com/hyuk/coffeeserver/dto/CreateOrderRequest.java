package com.hyuk.coffeeserver.dto;

import com.hyuk.coffeeserver.entity.OrderItem;
import java.util.List;

public class CreateOrderRequest {

    private final String nickName;
    private final List<OrderItem> orderItems;

    public CreateOrderRequest(String nickName, List<OrderItem> orderItems) {
        this.nickName = nickName;
        this.orderItems = orderItems;
    }

    public String getNickName() {
        return nickName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
