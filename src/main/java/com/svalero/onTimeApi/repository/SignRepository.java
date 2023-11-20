package com.svalero.onTimeApi.repository;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    List<Sign> findAll();
    List<Sign> findByDay(LocalDate day);
    List<Sign> findAllByUserInSign_Department(String userInSign_department);
    List<Sign> findAllByUserInSign_DepartmentAndDay(String userInSign_department, LocalDate day);
    List<Sign> findByUserInSign(User user);
    List<Sign> findByUserInSignAndDay(User user, LocalDate day);

//    List<Sign> findByDayBetweenAndDay(LocalDate firstDay, LocalDate secondDay);
}
