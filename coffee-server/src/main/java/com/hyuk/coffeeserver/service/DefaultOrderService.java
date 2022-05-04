package com.hyuk.coffeeserver.service;

import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(NickName nickName, List<OrderItem> items) {
        var order = new Order(
            UUID.randomUUID(),
            nickName,
            items,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());
        return orderRepository.insert(order);
    }
}
