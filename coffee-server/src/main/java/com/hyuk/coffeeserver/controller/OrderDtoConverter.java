package com.hyuk.coffeeserver.controller;

import com.hyuk.coffeeserver.dto.OrderDto;
import com.hyuk.coffeeserver.dto.OrderDtoWithItemDtos;
import com.hyuk.coffeeserver.dto.OrderItemDto;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
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

    //to do
    public static OrderDtoWithItemDtos toOrderDtoWithItem(Order order) {
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
            .map(OrderDtoConverter::toOrderItemDto)
            .collect(Collectors.toList());
        return new OrderDtoWithItemDtos(
            order.getOrderId(),
            order.getNickName().getNickName(),
            orderItemDtos,
            order.getOrderStatus().toString(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }

    private static OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
            orderItem.getCoffeeId(),
            orderItem.getCategory().toString(),
            orderItem.getPrice(),
            orderItem.getQuantity()
        );
    }
}
