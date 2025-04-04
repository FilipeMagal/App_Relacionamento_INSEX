    package com.insexba.relacionamento_insex.entity;

    import com.insexba.relacionamento_insex.enums.user.Gender;
    import com.insexba.relacionamento_insex.enums.user.TypeUser;
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

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private int age;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Gender gender;

        @Enumerated(EnumType.STRING)
        private TypeUser typeUser;



    }
