package com.hyuk.coffeeserver.entity;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_NICKNAME_FORMAT_EXP_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hyuk.coffeeserver.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NickNameTest {

    @Test
    @DisplayName("닉네임 정상 생성")
    void testCreateNickNameSuccess() {
        //given
        //when
        var nickName = new NickName("test");

        //then
        assertThat(nickName).isNotNull();
    }

    @Test
    @DisplayName("닉네임 생성 실패")
    void testCreateNickNameFailBecauseInvalidFormat() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new NickName("too long............."))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining(INVALID_NICKNAME_FORMAT_EXP_MSG);
    }
}