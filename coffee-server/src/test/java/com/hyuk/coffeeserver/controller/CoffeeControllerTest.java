package com.hyuk.coffeeserver.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.service.CoffeeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CoffeeController.class)
class CoffeeControllerTest {

    @MockBean
    CoffeeService coffeeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("커피 전체 조회")
    void testViewAllCoffee() throws Exception {
        //given
        given(coffeeService.findAllCoffees()).willReturn(
            List.of(
                new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 1000L),
                new Coffee(UUID.randomUUID(), "coffeeName2", Category.LATTE, 2000L)
            )
        );

        //when
        //then
        mockMvc.perform(get("/coffees"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("coffees"))
            .andExpect(view().name("coffee/coffees"));
    }

}