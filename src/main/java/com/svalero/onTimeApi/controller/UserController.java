package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.exception.DepartmentNotFoundException;
import com.svalero.onTimeApi.exception.ErrorMessage;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
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

    // TODO Modified
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
    public ResponseEntity<User> getUser(@PathVariable long id) throws UserNotFoundException {
        logger.debug(LITERAL_BEGIN_GETID + USER);
        User user = userService.findById(id);
        logger.debug(LITERAL_END_GETID + USER);

        return ResponseEntity.ok(user);
    }

    /**
     * ResponseEntity.ok: Devuelve un 200 ok con los datos buscados
     *
     * @GetMapping("/users/department/{department"): URL donde se devolverán los datos por el department
     */
    @GetMapping("/users/department/{department}")
    public Object getUserByDepartment(@PathVariable String department) throws DepartmentNotFoundException {
         logger.debug(LITERAL_BEGIN_LISTDEPARTMENT);

        List<User> users = userService.findByDepartment(department);

        if (users.isEmpty()) {
            ErrorMessage errorMessage = new ErrorMessage(404, DEPARTMENT_NOT_FOUND);
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
        }
        logger.debug(LITERAL_END_LISTDEPARTMENT);

        return ResponseEntity.ok(users);
    }

    /**
     *
     */
    @GetMapping("/users/login")
    public ResponseEntity<Object> getLogin(@RequestParam (name = "username", defaultValue = "" )  String username,
                                           @RequestParam (name = "pass", defaultValue = "") String pass) throws UserNotFoundException {

            logger.debug(LITERAL_BEGIN_LISTALL + USER);
            User user = userService.findUserByUsername(username);
            System.out.println(user.getUsername() + "/" + user.getPass() + "/" + pass);

            if (user.getUsername() != null && user.getPass().equals(pass)) {
                return ResponseEntity.ok(user);
            }  else {
                ErrorMessage errorMessage = new ErrorMessage(400, "Datos Incorrectos");
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
            }
    }

    /**
     * @ExceptionHandler(UserNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso UserNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException unfe) {
        logger.error(unfe.getMessage(), unfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        unfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, unfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
    }

    /**
     * @ExceptionHandler(DepartmentNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso DepartmentNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleDepartmentNotFoundException(DepartmentNotFoundException dnfe) {
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
