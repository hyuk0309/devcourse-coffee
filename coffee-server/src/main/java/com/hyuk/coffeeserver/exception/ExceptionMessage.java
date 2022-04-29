package com.hyuk.coffeeserver.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
        throw new AssertionError("상수 저장용 클래스입니다.");
    }

    //Service 예외 메시지
    public static final String EXIST_NAME_EXP_MSG = "이름이 중복될 수 없습니다.";

    //Repository 예외 메시지
    public static final String NOTHING_WAS_INSERTED_EXP_MSG = "Nothing was inserted!";
}
