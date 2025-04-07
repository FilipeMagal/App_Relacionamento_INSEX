package com.insexba.relacionamento_insex.entity;

import com.insexba.relacionamento_insex.enums.profile.Desired_Relationship;
import com.insexba.relacionamento_insex.enums.profile.Education;
import com.insexba.relacionamento_insex.enums.profile.Ethnicity;
import com.insexba.relacionamento_insex.enums.profile.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "profile_api")
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Ethnicity ethnicity;

    @Enumerated(EnumType.STRING)
    private Education education;

    @Enumerated(EnumType.STRING)
    private MaritalStatus marital_Status;

    @Enumerated(EnumType.STRING)
    private Desired_Relationship desired_Relationship;

    private String bio;

    private String profession;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;

    //Falta a foto de perfil

}
