package com.insexba.relacionamento_insex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insexba.relacionamento_insex.enums.profile.Desired_Relationship;
import com.insexba.relacionamento_insex.enums.profile.Education;
import com.insexba.relacionamento_insex.enums.profile.Ethnicity;
import com.insexba.relacionamento_insex.enums.profile.MaritalStatus;
import com.insexba.relacionamento_insex.enums.user.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProfileDTO {

    @NotNull(message = "Etnia é obrigatória")
    private Ethnicity ethnicity;

    @NotNull(message = "Escolaridade é obrigatória")
    private Education education;

    @NotNull(message = "Estado civil é obrigatório")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Tipo de relacionamento desejado é obrigatório")
    private Desired_Relationship desiredRelationship;

    @NotBlank(message = "A biografia não pode estar vazia")
    private String bio;

    @NotBlank(message = "Profissão não pode estar vazia")
    private String profession;

    // Campos adicionais de dados pessoais
    @NotBlank(message = "Nome é obrigatório")
    private String firstName;

    @NotBlank(message = "Sobrenome é obrigatório")
    private String lastName;

    @Min(value = 18, message = "Idade mínima permitida é 18 anos")
    private int age;

    @NotNull(message = "Gênero é obrigatório")
    private Gender gender;

    @NotNull(message = "Interesses são obrigatórios")
    @Size(min = 1, message = "Adicione pelo menos um interesse")
    private List<String> interests;

    @NotNull(message = "Foto de perfil obrigatória")
    private MultipartFile profilePicture;

}
