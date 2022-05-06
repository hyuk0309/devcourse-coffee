package com.hyuk.coffeeserver.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @MockBean
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("전체 주문 페이지 조회")
    void testViewOrdersWithoutOrderStatusSuccess() throws Exception {
        //given
        var order1 = new Order(UUID.randomUUID(), new NickName("test1"),
            null, OrderStatus.ORDERED, LocalDateTime.now(), LocalDateTime.now());
        var order2 = new Order(UUID.randomUUID(), new NickName("test2"),
            null, OrderStatus.ORDERED, LocalDateTime.now(), LocalDateTime.now());
        given(orderService.searchOrdersOrderByCreatedAt()).willReturn(List.of(order2, order1));

        //when
        //then
        mockMvc.perform(get("/orders"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("orderDtos"))
            .andExpect(view().name("order/orders"));
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("전체 주문 페이지 조회 - 특정 상태의 주문만")
    void testViewOrdersWithOrderStatusSuccess(OrderStatus orderStatus) throws Exception {
        //given
        var order = new Order(UUID.randomUUID(), new NickName("test1"),
            null, orderStatus, LocalDateTime.now(), LocalDateTime.now());
        given(orderService.searchOrdersOrderByCreatedAt(orderStatus))
            .willReturn(List.of(order));

        //when
        //then
        mockMvc.perform(get("/orders?orderStatus=" + orderStatus.name()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("orderDtos"))
            .andExpect(model().attribute("orderDtos", hasSize(1)))
            .andExpect(view().name("order/orders"));
    }

    @Test
    @DisplayName("주문 페이지 조회")
    void testViewOrderSuccess() throws Exception {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "sweet americano", Category.AMERICANO, 1000L);

        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());

        given(orderService.searchOrderWithOrderItems(order.getOrderId())).willReturn(order);

        //when
        //then
        mockMvc.perform(get("/orders/" + order.getOrderId().toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("orderDtoWithItem"))
            .andExpect(view().name("order/order"));
    }

    @Test
    @DisplayName("주문 상태 변경 요청")
    void testChangeOrderStatus() throws Exception {
        //given
        var orderId = UUID.randomUUID();

        //when
        //then
        mockMvc.perform(post("/orders/" + orderId.toString() + "/change-status"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/orders/" + orderId.toString()));
    }

    @Test
    @DisplayName("주문 삭제 요청")
    void testRemoveOrder() throws Exception {
        //given
        var orderId = UUID.randomUUID();

        //when
        //then
        mockMvc.perform(post("/orders/" + orderId.toString() + "/delete"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/orders"));
    }
}