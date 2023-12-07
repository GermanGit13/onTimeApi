package com.svalero.onTimeApi.repository;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface SignRepository extends CrudRepository<Sign, Long> {
    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */

    /**
     * Busquedas listando todos los usuarios como referencia principal
     */
    List<Sign> findAllByOrderByDayDesc();
    List<Sign> findByDay(LocalDate day);
    List<Sign> findByDayBetweenOrderByDayDesc(LocalDate firstDay, LocalDate secondDay);
    List<Sign> findByUserInSign_NameOrderByDayDesc(String name);
    List<Sign> findByUserInSign_NameContainsOrderByDayDesc(String name);
    List<Sign> findByDayAndUserInSign_NameContainsOrderByDayDesc(LocalDate day, String name);
    List<Sign> findByDayBetweenAndUserInSign_NameContainsOrderByDayDesc(LocalDate firstDay, LocalDate secondDay, String name);

    /**
     * Busquedas listando todos los usuarios por DEPARTAMENTO como referencia principal
     */
    List<Sign> findAllByUserInSign_DepartmentOrderByDayDesc(String userInSign_department);
    List<Sign> findAllByUserInSign_DepartmentAndDayOrderByDayDesc(String userInSign_department, LocalDate day);
    List<Sign> findByUserInSign_DepartmentAndDayAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate day, String name);
    List<Sign> findByUserInSign_DepartmentAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, String name);
    List<Sign> findByUserInSign_DepartmentAndDayBetweenOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay);
    List<Sign> findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay, String name);

    /**
     * Busquedas listando por USUARIO como referencia principal
     */
    List<Sign> findByUserInSignOrderByDayDesc(User user);
    List<Sign> findByUserInSignAndDayOrderByDayDesc(User user, LocalDate day);
    List<Sign> findByUserInSignAndDayBetweenOrderByDayDesc(User user, LocalDate firstDay, LocalDate seconDay);
}
