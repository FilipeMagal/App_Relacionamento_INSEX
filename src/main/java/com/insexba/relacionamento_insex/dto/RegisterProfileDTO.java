package com.insexba.relacionamento_insex.dto;

import com.insexba.relacionamento_insex.enums.profile.Desired_Relationship;
import com.insexba.relacionamento_insex.enums.profile.Education;
import com.insexba.relacionamento_insex.enums.profile.Ethnicity;
import com.insexba.relacionamento_insex.enums.profile.MaritalStatus;
import com.insexba.relacionamento_insex.enums.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProfileDTO {

    private Integer id;

    private Ethnicity ethnicity;

    private Education education;

    private MaritalStatus maritalStatus;

    private Desired_Relationship desiredRelationship;

    private String bio;

    private String profession;

    private Integer userId; // Relacionamento com o User, mas só com o ID do usuário

    // Campos adicionais de dados pessoais
    private String firstName;
    private String lastName;
    private int age;
    private Gender gender;  // Adicionado o campo de gênero
    private String birth_Data;


}
