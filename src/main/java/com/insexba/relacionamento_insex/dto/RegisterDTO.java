package com.insexba.relacionamento_insex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;

import java.util.Date;

public record RegisterDTO(String firstName, String lastName, String password,  String email, Date birthData, Gender gender, TypeUser user) {

}
