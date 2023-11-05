package com.svalero.onTimeApi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class onTimeConfig {

    /**
     * @Configuration: Clase de configuración para que tenga acceso desde cuaqluier parte del proyecto.
     * @Bean: Se usa junto con @Configuration -> para los métodos que creamos y queremos usar en el proyecto.
     * ModelMapper: Libreria para mapear campos entre objetos.
     * BCryptPasswordEncoder: Librería para encriptar y comparar las contraseñas
     */

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
