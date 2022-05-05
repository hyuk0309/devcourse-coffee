package com.hyuk.coffeeserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("HomePage로 Redirect")
    void testRedirectToHomePage() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/homes"));
    }

    @Test
    @DisplayName("Home page 요청")
    void testViewHomePage() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/homes"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
    }
}