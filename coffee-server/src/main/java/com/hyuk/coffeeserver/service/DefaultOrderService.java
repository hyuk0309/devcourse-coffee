package com.hyuk.coffeeserver.service;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_ORDER_ID_EXP_MSG;

import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.exception.ServiceException;
import com.hyuk.coffeeserver.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
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

    @Override
    public List<Order> searchOrdersOrderByCreatedAt() {
        return orderRepository.findOrdersOrderByCreatedAt();
    }

    //to do
    @Override
    public List<Order> searchOrdersOrderByCreatedAt(OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order searchOrderWithOrderItems(UUID orderId) {
        return orderRepository.findOrderWithOrderItems(orderId)
            .orElseThrow(() -> {
                throw new ServiceException(INVALID_ORDER_ID_EXP_MSG.toString());
            });
    }

    @Override
    @Transactional
    public void changeOrderStatus(UUID orderId) {
        orderRepository.updateOrderStatusByOrderId(orderId);
    }

    @Override
    @Transactional
    public void removeOrder(UUID orderId) {
        orderRepository.deleteOrderAndOrderItems(orderId);
    }
}
