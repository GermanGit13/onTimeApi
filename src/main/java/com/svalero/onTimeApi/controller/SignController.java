package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.service.SignService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.svalero.onTimeApi.Util.Literal.*;

/** 4) Las clases que expongan la lógica de la Aplicación al exterior
 * parecido a los jsp antiguos, capa visible
 * @RestController: para que spring boot sepa que es la capa que se ve al exterior
 */
@RestController
public class SignController {

    /**
     * Llamamos al SignService el cual llama al SignRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private SignService signService;

    private final Logger logger = LoggerFactory.getLogger(SignController.class);

    /**
     * ResponseEntity<Sign>: Devolvemos el objeto y un 201
     * @PostMapping("/users/{userId}/signs"): Método para dar de alta en la BBDD sign
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users/{userId}/signs")
    @Validated
    public ResponseEntity<Sign> addSign(@Valid @PathVariable long userId, Sign sign) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_ADD + SIGN);
        Sign newSign = signService.addSign(sign, userId);
        return new ResponseEntity<>(newSign, HttpStatus.CREATED);
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/signs/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/signs/{id}")
    public ResponseEntity<Void> deleteSign(@PathVariable long id) throws SignNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + SIGN);
        signService.deleteSign(id);
        logger.debug(LITERAL_END_DELETE + SIGN);

        return ResponseEntity.noContent().build();
    }

    // TODO Modified
    /**
     * @PutMapping("/signs/{id}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody User user para pasarle los datos del objeto a modificar
     */


    /**
     * Buscar todos los fichajes
     * @GetMapping("/signs"): URL donde se devolverán los datos
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/signs")
    public ResponseEntity<Object> getSign(){
        logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
        List<Sign> signs = signService.findAll();
        logger.debug(LITERAL_END_LISTALL + SIGN);

        return ResponseEntity.ok(signs);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/signs/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws MatchNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("signs/{id}")
    public ResponseEntity<Sign> findById(@PathVariable long id) throws SignNotFoundException {
        logger.debug(LITERAL_BEGIN_GETID + SIGN);
        Sign sign = signService.findById(id);
        logger.debug(LITERAL_END_GETID + SIGN);

        return ResponseEntity.ok(sign);
    }
}
