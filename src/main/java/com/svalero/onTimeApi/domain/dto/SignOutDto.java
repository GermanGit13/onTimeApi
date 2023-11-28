package com.svalero.onTimeApi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignOutDto {

    private LocalTime out_time;
    private String incidende_out;

}
