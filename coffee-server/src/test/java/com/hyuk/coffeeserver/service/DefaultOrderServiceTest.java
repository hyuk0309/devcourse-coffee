package com.hyuk.coffeeserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.repository.OrderRepository;
import java.time.LocalDateTime;
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

    @Test
    @DisplayName("모든 주문 조회 (생성일 기준 내림 차순)")
    void testSearchOrdersOrderByCreatedAtSuccess() {
        //given
        var order1 = new Order(UUID.randomUUID(), new NickName("test1"),
            null, OrderStatus.ORDERED, LocalDateTime.now(), LocalDateTime.now());
        var order2 = new Order(UUID.randomUUID(), new NickName("test2"),
            null, OrderStatus.ORDERED, LocalDateTime.now(), LocalDateTime.now());
        given(orderRepository.findOrdersOrderByCreatedAt()).willReturn(List.of(order2, order1));

        //when
        var orders = orderService.searchOrdersOrderByCreatedAt();
        
        //then
        assertThat(orders).containsExactly(order2, order1);
    }
}