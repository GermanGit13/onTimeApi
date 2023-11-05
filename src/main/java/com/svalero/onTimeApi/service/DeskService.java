package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Desk;
import com.svalero.onTimeApi.exception.DeskNotFoundException;
import jakarta.persistence.RollbackException;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface DeskService {

    Desk addDesk(Desk desk);
    void deleteDesk(long id) throws DeskNotFoundException;
    Desk modifyDesk(long id, Desk newDesk) throws DeskNotFoundException, RollbackException;
    List<Desk> findAll();
    Desk findById(long id) throws DeskNotFoundException;
}
