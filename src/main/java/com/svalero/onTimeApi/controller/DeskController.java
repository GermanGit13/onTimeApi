package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.Desk;
import com.svalero.onTimeApi.exception.DeskNotFoundException;
import com.svalero.onTimeApi.exception.ErrorMessage;
import com.svalero.onTimeApi.service.DeskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeskController {
    @Autowired
    private DeskService deskService;

    private final Logger logger = LoggerFactory.getLogger(DeskController.class); //Creamos el objeto capaz de pintar las trazas en el log y lo asociamos a la clase que queremos controlar

    /**
     * ResponseEntity<Desk>: Devolvemos el objeto y un 201
     * @PostMapping("/desks"): Método para dar de alta en la BBDD desk
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     */
    @PostMapping("/desks")
    @Validated
    public ResponseEntity<Desk> addDesk(@Valid @RequestBody Desk desk) {
        logger.debug(LITERAL_BEGIN_ADD + DESK);
        Desk newDesk = deskService.addDesk(desk);
        logger.debug(LITERAL_END_ADD + DESK);

        return new ResponseEntity<>(newDesk, HttpStatus.CREATED);
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/desks/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/desks/{id}")
    public ResponseEntity<Void> deleteDesk(@PathVariable long id) throws DeskNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + DESK);
        deskService.deleteDesk(id);
        logger.debug(LITERAL_END_DELETE + DESK);

        return ResponseEntity.noContent().build();
    }

    /**
     * @PutMapping("/desks/{id}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody User user para pasarle los datos del objeto a modificar
     */
    @PutMapping("/desks/{id}")
    public ResponseEntity<Desk> modifyDesk(@PathVariable long id, @RequestBody Desk desk) throws DeskNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + DESK);
        Desk modifiedDesk = deskService.modifyDesk(id, desk);
        logger.debug(LITERAL_END_MODIFY + DESK);

        return ResponseEntity.status(HttpStatus.OK).body(modifiedDesk);
    }

    /**
     * Buscar todos los escritorios
     * @GetMapping("/desks/"): URL donde se devolverán los datos por el código Id
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/desks")
    public ResponseEntity<Object> getDesk() {
        logger.debug(LITERAL_END_LISTALL + DESK);
        List<Desk> desks = deskService.findAll();
        logger.debug(LITERAL_END_LISTALL + DESK);

        return ResponseEntity.ok(desks);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/desks/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws UserNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/desks/{id}")
    public ResponseEntity<Desk> getDesk(@PathVariable long id) throws DeskNotFoundException {
        logger.debug(LITERAL_BEGIN_GETID + DESK);
        Desk desk = deskService.findById(id);
        logger.debug(LITERAL_END_GETID + DESK);

        return ResponseEntity.ok(desk);
    }

    /**
     * @ExceptionHandler(UserNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso DeskNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(DeskNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(DeskNotFoundException dnfe) {
        logger.error(dnfe.getMessage(), dnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        dnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, dnfe.getMessage());
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
