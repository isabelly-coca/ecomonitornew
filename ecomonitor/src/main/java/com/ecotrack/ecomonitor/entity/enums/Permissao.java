package com.ecotrack.ecomonitor.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permissao {

    ROLE_ADMIN(1, "Administrador"),
    ROLE_USER(2, "Usuário");

    private final Integer id;
    private final String descricao;
}

