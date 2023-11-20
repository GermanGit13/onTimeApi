package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.repository.SignRepository;
import com.svalero.onTimeApi.repository.UserRepository;
import jakarta.persistence.RollbackException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignRepository signRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Sign addSign(Sign sign, long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        sign.setUserInSign(user);

        return signRepository.save(sign);
    }

    @Override
    public void deleteSign(long id) throws SignNotFoundException {
        Sign sign = signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);
        sign.setUserInSign(null);

        signRepository.delete(sign);
    }

    @Override
    public Sign modifySign(long idSign, long idUser, Sign newSign) throws SignNotFoundException, UserNotFoundException, RollbackException {
        Sign existingSign = signRepository.findById(idSign)
                .orElseThrow(SignNotFoundException::new);
        User existingUser = userRepository.findById(idUser)
                        .orElseThrow(UserNotFoundException::new);
        newSign.setId(idSign);
        newSign.setUserInSign(existingUser);

        modelMapper.map(newSign, existingSign);
        return signRepository.save(existingSign);
    }

    @Override
    public List<Sign> findAll() {
        return signRepository.findAll();
    }

    @Override
    public Sign findById(long id) throws SignNotFoundException {
        return signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);
    }

    @Override
    public List findByDepartment(String userInSign_department) {
        return signRepository.findAllByUserInSign_Department(userInSign_department);
    }

    @Override
    public List findByUserInSign(User user) {
        return signRepository.findByUserInSign(user);
    }

    @Override
    public List findByDay(LocalDate day) {
        return signRepository.findByDay(day);
    }

    @Override
    public List<Sign> findAllByUserInSign_DepartmentAndDay(String userInSign_department, LocalDate day) {
        return signRepository.findAllByUserInSign_DepartmentAndDay(userInSign_department, day);
    }

    @Override
    public List<Sign> findByUserInSignAndDay(User user, LocalDate day) {


        return signRepository.findByUserInSignAndDay(user, day);
    }

//    @Override
//    public List findByDayBetweenAndDay(LocalDate firstDay, LocalDate secondDay) {
//        return signRepository.findByDayBetweenAndDay(firstDay, secondDay);
//    }
}
