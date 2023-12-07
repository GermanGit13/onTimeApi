package com.svalero.onTimeApi.service;


import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.domain.dto.SignOutDto;
import com.svalero.onTimeApi.domain.dto.UserPassDto;
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
    SignOutDto signOut(long id, SignOutDto signOutDto) throws SignNotFoundException;
    void deleteSign (long id) throws SignNotFoundException;
    Sign modifySign(long idSign, long idUser, Sign newSign) throws SignNotFoundException, UserNotFoundException, RollbackException;
    List<Sign> findAllByOrderByDayDesc();
    List<Sign> findByDay(LocalDate day);
    List<Sign> findByDayBetweenOrderByDayDesc(LocalDate firstDay, LocalDate secondDay);
    List<Sign> findByUserInSign_NameOrderByDayDesc(String name);
    List<Sign> findByUserInSign_NameContainsOrderByDayDesc(String name);
    List<Sign> findByDayAndUserInSign_NameContainsOrderByDayDesc(LocalDate day, String name);
    List<Sign> findByDayBetweenAndUserInSign_NameContainsOrderByDayDesc(LocalDate firstDay, LocalDate secondDay, String name);

    List<Sign> findByDepartmentOrderByDayDesc(String userInSign_department);
    List<Sign> findAllByUserInSign_DepartmentAndDayOrderByDayDesc(String userInSign_department, LocalDate day);
    List<Sign> findByUserInSign_DepartmentAndDayAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate day, String name);
    List<Sign> findByUserInSign_DepartmentAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, String name);
    List<Sign> findByUserInSign_DepartmentAndDayBetweenOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay);

    Sign findById(long id) throws SignNotFoundException;
    List<Sign> findByUserInSignOrderByDayDesc(User user);
    List<Sign> findByUserInSignAndDayOrderByDayDesc(User user, LocalDate day);
    List<Sign> findByUserInSignAndDayBetweenOrderByDayDesc(User user, LocalDate firstDay, LocalDate seconDay);
    List<Sign> findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay, String name);
//    List<Sign> findByDayTrueAndIn_timeIsTrueAndUserInSign(LocalDate date, LocalTime inTime, User user);
}
