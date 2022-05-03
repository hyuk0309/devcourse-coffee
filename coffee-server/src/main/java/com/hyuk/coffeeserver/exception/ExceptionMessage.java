package com.hyuk.coffeeserver.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
        throw new AssertionError("상수 저장용 클래스입니다.");
    }

    //Service 예외 메시지
    public static final String EXIST_NAME_EXP_MSG = "이름이 중복될 수 없습니다.";
    public static final String INVALID_COFFEE_ID_EXP_MSG = "존재하지 않는 커피 아이디 입니다.";
    public static final String INVALID_NICKNAME_FORMAT_EXP_MSG = "10글자 이하의 닉네임을 설정해 주세요.";

    //Repository 예외 메시지
    public static final String NOTHING_WAS_INSERTED_EXP_MSG = "Nothing was inserted!";
    public static final String NOTHING_WAS_DELETED_EXP_MSG = "Nothing was deleted";
    public static final String NOTHING_WAS_UPDATED_EXP_MSG = "Nothing was updated";
}
