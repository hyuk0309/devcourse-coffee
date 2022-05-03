package com.hyuk.coffeeserver.entity;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_EMAIL_FORMAT_EXP_MSG;

import com.hyuk.coffeeserver.exception.ServiceException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern emailPattern = Pattern.compile(
        "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b");

    private final String email;

    public Email(String email) {
        validateEmailFormat(email);

        this.email = email;
    }

    private void validateEmailFormat(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new ServiceException(INVALID_EMAIL_FORMAT_EXP_MSG);
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email) o;
        return getEmail().equals(email1.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
