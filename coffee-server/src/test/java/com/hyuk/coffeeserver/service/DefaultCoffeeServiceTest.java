package com.hyuk.coffeeserver.service;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.EXIST_NAME_EXP_MSG;
import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_COFFEE_ID_EXP_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("특정 ID 커피 삭제 성공")
    void testRemoveCoffeeSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 2000L);
        given(coffeeRepository.findById(coffee.getId())).willReturn(Optional.of(coffee));

        //when
        coffeeService.removeCoffee(coffee.getId());

        //then
        var inOrder = inOrder(coffeeRepository);
        inOrder.verify(coffeeRepository, times(1)).findById(coffee.getId());
        inOrder.verify(coffeeRepository, times(1)).deleteById(coffee.getId());
    }

    @Test
    @DisplayName("특정 ID 커피 삭제 실패")
    void testRemoveCoffeeFailBecauseInvalidId() {
        //given
        var invalidId = UUID.randomUUID();
        given(coffeeRepository.findById(invalidId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> coffeeService.removeCoffee(invalidId))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_COFFEE_ID_EXP_MSG.toString());
    }

    @Test
    @DisplayName("모든 커피 조회")
    void testFindAllCoffees() {
        //given
        //when
        coffeeService.findAllCoffees();

        //then
        verify(coffeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 ID 커피 조회 성공")
    void testFindCoffeeSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 2000L);
        given(coffeeRepository.findById(coffee.getId())).willReturn(Optional.of(coffee));

        //when
        var retrievedCoffee = coffeeService.findCoffee(coffee.getId());

        //then
        assertThat(retrievedCoffee.getId()).isEqualTo(coffee.getId());
    }

    @Test
    @DisplayName("특정 ID 커피 조회 실패")
    void testFindCoffeeFailBecauseInvalidId() {
        //given
        UUID invalid = UUID.randomUUID();
        given(coffeeRepository.findById(invalid)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> coffeeService.findCoffee(invalid))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_COFFEE_ID_EXP_MSG.toString());
    }

    @Test
    @DisplayName("커피 이름 변경 성공")
    void testUpdateNameSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 1000L);
        given(coffeeRepository.findById(coffee.getId())).willReturn(Optional.of(coffee));

        String updateName = "updateName";
        given(coffeeRepository.findByName(updateName)).willReturn(Optional.empty());

        given(coffeeRepository.updateCoffee(coffee)).willReturn(coffee);

        //when
        var updatedCoffee = coffeeService.updateName(coffee.getId(), updateName);

        //then
        var inOrder = inOrder(coffeeRepository);
        inOrder.verify(coffeeRepository, times(1)).findById(coffee.getId());
        inOrder.verify(coffeeRepository, times(1)).findByName(updateName);
        inOrder.verify(coffeeRepository, times(1)).updateCoffee(coffee);

        assertAll(
            () -> assertThat(updatedCoffee.getId()).isEqualTo(coffee.getId()),
            () -> assertThat(updatedCoffee.getName()).isEqualTo(updateName)
        );
    }

    @Test
    @DisplayName("커피 이름 변경 실패 - 존재하지 않는 커피")
    void testUpdateNameFailBecauseInvalidId() {
        //given
        var invalidId = UUID.randomUUID();
        given(coffeeRepository.findById(invalidId)).willReturn(Optional.empty());

        String updateName = "updateName";

        //when
        //then
        assertThatThrownBy(() -> coffeeService.updateName(invalidId, updateName))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_COFFEE_ID_EXP_MSG.toString());
    }

    @Test
    @DisplayName("커피 이름 변경 실패 - 중복되는 이름")
    void testUpdateNameFailBecauseDuplicateName() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 1000L);
        given(coffeeRepository.findById(coffee.getId())).willReturn(Optional.of(coffee));

        String updateName = "updateName";
        var existCoffee = new Coffee(UUID.randomUUID(), "updateName", Category.AMERICANO, 2000L);
        given(coffeeRepository.findByName(updateName)).willReturn(Optional.of(existCoffee));

        //when
        //then
        assertThatThrownBy(() -> coffeeService.updateName(coffee.getId(), updateName))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(EXIST_NAME_EXP_MSG.toString());
    }

}