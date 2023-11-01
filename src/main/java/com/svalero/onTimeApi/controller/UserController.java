package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.service.UserService;
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
public class UserController {

    /**
     * Llamamos al UserService el cual llama al UserRepository y a su vez este llama a la BBDD
     */
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class); //Creamos el objeto capaz de pintar las trazas en el log y lo asociamos a la clase que queremos controlar

    /**
     * ResponseEntity<User>: Devolvemos el objeto y un 201
     * @PostMapping("/user"): Método para dar de alta en la BBDD user
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users")
    @Validated
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        logger.debug(LITERAL_BEGIN_ADD + USER);
        User newUser = userService.addUser(user);
        logger.debug(LITERAL_END_ADD + USER);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * ResponseEntity<Void>: Vacio, solo tiene código de estado
     * @DeleteMapping("/users/{id}"): Método para dar borrar por id
     * @PathVariable: Para indicar que el parámetro que le pasamos por la url
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_DELETE + USER);
        userService.deleteUser(id);
        logger.debug(LITERAL_END_DELETE + USER);

        return ResponseEntity.noContent().build(); // Devuelve nada al borrar o la excepción cuando falla
    }

    /**
     * @PutMapping("/users/{id}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody User user para pasarle los datos del objeto a modificar
     */

    /**
     * Buscar todos los usuarios
     * @GetMapping("/users/"): URL donde se devolverán los datos por el código Id
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        logger.debug(LITERAL_BEGIN_LISTALL + USER);
        List<User> users = userService.findAll();
        logger.debug(LITERAL_END_LISTALL + USER);
        return ResponseEntity.ok(users);
      }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/users/id"): URL donde se devolverán los datos por el código Id
     * @PathVariable: Para indicar que el parámetro que le pasamos en el String es que debe ir en la URL
     * throws UserNotFoundException: capturamos la exception y se la mandamos al manejador de excepciones creado más abajo @ExceptionHandler
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserId(@PathVariable long id) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_GETID + USER);
        User user = userService.findById(id);
        logger.debug(LITERAL_END_GETID + USER);

        return ResponseEntity.ok(user);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     * @GetMapping("/users/department/{department"): URL donde se devolverán los datos por el department
     */

}
