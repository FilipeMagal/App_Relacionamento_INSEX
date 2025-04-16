package com.insexba.relacionamento_insex.enums.user;

import lombok.Getter;

@Getter
public enum TypeUser {
    Usuario ("Usuário"),
    Administrador ("Administrador");

    private final String role;

    TypeUser(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
