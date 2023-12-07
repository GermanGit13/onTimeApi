package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.domain.dto.SignOutDto;
import com.svalero.onTimeApi.domain.dto.UserPassDto;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.repository.SignRepository;
import com.svalero.onTimeApi.repository.UserRepository;
import jakarta.persistence.RollbackException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public SignOutDto signOut(long id, SignOutDto signOutDto) throws SignNotFoundException {
        Sign sign = signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);

        modelMapper.map(signOutDto, sign);
        signRepository.save(sign);

        return signOutDto;
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
    public List<Sign> findAllByOrderByDayDesc() {
        return signRepository.findAllByOrderByDayDesc();
    }

    @Override
    public Sign findById(long id) throws SignNotFoundException {
        return signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);
    }

    @Override
    public List<Sign> findByDepartmentOrderByDayDesc(String userInSign_department) {
        return signRepository.findAllByUserInSign_DepartmentOrderByDayDesc(userInSign_department);
    }

    @Override
    public List<Sign> findByUserInSignOrderByDayDesc(User user) {
        return signRepository.findByUserInSignOrderByDayDesc(user);
    }

    @Override
    public List<Sign> findByDay(LocalDate day) {
        return signRepository.findByDay(day);
    }

    @Override
    public List<Sign> findAllByUserInSign_DepartmentAndDayOrderByDayDesc(String userInSign_department, LocalDate day) {
        return signRepository.findAllByUserInSign_DepartmentAndDayOrderByDayDesc(userInSign_department, day);
    }

    @Override
    public List<Sign> findByUserInSign_DepartmentAndDayAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate day, String name) {
        return signRepository.findByUserInSign_DepartmentAndDayAndUserInSign_NameContainsOrderByDayDesc(userInSign_department, day, name);
    }

    @Override
    public List<Sign> findByUserInSign_DepartmentAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, String name) {
        return signRepository.findByUserInSign_DepartmentAndUserInSign_NameContainsOrderByDayDesc(userInSign_department, name);
    }

    @Override
    public List<Sign> findByUserInSign_DepartmentAndDayBetweenOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay) {
        return signRepository.findByUserInSign_DepartmentAndDayBetweenOrderByDayDesc(userInSign_department, firstDay, secondDay);
    }

    @Override
    public List<Sign> findByUserInSignAndDayOrderByDayDesc(User user, LocalDate day) {

        return signRepository.findByUserInSignAndDayOrderByDayDesc(user, day);
    }

    @Override
    public List<Sign> findByUserInSignAndDayBetweenOrderByDayDesc(User user, LocalDate firstDay, LocalDate seconDay) {
        return signRepository.findByUserInSignAndDayBetweenOrderByDayDesc(user, firstDay, seconDay);
    }

    @Override
    public List<Sign> findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContainsOrderByDayDesc(String userInSign_department, LocalDate firstDay, LocalDate secondDay, String name) {
        return signRepository.findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContainsOrderByDayDesc(userInSign_department, firstDay, secondDay, name);
    }

    @Override
    public List<Sign> findByUserInSign_NameOrderByDayDesc(String name) {
        return signRepository.findByUserInSign_NameOrderByDayDesc(name);
    }

    @Override
    public List<Sign> findByDayBetweenOrderByDayDesc(LocalDate firstDay, LocalDate secondDay) {
        return signRepository.findByDayBetweenOrderByDayDesc(firstDay, secondDay);
    }

    @Override
    public List<Sign> findByUserInSign_NameContainsOrderByDayDesc(String name) {
        return signRepository.findByUserInSign_NameContainsOrderByDayDesc(name);
    }

    @Override
    public List<Sign> findByDayAndUserInSign_NameContainsOrderByDayDesc(LocalDate day, String name) {
        return signRepository.findByDayAndUserInSign_NameContainsOrderByDayDesc(day, name);
    }

    @Override
    public List<Sign> findByDayBetweenAndUserInSign_NameContainsOrderByDayDesc(LocalDate firstDay, LocalDate secondDay, String name) {
        return signRepository.findByDayBetweenAndUserInSign_NameContainsOrderByDayDesc(firstDay, secondDay, name);
    }

//    @Override
//    public List<Sign> findByDayTrueAndIn_timeIsTrueAndUserInSign(LocalDate date, LocalTime inTime, User user) {
//        return signRepository.findByDayTrueAndIn_timeIsTrueAndUserInSign(date, inTime, user);
//    }
}
