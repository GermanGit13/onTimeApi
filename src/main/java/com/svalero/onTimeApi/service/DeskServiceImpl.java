package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Desk;
import com.svalero.onTimeApi.exception.DeskNotFoundException;
import com.svalero.onTimeApi.repository.DeskRepository;
import jakarta.persistence.RollbackException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskServiceImpl implements DeskService{
    @Autowired
    private DeskRepository deskRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Desk addDesk(Desk desk) {
        return deskRepository.save(desk);
    }

    @Override
    public void deleteDesk(long id) throws DeskNotFoundException {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);
        deskRepository.delete(desk);
    }

    @Override
    public Desk modifyDesk(long id, Desk newDesk) throws DeskNotFoundException, RollbackException {
        Desk existingDesk = deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);
        newDesk.setId(id);

        modelMapper.map(newDesk, existingDesk);
        return deskRepository.save(existingDesk);
    }

    @Override
    public List<Desk> findAll() {
        return deskRepository.findAll();
    }

    @Override
    public Desk findById(long id) throws DeskNotFoundException {
        return deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);
    }
}
