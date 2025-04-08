    package com.insexba.relacionamento_insex.entity;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.insexba.relacionamento_insex.enums.user.Gender;
    import com.insexba.relacionamento_insex.enums.user.TypeUser;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.time.Period;
    import java.util.Date;
    import java.util.List;

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

        @Column(name = "birth_data", nullable = false)
        @Temporal(TemporalType.DATE)
        private Date birth_Data;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Gender gender;

        @Enumerated(EnumType.STRING)
        private TypeUser typeUser;

        @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
        private Profile profile;

        // Metodo para calcular a idade
        public int getAge() {
            LocalDate today = LocalDate.now();
            LocalDate birthDate = new java.sql.Date(birth_Data.getTime()).toLocalDate();
            return Period.between(birthDate, today).getYears();
        }




    }
