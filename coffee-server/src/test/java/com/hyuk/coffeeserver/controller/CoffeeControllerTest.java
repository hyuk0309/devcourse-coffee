package com.hyuk.coffeeserver.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

    @Test
    @DisplayName("커피 생성 폼 조회")
    void testViewCreateForm() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/coffees/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("coffee/new-form"));
    }

    @Test
    @DisplayName("커피 생성 요청")
    void testCreateCoffee() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post("/coffees/new")
                .param("name", "coffeeName")
                .param("category", Category.AMERICANO.name())
                .param("price", "1000"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/coffees"));
    }

    @Test
    @DisplayName("커피 상세 페이지 조회")
    void testViewCoffee() throws Exception {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 1000L);
        given(coffeeService.findCoffee(coffee.getId())).willReturn(coffee);

        //when
        //then
        mockMvc.perform(get("/coffees/" + coffee.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("coffee"))
            .andExpect(view().name("coffee/coffee"));
    }

    @Test
    @DisplayName("커피 삭제 요청")
    void testDeleteCoffee() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post("/coffees/" + UUID.randomUUID().toString() + "/delete"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/coffees"));
    }
}