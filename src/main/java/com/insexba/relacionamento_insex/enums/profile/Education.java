package com.insexba.relacionamento_insex.enums.profile;

import lombok.Getter;

@Getter
public enum Education {
    Ensino_Fundamental("Ensino Fundamental"),
    Ensino_Médio("Ensino Médio"),
    Ensino_Superior("Ensino Superior"),
    Pós_graduação("Pós-graduação");

    private final String description;

    // Construtor
    Education(String description) {
        this.description = description;
    }

}
