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


}
