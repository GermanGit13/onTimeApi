package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.DepartmentNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import jakarta.persistence.RollbackException;
import org.springframework.stereotype.Service;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface UserService {

    User addUser(User user);
    void deleteUser(long id) throws UserNotFoundException;
    User modifyUser(long id, User newUser) throws UserNotFoundException, RollbackException;
    List<User> findAll();
    List<User> findByDepartment(String department) throws DepartmentNotFoundException;
    User findById(long id) throws UserNotFoundException;
    User findUserByUsernameAndPass(String username, String pass) throws  UserNotFoundException;
    User findUserByUsername(String username) throws UserNotFoundException;
}
