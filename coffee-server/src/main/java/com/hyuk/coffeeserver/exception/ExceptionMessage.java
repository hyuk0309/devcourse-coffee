package com.hyuk.coffeeserver.exception;

public enum ExceptionMessage {
    //Service 예외 메시지
    EXIST_NAME_EXP_MSG("이름이 중복될 수 없습니다."),
    INVALID_COFFEE_ID_EXP_MSG("존재하지 않는 커피 아이디 입니다."),
    INVALID_NICKNAME_FORMAT_EXP_MSG("10글자 이하의 닉네임을 설정해 주세요."),
    INVALID_ORDER_ID_EXP_MSG("존재하는 않는 주문 아이디 입니다."),

    //Repository 예외 메시지
    NOTHING_WAS_INSERTED_EXP_MSG("Nothing was inserted!"),
    NOTHING_WAS_DELETED_EXP_MSG("Nothing was deleted"),
    NOTHING_WAS_UPDATED_EXP_MSG("Nothing was updated");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
