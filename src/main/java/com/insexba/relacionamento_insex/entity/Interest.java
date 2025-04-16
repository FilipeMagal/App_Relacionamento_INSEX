package com.insexba.relacionamento_insex.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    public Interest (String name){
        this.name = name;
    }


    // Opcional, só se quiser ver os usuários que têm esse interesse
    @ManyToMany(mappedBy = "interests")
    private List<User> users = new ArrayList<>();
}
