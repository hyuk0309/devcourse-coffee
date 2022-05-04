package com.hyuk.coffeeserver.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultOrderServiceTest {

    @InjectMocks
    DefaultOrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Test
    @DisplayName("주문 정상 생성")
    void testCreateOrderSuccess() {
        //given
        //when
        orderService.createOrder(new NickName("test"),
            List.of(new OrderItem(UUID.randomUUID(), Category.AMERICANO, 1000L, 1)));

        //then
        verify(orderRepository).insert(any(Order.class));
    }

}