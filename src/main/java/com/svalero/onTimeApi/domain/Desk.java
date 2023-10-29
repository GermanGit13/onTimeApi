package com.svalero.onTimeApi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.svalero.onTimeApi.Util.Literal.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = DESK)
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private int floor;

    private boolean desk;

    private boolean meetingRoom;

    /**
     * Para relacionar los sitios con las  reservas: Un sitio puede tener x reservas
     * @OneToMany(mappedBy = "desk"): Indicamos que ya está mapeado en la Clase Booking que es donde se genera la columna con id de los escritorios
     * @JsonBackReference(value = "desk_bookings"): Es para cortar la recursión infinita, por el lado del usuario para que no se siga mostrando el objeto sign completo. Evitar bucle infinito
     */
    @OneToMany(mappedBy = "deskInBooking")
    @JsonBackReference(value = "desk_bookings")
    private List<Booking> bookings;
}
