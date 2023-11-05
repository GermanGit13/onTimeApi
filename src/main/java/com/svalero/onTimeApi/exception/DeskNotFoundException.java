package com.svalero.onTimeApi.exception;


import static com.svalero.onTimeApi.Util.Literal.DESK_NOT_FOUND;

public class DeskNotFoundException extends Exception{

    public DeskNotFoundException() {
        super(DESK_NOT_FOUND);
    }

    public DeskNotFoundException(String message) {
        super(message);
    }
}
