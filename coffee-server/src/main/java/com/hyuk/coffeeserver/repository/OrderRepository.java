package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order insert(Order order);

    List<Order> findOrdersOrderByCreatedAt();

    List<Order> findOrdersOrderByCreatedAt(OrderStatus orderStatus);

    Optional<Order> findOrderWithOrderItems(UUID orderId);

    void updateOrderStatusByOrderId(UUID orderId);

    void deleteOrderAndOrderItems(UUID orderId);
}
