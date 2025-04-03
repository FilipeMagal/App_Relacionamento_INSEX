package com.insexba.relacionamento_insex.entity;

import com.insexba.relacionamento_insex.enums.Gender;
import com.insexba.relacionamento_insex.enums.TypeUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_api")
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    // Dados pessoais

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private TypeUser typeUser;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String ethnicity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false, unique = true)
    private String email;

}
