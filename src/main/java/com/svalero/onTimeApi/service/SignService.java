package com.svalero.onTimeApi.service;


import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import jakarta.persistence.RollbackException;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface SignService {

    Sign addSign (Sign sign, long userId) throws UserNotFoundException;
    void deleteSign (long id) throws SignNotFoundException;
    Sign modifySign(long idSign, long idUser, Sign newSign) throws SignNotFoundException, UserNotFoundException, RollbackException;
    List<Sign> findAll();
    Sign findById(long id) throws SignNotFoundException;
    List findByDepartment(String userInSign_department);
    List findByUserInSign(User user);
}
