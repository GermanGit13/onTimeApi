package com.svalero.onTimeApi.exception;

import static com.svalero.onTimeApi.Util.Literal.BOOKING_NOT_FOUND;

public class BookingNotFoundException extends Exception{

    public BookingNotFoundException(){
        super(BOOKING_NOT_FOUND);
    }

    public BookingNotFoundException(String message) {
        super(message);
    }
}
