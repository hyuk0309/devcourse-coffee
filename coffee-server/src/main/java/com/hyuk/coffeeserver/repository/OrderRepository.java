package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Order;

public interface OrderRepository {

    Order insert(Order order);
}
