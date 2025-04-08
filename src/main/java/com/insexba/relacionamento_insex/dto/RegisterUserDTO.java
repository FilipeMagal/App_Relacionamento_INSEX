package com.insexba.relacionamento_insex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth_Data;
    private Gender gender;
    private TypeUser typeUser;
    private int agr;




}
