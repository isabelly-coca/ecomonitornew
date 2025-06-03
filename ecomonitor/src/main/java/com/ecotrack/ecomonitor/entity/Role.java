package com.ecotrack.ecomonitor.entity;

import com.ecotrack.ecomonitor.entity.enums.Permissao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Schema(name = "Role", description = "Representa uma função ou papel associado a um usuário, com permissões específicas no sistema.")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da role", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Permissão associada à role", example = "ROLE_ADMIN")
    private Permissao permissao;

    public Role() {}

}

