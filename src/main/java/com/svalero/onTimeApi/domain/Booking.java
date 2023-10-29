package com.svalero.onTimeApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

import static com.svalero.onTimeApi.Util.Literal.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = BOOKING)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    @NotBlank(message = LITERAL_NOT_BLANK)
    private Date day;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    @NotBlank(message = LITERAL_NOT_BLANK)
    private Time inBooking;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    @NotBlank(message = LITERAL_NOT_BLANK)
    private Time outBooking;

    /**
     * Siempre en las N:1 (ManyToOne se define la clave ajena en el lado N (Many)
     * Para relacionar una reserva con un usuario
     * @ManyToOne: Muchas reservas asociados a un usuario N:1
     * @JoinColumn(name = "user_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInBooking;

}
