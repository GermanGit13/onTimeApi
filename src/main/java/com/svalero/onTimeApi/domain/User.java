package com.svalero.onTimeApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.onTimeApi.Util.Literal.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = LITERAL_NOT_BLANK)
    @NotNull(message = LITERAL_NOT_NULL)
    @Size(min = 3, max = 9)
    private String username;

    @NotBlank(message = LITERAL_NOT_BLANK)
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




}
