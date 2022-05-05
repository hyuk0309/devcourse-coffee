package com.hyuk.coffeeserver.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("전체 주문 조회 페이지")
    void testViewOrdersSuccess() throws Exception {
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

}