package com.hyuk.coffeeserver.controller;

import com.hyuk.coffeeserver.dto.OrderDto;
import com.hyuk.coffeeserver.entity.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoConverter {

    private OrderDtoConverter() {
        throw new AssertionError("util class");
    }

    public static List<OrderDto> toOrderDtoList(List<Order> orders) {
        return orders.stream()
            .map(order -> toOrderDto(order))
            .collect(Collectors.toList());
    }

    private static OrderDto toOrderDto(Order order) {
        return new OrderDto(
            order.getOrderId(),
            order.getNickName().getNickName(),
            order.getOrderStatus().toString(),
            order.getCreatedAt()
        );
    }
}
