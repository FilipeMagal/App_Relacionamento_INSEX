package com.insexba.relacionamento_insex.enums;

import lombok.Getter;

@Getter
public enum TypeUser {
    Usuario ("Usuário"),
    Administradora ("Administradora"),
    Tecnico ("Técnico");

    private final String type;

    TypeUser(String type) {
        this.type = type;
    }
}
