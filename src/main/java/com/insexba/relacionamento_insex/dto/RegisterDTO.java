package com.insexba.relacionamento_insex.dto;

import com.insexba.relacionamento_insex.enums.Gender;
import com.insexba.relacionamento_insex.enums.TypeUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private int age;
    private String ethnicity;
    private Gender gender;
    private String bio;
    private String email;
    private TypeUser typeUser;
    private String password;
}
