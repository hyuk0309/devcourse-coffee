package com.hyuk.coffeeserver.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.exception.ServiceException;
import com.hyuk.coffeeserver.repository.CoffeeRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCoffeeServiceTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @InjectMocks
    private DefaultCoffeeService coffeeService;

    @Test
    @DisplayName("coffee 정상 생성")
    void testCreateCoffeeSuccess() {
        //given
        String name = "coffeeName";
        Category category = Category.AMERICANO;
        long price = 2000L;

        given(coffeeRepository.findByName(name)).willReturn(Optional.empty());

        //when
        coffeeService.createCoffee(name, category, price);

        //then
        var inOrder = inOrder(coffeeRepository);
        inOrder.verify(coffeeRepository, times(1)).findByName(name);
        inOrder.verify(coffeeRepository, times(1)).insertCoffee(any(Coffee.class));
    }

    @Test
    @DisplayName("coffee 생성 실패 - 이미 존재하는 이름")
    void testCreatCoffeeFailBecauseDuplicateName() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 2000L);
        given(coffeeRepository.findByName(coffee.getName()))
            .willReturn(Optional.of(coffee));

        //when
        //then
        assertThatThrownBy(() ->
            coffeeService.createCoffee(coffee.getName(), coffee.getCategory(), coffee.getPrice()))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("이름이 중복될 수 없습니다.");
    }
}