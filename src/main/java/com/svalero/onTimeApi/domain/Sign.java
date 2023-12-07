package com.svalero.onTimeApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.svalero.onTimeApi.Util.Literal.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = SIGN)
public class Sign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String modality;

    @Column
    @NotNull(message = LITERAL_NOT_NULL)
    private LocalDate day;

    @Column(name = INTIME)
    private LocalTime in_time;

    @Column(name = OUTTIME)
    private LocalTime out_time;

    @Column(name = INCIDENCEIN)
    private String incidence_in;

    @Column(name = INCIDENCEOUT)
    private String incidence_out;

    /**
     * Siempre en las N:1 (ManyToOne se define la clave ajena en el lado N (Many)
     * Para relacionar un fichaje con un usuario
     * @ManyToOne: Muchos fichajes asociados a un usuario N:1
     * @JoinColumn(name = "user_id") como queremos que se llame la tabla de la relación N:1
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInSign;
}
