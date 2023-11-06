package com.svalero.onTimeApi.repository;

import com.svalero.onTimeApi.domain.Booking;
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
public interface BookingRepository extends CrudRepository<Booking, Long> {
    /**
     * Query Methods: Métodos de las sentencias SQL con el nombre ya resuelve la consulta
     */
    List<Booking> findAll();

}
