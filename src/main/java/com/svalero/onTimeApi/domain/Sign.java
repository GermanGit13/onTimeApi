package com.svalero.onTimeApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private LocalDate day;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private Date inTime;

    @Column
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    private Time outTime;

    @Column
    private String incidenceIn;

    @Column
    private String incidendeOut;

}
