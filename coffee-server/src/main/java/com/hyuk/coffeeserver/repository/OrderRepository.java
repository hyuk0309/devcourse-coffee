package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order insert(Order order);

    List<Order> findOrdersOrderByCreatedAt();

    Optional<Order> findOrderWithOrderItems(UUID orderId);
}
