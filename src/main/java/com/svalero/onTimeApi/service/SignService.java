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
    List<Sign> findByDay(LocalDate day);
    List<Sign> findByDayBetween(LocalDate firstDay, LocalDate secondDay);
    List<Sign> findByUserInSign_Name(String name);
    List<Sign> findByUserInSign_NameContains(String name);
    List<Sign> findByDayAndUserInSign_NameContains(LocalDate day, String name);
    List<Sign> findByDayBetweenAndUserInSign_NameContains(LocalDate firstDay, LocalDate secondDay, String name);

    List<Sign> findByDepartment(String userInSign_department);
    List<Sign> findAllByUserInSign_DepartmentAndDay(String userInSign_department, LocalDate day);
    List<Sign> findByUserInSign_DepartmentAndDayAndUserInSign_NameContains(String userInSign_department, LocalDate day, String name);
    List<Sign> findByUserInSign_DepartmentAndUserInSign_NameContains(String userInSign_department, String name);
    List<Sign> findByUserInSign_DepartmentAndDayBetween(String userInSign_department, LocalDate firstDay, LocalDate secondDay);

    Sign findById(long id) throws SignNotFoundException;
    List<Sign> findByUserInSign(User user);
    List<Sign> findByUserInSignAndDay(User user, LocalDate day);
    List<Sign> findByUserInSignAndDayBetween(User user, LocalDate firstDay, LocalDate seconDay);
    List<Sign> findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContains(String userInSign_department, LocalDate firstDay, LocalDate secondDay, String name);
//    List<Sign> findByDayTrueAndIn_timeIsTrueAndUserInSign(LocalDate date, LocalTime inTime, User user);
}
