package com.hyuk.coffeeserver.entity;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_NICKNAME_FORMAT_EXP_MSG;

import com.hyuk.coffeeserver.exception.ServiceException;
import java.util.Objects;

public class NickName {

    private final String nickName;

    public NickName(String nickName) {
        validateNickNameFormat(nickName);

        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NickName nickName1 = (NickName) o;
        return getNickName().equals(nickName1.getNickName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNickName());
    }

    private void validateNickNameFormat(String nickName) {
        if (nickName.length() >= 10) {
            throw new ServiceException(INVALID_NICKNAME_FORMAT_EXP_MSG.toString());
        }
    }
}
