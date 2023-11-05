package com.svalero.onTimeApi.exception;

import static com.svalero.onTimeApi.Util.Literal.SIGN_NOT_FOUND;

public class SignNotFoundException extends Exception{

    public SignNotFoundException() {
        super(SIGN_NOT_FOUND);
    }

    public SignNotFoundException(String message) {
        super(message);
    }
}
