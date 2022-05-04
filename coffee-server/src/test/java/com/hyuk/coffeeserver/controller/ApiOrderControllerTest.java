package com.hyuk.coffeeserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuk.coffeeserver.dto.CreateOrderRequest;
import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.service.OrderService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ApiOrderController.class)
class ApiOrderControllerTest {

    @MockBean
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 생성 요청")
    void testCreateOrderSuccess() throws Exception {
        //given
        var createOrderRequest = new CreateOrderRequest("test",
            List.of(new OrderItem(UUID.randomUUID(), Category.AMERICANO, 1000L, 1)));

        //when
        //then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequest)))
            .andExpect(status().isCreated())
            .andDo(print());
    }
}