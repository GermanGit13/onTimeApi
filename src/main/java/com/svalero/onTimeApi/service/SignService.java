package com.svalero.onTimeApi.service;


import com.svalero.onTimeApi.domain.Sign;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface SignService {

    List<Sign> findAll();
}
