    package com.insexba.relacionamento_insex.entity;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.insexba.relacionamento_insex.enums.user.Gender;
    import com.insexba.relacionamento_insex.enums.user.TypeUser;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDate;
    import java.time.Period;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Date;
    import java.util.List;

    @Table(name = "user_api")
    @Entity
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public class User implements UserDetails {

        // Dados pessoais


        public User(String firstName, String lastName, String password,  String email, Date birthData, Gender gender, TypeUser typeUser) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.password = password;
            this.email = email;
            this.birthData = birthData;
            this.typeUser = typeUser;
        }




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
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date birthData;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Gender gender;

        @Enumerated(EnumType.STRING)
        private TypeUser typeUser = TypeUser.Usuario;

        @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
        private Profile profile;

        // Metodo para calcular a idade
        public int getAge() {
            LocalDate today = LocalDate.now();
            LocalDate birthDate = new java.sql.Date(birthData.getTime()).toLocalDate();
            return Period.between(birthDate, today).getYears();
        }

        @PrePersist
        public void ensureTypeUser() {
            // Se typeUser for null, atribui TypeUser.Usuario
            if (this.typeUser == null) {
                this.typeUser = TypeUser.Usuario;
            }
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinTable(
                name = "user_interest",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "interest_id")
        )
        private List<Interest> interests = new ArrayList<>();


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            if (this.typeUser == TypeUser.Administrador) return List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"), new SimpleGrantedAuthority("ROLE_USUARIO"));
            else return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
