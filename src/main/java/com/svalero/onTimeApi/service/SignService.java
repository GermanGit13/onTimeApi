package com.svalero.onTimeApi.service;


import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import jakarta.persistence.RollbackException;

import java.time.LocalDate;
import java.time.LocalTime;
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
    List<Sign> findByDepartment(String userInSign_department);

    List<Sign> findByUserInSign(User user);
    List<Sign> findByDay(LocalDate day);
    List<Sign> findAllByUserInSign_DepartmentAndDay(String userInSign_department, LocalDate day);
    List<Sign> findByUserInSignAndDay(User user, LocalDate day);
    List<Sign> findByUserInSign_Name(String name);
    List<Sign> findByDayBetween(LocalDate firstDay, LocalDate secondDay);
    List<Sign> findByUserInSign_NameContains(String name);
//    List<Sign> findByDayTrueAndIn_timeIsTrueAndUserInSign(LocalDate date, LocalTime inTime, User user);
}
