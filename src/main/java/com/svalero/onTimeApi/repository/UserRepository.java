package com.svalero.onTimeApi.repository;

import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.DepartmentNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 1) Son los métodos que conectan con la BBDD
 * @Repository para decirle que es un DAO y que extiende de CrudRepository
 * interface: hacemos interface con la anotación específica
 * así solo con escribir el nombre de los métodos, sprinboot sabe que métodos son
 * extends CrudRepository<TipoDato, ClaveId>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */

    List<User> findAll();
    List<User> findByDepartment(String department)throws DepartmentNotFoundException;
    User findUserByUsernameAndPass(String username, String pass) throws UserNotFoundException;
    User findUserByUsername(String username) throws UserNotFoundException;
}
