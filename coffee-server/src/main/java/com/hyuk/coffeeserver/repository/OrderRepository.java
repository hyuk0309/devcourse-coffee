package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Order;
import java.util.List;

public interface OrderRepository {

    Order insert(Order order);

    List<Order> findOrdersOrderByCreatedAt();
}
