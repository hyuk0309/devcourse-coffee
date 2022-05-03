package com.hyuk.coffeeserver.entity;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_EMAIL_FORMAT_EXP_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hyuk.coffeeserver.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    @DisplayName("이메일 생성 성공")
    void testCreateEmailSuccess() {
        //given
        //when
        var email = new Email("test@gmail.com");

        //then
        assertThat(email).isNotNull();
    }

    @Test
    @DisplayName("이메일 생성 실패")
    void testCreateEmailFailBecauseInvalidFormat() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Email("test"))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_EMAIL_FORMAT_EXP_MSG);
    }
}