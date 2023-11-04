package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Sign;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignService signService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired

    @Override
    public List<Sign> findAll() {
        return signService.findAll();
    }
}
