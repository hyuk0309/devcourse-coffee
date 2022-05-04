package com.hyuk.coffeeserver.service;

import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import java.util.List;

public interface OrderService {

    Order createOrder(NickName nickName, List<OrderItem> items);
}
