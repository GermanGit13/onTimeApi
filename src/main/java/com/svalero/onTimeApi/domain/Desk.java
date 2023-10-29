package com.svalero.onTimeApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
