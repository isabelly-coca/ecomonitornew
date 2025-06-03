package com.ecotrack.ecomonitor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Schema(name = "Usuario", description = "Representa um usuário do sistema com credenciais e permissões para acesso.")
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "Nome de usuário para login", example = "joaosilva")
    private String username;

    @Schema(description = "Senha do usuário (armazenada de forma segura)", example = "senha123")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @Schema(description = "Lista de papéis (roles) associados ao usuário, definindo suas permissões")
    private List<Role> listRoles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.listRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getPermissao().toString()))
                .collect(Collectors.toList());
    }

    // Os demais métodos de UserDetails (isAccountNonExpired, etc.) podem ser implementados aqui se desejar documentar também.
}

