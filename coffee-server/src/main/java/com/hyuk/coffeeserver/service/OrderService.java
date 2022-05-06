package com.hyuk.coffeeserver.service;

import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(NickName nickName, List<OrderItem> items);

    List<Order> searchOrdersOrderByCreatedAt();

    Order searchOrderWithOrderItems(UUID orderId);

    void changeOrderStatus(UUID orderId);

    void removeOrder(UUID orderId);
}
