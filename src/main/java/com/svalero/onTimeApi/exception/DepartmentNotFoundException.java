package com.svalero.onTimeApi.exception;

import static com.svalero.onTimeApi.Util.Literal.DEPARTMENT_NOT_FOUND;

public class DepartmentNotFoundException extends Exception {

    public DepartmentNotFoundException() {
        super(DEPARTMENT_NOT_FOUND);
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
