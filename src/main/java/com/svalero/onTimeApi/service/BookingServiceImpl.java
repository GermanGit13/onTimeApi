package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Booking;
import com.svalero.onTimeApi.domain.Desk;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.BookingNotFoundException;
import com.svalero.onTimeApi.exception.DeskNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.repository.BookingRepository;
import com.svalero.onTimeApi.repository.DeskRepository;
import com.svalero.onTimeApi.repository.UserRepository;
import jakarta.persistence.RollbackException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 3) Para implementar la interface de cada service
 * @Service: Para que spring boot sepa que es la capa del service y donde está la lógica
 */
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeskRepository deskRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Booking addBooking(Booking booking, long userdId, long deskId) throws UserNotFoundException, DeskNotFoundException {
        User user = userRepository.findById(userdId)
                .orElseThrow(UserNotFoundException::new);
        Desk desk = deskRepository.findById(deskId)
                .orElseThrow(DeskNotFoundException::new);
        booking.setUserInBooking(user);
        booking.setDeskInBooking(desk);

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(long id) throws BookingNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);
        booking.setUserInBooking(null);
        booking.setDeskInBooking(null);

        bookingRepository.delete(booking);
    }

    @Override
    public Booking modifyBooking(long idBooking, long idUser, long idDesk, Booking newBooking) throws BookingNotFoundException, UserNotFoundException, DeskNotFoundException, RollbackException {
       Booking existingBooking = bookingRepository.findById(idBooking)
               .orElseThrow(BookingNotFoundException::new);
       User existingUser = userRepository.findById(idUser)
               .orElseThrow(UserNotFoundException::new);
       Desk existingDesk = deskRepository.findById(idDesk)
               .orElseThrow(DeskNotFoundException::new);
       newBooking.setId(idBooking);
       newBooking.setUserInBooking(existingUser);
       newBooking.setDeskInBooking(existingDesk);

       modelMapper.map(newBooking, existingBooking);
       return bookingRepository.save(existingBooking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking finfById(long id) throws BookingNotFoundException {
        return bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);
    }
}
