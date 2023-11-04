package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.DepartmentNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.repository.UserRepository;
import jakarta.persistence.RollbackException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        String plainPassword = user.getPass();
        String hashsedPassword = passwordEncoder.encode(plainPassword);
        user.setPass(hashsedPassword);

        return userRepository.save(user); //Conectamos  con la BBDD mediante el repositorio
    }

    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.findById(id) //recogemos el user en concreto si existe si no saltará la excepción
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user); // si llega aquí es que existe en la BBDD y lo podemos borrar
    }

    @Override
    public User modifyUser(long id, User newUser) throws UserNotFoundException, RollbackException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        newUser.setId(id);

        if (!passwordEncoder.matches(newUser.getPass(), existingUser.getPass())) {
            String plainPassword = newUser.getPass();
            String hashsedPassword = passwordEncoder.encode(plainPassword);
            newUser.setPass(hashsedPassword);
        } else {
            newUser.setPass(existingUser.getPass());
        }

        modelMapper.map(newUser, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByDepartment(String department) throws DepartmentNotFoundException {
        return userRepository.findByDepartment(department);
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findUserByUsernameAndPass(String username, String pass) throws UserNotFoundException {
        return userRepository.findUserByUsernameAndPass(username, pass);
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username);
    }
}
