package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.Booking;
import com.svalero.onTimeApi.exception.*;
import com.svalero.onTimeApi.service.BookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svalero.onTimeApi.Util.Literal.*;

/** 4) Las clases que expongan la lógica de la Aplicación al exterior
 * parecido a los jsp antiguos, capa visible
 * @RestController: para que spring boot sepa que es la capa que se ve al exterior
 */
@RestController
public class BookingController {

    /**
     * Llamamos al SignService el cual llama al SignRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private BookingService bookingService;

    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    /**
     * ResponseEntity<Booking>: Devolvemos el objeto y un 201
     * @PostMapping("/users/{userId}/desks/{id}/bookings"): Método para dar de alta en la BBDD booking
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users/{userId}/desks/{deskId}/bookings")
    @Validated
    public ResponseEntity<Booking> addBooking(@Valid @PathVariable long userId, @PathVariable long deskId, @RequestBody Booking booking) throws UserNotFoundException, DeskNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + BOOKING);
        Booking newBooking = bookingService.addBooking(booking,userId,deskId);
        logger.debug(LITERAL_END_ADD + BOOKING);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/bookings/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) throws BookingNotFoundException{
        logger.debug(LITERAL_BEGIN_DELETE + BOOKING);
        bookingService.deleteBooking(id);
        logger.debug(LITERAL_END_DELETE+ BOOKING);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/bookings/{idBooking}/users/{idUser}/desks/{idDesk}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody User user para pasarle los datos del objeto a modificar
     */
    @PutMapping("/bookings/{idBooking}/users/{idUser}/desks/{idDesk}")
    public ResponseEntity<Booking> modifyBooking(@PathVariable long idBooking, @PathVariable long idUser, @PathVariable long idDesk, @RequestBody Booking newBooking) throws BookingNotFoundException, UserNotFoundException, DeskNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + BOOKING);
        Booking modifyBooking = bookingService.modifyBooking(idBooking, idUser, idDesk, newBooking);
        logger.debug(LITERAL_END_MODIFY + BOOKING);

        return ResponseEntity.status(HttpStatus.OK).body(modifyBooking);
    }

    /**
     * Buscar todos las reservas
     * @GetMapping("/bookings"): URL donde se devolverán los datos
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/bookings")
    public ResponseEntity<Object> getBooking() {
        logger.debug(LITERAL_END_LISTALL + BOOKING);
        List<Booking> bookings = bookingService.findAll();
        logger.debug(LITERAL_END_LISTALL + BOOKING);

        return ResponseEntity.ok(bookings);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/bookings/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws MatchNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> findById(@PathVariable long id) throws BookingNotFoundException {
        logger.debug(LITERAL_BEGIN_GETID + BOOKING);
        Booking booking = bookingService.finfById(id);
        logger.debug((LITERAL_END_GETID + BOOKING));

        return ResponseEntity.ok(booking);
    }

    /**
     * @ExceptionHandler(BookingNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso SignNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(SignNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleBookingNotFoundException(BookingNotFoundException bnfe) {
        logger.error(bnfe.getMessage(), bnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        bnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, bnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 400 Bad Request alguien llama a la API de forma incorrecta
     *@ExceptionHandler(MethodArgumentNotValidException.class) Para capturar la excepcion de las validaciones que hacemos al dar de alta un objeto
     * le pasamos un mensaje personalizado de ErrorMessage
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
        logger.error(manve.getMessage(), manve); //Mandamos la traza de la exception al log, con su mensaje y su traza
        manve.printStackTrace(); //Para la trazabilidad de la exception
        /**
         * Código que extrae que campos no han pasado la validación
         */
        Map<String, String> errors = new HashMap<>(); //Montamos un Map de errores
        manve.getBindingResult().getAllErrors().forEach(error -> { //para la exception manve recorremos todos los campos
            String fieldName = ((FieldError) error).getField(); //Extraemos con getField el nombre del campo que no ha pasado la validación
            String message = error.getDefaultMessage(); // y el mensaje asociado
            errors.put(fieldName, message);
        });
        /**
         * FIN Código que extrae que campos no han pasado la validación
         */

        ErrorMessage errorMessage = new ErrorMessage(400, BAD_REQUEST, errors); //Podemos pasarle código y mensaje o añadir los códigos de error del Map sacamos los campos que han fallado
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // le pasamos el error y el 400 de not found
    }

    /**
     * Lo usamos para contralar las excepciones en general para pillar los errors 500
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception); //Mandamos la traza de la exception al log, con su mensaje y su traza
        //exception.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(500, INTERNAL_ERROR); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
    }
}
