package com.svalero.onTimeApi.exception;

import static com.svalero.onTimeApi.Util.Literal.USER_NOT_FOUND;

public class UserNotFoundException extends Exception{

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
