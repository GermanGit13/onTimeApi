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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Sign newSign = sign;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String fecha = String.valueOf(newSign.getDay());
//        try {
//            newSign.setDay(simpleDateFormat.parse(fecha));
//        } catch (ParseException e)
//    }
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        newSign.setUserInSign(user);

        return signRepository.save(newSign);
    }

    @Override
    public void deleteSign(long id) throws SignNotFoundException {
        Sign sign = signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);
        sign.setUserInSign(null);

        signRepository.delete(sign);
    }

    @Override
    public Sign modifySign(long id, Sign newSign) throws SignNotFoundException, RollbackException {
        Sign existingSign = signRepository.findById(id)
                .orElseThrow(SignNotFoundException::new);
        newSign.setId(id);

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
}
