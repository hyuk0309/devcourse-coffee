package com.hyuk.coffeeserver.service;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_ORDER_ID_EXP_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.exception.ServiceException;
import com.hyuk.coffeeserver.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Test
    @DisplayName("특정 아이디 주문 조회(주문 아이템과 함께) 성공")
    void testSearchOrderWithOrderItemsSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "sweet coffee", Category.AMERICANO, 1000L);

        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());

        given(orderRepository.findOrderWithOrderItems(order.getOrderId()))
            .willReturn(Optional.of(order));

        //when
        orderService.searchOrderWithOrderItems(order.getOrderId());

        //then
        verify(orderRepository).findOrderWithOrderItems(order.getOrderId());
    }

    @Test
    @DisplayName("특정 아이디 주문 조회(주문 아이템과 함께) 실패")
    void testSearchOrderWithOrderItemsFailBecauseInvalidOrderId() {
        //given
        UUID invalidOrderId = UUID.randomUUID();
        given(orderRepository.findOrderWithOrderItems(invalidOrderId))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> orderService.searchOrderWithOrderItems(invalidOrderId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_ORDER_ID_EXP_MSG.toString());
    }

    @Test
    @DisplayName("주문 상태 변경")
    void testChangeOrderStatus() {
        //given
        var orderId = UUID.randomUUID();

        //when
        orderService.changeOrderStatus(orderId);

        //then
        verify(orderRepository).updateOrderStatusByOrderId(orderId);
    }
}