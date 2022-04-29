package com.hyuk.coffeeserver.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoffeeTest {

    @Test
    @DisplayName("Coffee 생성")
    void testFourParamConstructor() {
        //given
        //when
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee", Category.AMERICANO, 2000L);

        //then
        assertThat(coffee).isNotNull();
    }

    @Test
    @DisplayName("Coffee 생성")
    void testAllParamConstructor() {
        //given
        //when
        var coffee = new Coffee(
            UUID.randomUUID(),
            "testCoffee",
            Category.AMERICANO,
            2000L,
            "testDescription",
            LocalDateTime.now(),
            LocalDateTime.now());

        //then
        assertThat(coffee).isNotNull();
    }
}