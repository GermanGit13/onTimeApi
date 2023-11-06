package com.svalero.onTimeApi.service;

import com.svalero.onTimeApi.domain.Booking;
import com.svalero.onTimeApi.exception.BookingNotFoundException;
import com.svalero.onTimeApi.exception.DeskNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.repository.BookingRepository;
import jakarta.persistence.RollbackException;

import java.util.List;

/** 2) Capa donde va a estar la lógica, tendremos una interface por cada clase Java del domain
 * con los métodos aquí estará el grueso de la aplicación
 */
public interface BookingService {

    Booking addBooking(Booking booking, long userdId, long deskId) throws UserNotFoundException, DeskNotFoundException;
    void deleteBooking(long id) throws BookingNotFoundException;
    Booking modifyBooking(long idBooking, long idUser, long idDesk, Booking newBooking) throws BookingNotFoundException, UserNotFoundException, DeskNotFoundException, RollbackException;
    List<Booking> findAll();
    Booking finfById(long id) throws BookingNotFoundException;
}
