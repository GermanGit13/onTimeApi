package com.svalero.onTimeApi.controller;

import com.svalero.onTimeApi.domain.Sign;
import com.svalero.onTimeApi.domain.User;
import com.svalero.onTimeApi.domain.dto.SignOutDto;
import com.svalero.onTimeApi.domain.dto.UserPassDto;
import com.svalero.onTimeApi.exception.ErrorMessage;
import com.svalero.onTimeApi.exception.SignNotFoundException;
import com.svalero.onTimeApi.exception.UserNotFoundException;
import com.svalero.onTimeApi.service.SignService;
import com.svalero.onTimeApi.service.UserService;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(SignController.class);

    /**
     * ResponseEntity<Sign>: Devolvemos el objeto y un 201
     * @PostMapping("/users/{userId}/signs"): Método para dar de alta en la BBDD sign
     * @RequestBody: Los datos van en el cuerpo de la llamada como codificados
     * @Valid Para decir que valide los campos a la hora de añadir un nuevo objeto,  los campos los definidos en el domain de que forma no pueden ser introducidos o dejados en blanco por ejemplo en la BBDD
     */
    @PostMapping("/users/{userId}/signs")
    @Validated
    public ResponseEntity<Sign> addSign(@Valid @PathVariable long userId, @RequestBody Sign sign) throws UserNotFoundException {

        /**
         * Para poder fichar dias anteriores o crear un fichaje en el dia
         */
        if (sign.getDay() == null){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            sign.setIn_time(LocalTime.parse(LocalTime.now().format(dateTimeFormatter)));
            sign.setDay(LocalDate.now());
            System.out.println("Usuario registra fichaje: " + userId );
            System.out.println("Datos que recibo IN: " + sign.getDay() + " / " + sign.getIn_time()  + " / " + sign.getModality()  + " / " + sign.getIncidence_in());
            System.out.println("Datos que recibo OUT: " + sign.getDay() + " / " + sign.getOut_time()  + " / " + sign.getModality()  + " / " + sign.getIncidence_out());

            logger.debug(LITERAL_BEGIN_ADD + SIGN);
            Sign newSign = signService.addSign(sign, userId);
            return new ResponseEntity<>(newSign, HttpStatus.CREATED);
        } else {
            System.out.println("Usuario registra fichaje: " + userId );
            System.out.println("Datos que recibo IN: " + sign.getDay() + " / " + sign.getIn_time()  + " / " + sign.getOut_time());

            logger.debug(LITERAL_BEGIN_ADD + SIGN);
            Sign newSign = signService.addSign(sign, userId);
            return new ResponseEntity<>(newSign, HttpStatus.CREATED);
        }
    }

    @PatchMapping("/signs/{id}")
    public ResponseEntity<SignOutDto> signOut(@PathVariable long id, @Valid @RequestBody SignOutDto signOutDto) throws SignNotFoundException{
        logger.debug(LITERAL_BEGIN_SIGNOUT + SIGN);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        signOutDto.setOut_time(LocalTime.parse(LocalTime.now().format(dateTimeFormatter)));

        signService.signOut(id, signOutDto);
        return new ResponseEntity<>(signOutDto, HttpStatus.CREATED);
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

    /**
     * @PutMapping("/signs/{idSign}/users/{idUser}"): Método para modificar
     * @PathVariable: Para indicar que el parámetro que le pasamos
     * @RequestBody User user para pasarle los datos del objeto a modificar
     */
    @PutMapping("/signs/{idSign}/users/{idUser}")
    public ResponseEntity<Sign> modifySign(@PathVariable long idSign, @PathVariable long idUser, @RequestBody Sign sign) throws SignNotFoundException, UserNotFoundException {
        logger.debug(LITERAL_BEGIN_MODIFY + SIGN);
        User user = userService.findById(idUser);
        Sign modifySign = signService.modifySign(idSign,idUser, sign);
        logger.debug(LITERAL_END_MODIFY + SIGN);

        return ResponseEntity.status(HttpStatus.OK).body(modifySign);
    }

    /**
     * Buscar todos los fichajes
     * @GetMapping("/signs"): URL donde se devolverán los datos
     * @RequestParam: Son las QueryParam se usa para poder hacer filtrados en las busquedas "Where"
     */
    @GetMapping("/signs")
    public ResponseEntity<Object> getSigns(@RequestParam(name = "userInSign_department", defaultValue = "", required = false) String department,
                                           @RequestParam(name = "day", defaultValue = "", required = false) String day,
                                           @RequestParam(name = "name", defaultValue = "", required = false) String name,
                                           @RequestParam(name = "secondDay", defaultValue = "", required = false) String secondDay) {
        System.out.println("List Signs All Signs ");

        /**
         * Buscar todos los fichajes en un día
         */
        if (department.equals("") && !day.equals("") && secondDay.equals("")  && name.equals("")) {
            System.out.println("List Signs by Day: " + day);
            LocalDate firstDate = LocalDate.parse(day);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByDay(firstDate);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes entre fechas
         */
        if (department.equals("")  && !day.equals("") && !secondDay.equals("") && name.equals("")) {
            LocalDate firstDate = LocalDate.parse(day);
            LocalDate secondDate = LocalDate.parse(secondDay);
            System.out.println("List Signs Between: " + firstDate + " / " + secondDate);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByDayBetweenOrderByDayDesc(firstDate, secondDate);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes por letra en el nombre
         */
        if ( department.equals("") && day.equals("") && secondDay.equals("") && !name.equals("") ) {
            System.out.println("List Signs by name: " + name);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
//            List<Sign> signs = signService.findByUserInSign_Name(name);
            List<Sign> signs = signService.findByUserInSign_NameContainsOrderByDayDesc(name);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes en un día y letra en el nombre
         */
        if(department.equals("") && !day.equals("") && secondDay.equals("") && !name.equals("") ) {
            LocalDate firstDate = LocalDate.parse(day);
            System.out.println("List Signs by Day: " + day + " / " + name);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByDayAndUserInSign_NameContainsOrderByDayDesc(firstDate, name);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes entre fechas y name
         */
        if (department.equals("")  && !day.equals("") && !secondDay.equals("") && !name.equals("")) {
            LocalDate firstDate = LocalDate.parse(day);
            LocalDate secondDate = LocalDate.parse(secondDay);
            System.out.println("List Signs Between and Name: " + firstDate + " / " + secondDate + " / " + name);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByDayBetweenAndUserInSign_NameContainsOrderByDayDesc(firstDate, secondDate, name);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento
         */
        if (!department.equals("") && day.equals("") && secondDay.equals("") && name.equals("") ) {
            System.out.println("List Signs by department: " + department);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByDepartmentOrderByDayDesc(department);
            return  ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento y día
         */
        if (!department.equals("") && !day.equals("") && secondDay.equals("") && name.equals("")) {
            System.out.println("List Signs by department and Day: " + department + " / " + day);
            LocalDate firstDate = LocalDate.parse(day);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findAllByUserInSign_DepartmentAndDayOrderByDayDesc(department, firstDate);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento, día y name
         */
        if (!department.equals("") && !day.equals("")  && secondDay.equals("") && !name.equals("")) {
            System.out.println("List Signs by department and Day: " + department + " / " + day + " / " + name);
            LocalDate firstDate = LocalDate.parse(day);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByUserInSign_DepartmentAndDayAndUserInSign_NameContainsOrderByDayDesc(department, firstDate, name);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento y name
         */
        if (!department.equals("")  && day.equals("") && secondDay.equals("") && !name.equals("")) {
            System.out.println("List Signs by department and Day: " + department + " / " + name);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByUserInSign_DepartmentAndUserInSign_NameContainsOrderByDayDesc(department, name);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento entre fechas
         */
        if (!department.equals("") && !day.equals("") && !secondDay.equals("") && name.equals("")) {
            LocalDate firstDate = LocalDate.parse(day);
            LocalDate secondDate = LocalDate.parse(secondDay);
            System.out.println("List Signs by department between: " + firstDate + " / " + secondDate);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByUserInSign_DepartmentAndDayBetweenOrderByDayDesc(department, firstDate, secondDate);
            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un departamento entre fechas y nombre
         */
        if (!department.equals("") && !day.equals("") && !secondDay.equals("") && !name.equals("")) {
            LocalDate firstDate = LocalDate.parse(day);
            LocalDate secondDate = LocalDate.parse(secondDay);
            System.out.println("List Signs by department between: " + firstDate + " / " + secondDate);
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            List<Sign> signs = signService.findByUserInSign_DepartmentAndDayBetweenAndUserInSign_NameContainsOrderByDayDesc(department, firstDate, secondDate, name);
            return ResponseEntity.ok(signs);
        }

        logger.debug(LITERAL_END_LISTALL + SIGN);
        List<Sign> signs = signService.findAllByOrderByDayDesc();
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

    @GetMapping("/users/{userId}/signs")
    public ResponseEntity<Object> findByUserInSign(@PathVariable String userId,
                                                   @RequestParam(name = "day", defaultValue = "", required = false) String day,
                                                   @RequestParam(name = "secondDay", defaultValue = "", required = false) String seconDay) throws UserNotFoundException {

        /**
         * Buscar todos los fichajes de un usuario
         */
        if (!userId.equals("") && day.equals("") && seconDay.equals("")) {
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            long userIdNew = Long.parseLong(userId);
            User user = userService.findById(userIdNew);
            System.out.println("List Signs by User: " + user.getUsername());
            List<Sign> signs = signService.findByUserInSignOrderByDayDesc(user);
            logger.debug(LITERAL_END_LISTALL + SIGN);

            return ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un usuario y fecha
         */
        if (!userId.equals("") && !day.equals("") && seconDay.equals("")) {
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            long userIdNew = Long.parseLong(userId);
            User user = userService.findById(userIdNew);
            LocalDate firstDay = LocalDate.parse(day);
            System.out.println("List Signs by User and Day: " + user.getUsername() + " / " + day);
            List<Sign> signs = signService.findByUserInSignAndDayOrderByDayDesc(user, firstDay);

            return  ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un usuario y fecha
         */
        if (!userId.equals("") && !day.equals("") && !seconDay.equals("")) {
            logger.debug(LITERAL_BEGIN_LISTALL + SIGN);
            long userIdNew = Long.parseLong(userId);
            User user = userService.findById(userIdNew);
            LocalDate firstDay = LocalDate.parse(day);
            LocalDate secondDay = LocalDate.parse(seconDay);
            System.out.println("List Signs by User between date: " + user.getUsername() + " / " + firstDay + " / " + seconDay);
            List<Sign> signs = signService.findByUserInSignAndDayBetweenOrderByDayDesc(user, firstDay, secondDay);

            return  ResponseEntity.ok(signs);
        }

        /**
         * Buscar todos los fichajes de un usuario entre fechas
         */

        return null;
    }

    /**
     * @ExceptionHandler(SignNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso SignNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(SignNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleSignNotFoundException(SignNotFoundException snfe) {
        logger.error(snfe.getMessage(), snfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        snfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, snfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
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
