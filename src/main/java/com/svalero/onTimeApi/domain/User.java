package com.svalero.onTimeApi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.svalero.onTimeApi.Util.Literal.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = USER)
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotNull(message = LITERAL_NOT_NULL)
    private String username;

    @NotNull(message = LITERAL_NOT_NULL)
    @Size(min = 6, max = 16)
    private String pass;

    @Column(columnDefinition = ROL_DEFAULT)
    @NotNull(message = LITERAL_NOT_NULL)
    private String rol;

    @Column
    private String department;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    @Email
    private String mail;

    @Column
    private String address;

    @Column
    @Size(min = 9)
    private String phone;

    @Column
    private String photo;

    /**
     * Para relacionar los usuarios con los fichajes: Un usuario puede tener x fichajes a creados
     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Sign que es donde se genera la columna con id de los usuarios
     * @JsonBackReference(value = "user_signs"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto sign completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "userInSign")
    @JsonBackReference(value = "user_signs")
    private List<Sign> signs;

    /**
     * Para relacionar los usuarios con las  reservas: Un usuario puede tener x reservas
     * @OneToMany(mappedBy = "user"): Indicamos que ya está mapeado en la Clase Booking que es donde se genera la columna con id de los usuarios
     * @JsonBackReference(value = "user_bookings"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto sign completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "userInBooking")
    @JsonBackReference(value = "user_bookings")
    private List<Booking> bookings;
}
