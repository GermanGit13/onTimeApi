package com.svalero.onTimeApi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private int code;
    private String message;
    private Map<String, String> errors; //creamos un mapa de errores para validar los campos por ejemplo

    /**
     * Para lo errores que no necesitamos tanto detalle como para usar un Map por ejemplo el error 500 etc..
     * @param code
     * @param message
     */
    public ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
        errors = new HashMap<>(); // Para devolver un vacio en lugar de un nulo en los fallos que no necesitamos más informacion de los campos que fallaron - Sino ponemos esta linea saldria nulo que tampoco pasa nada
    }
}

