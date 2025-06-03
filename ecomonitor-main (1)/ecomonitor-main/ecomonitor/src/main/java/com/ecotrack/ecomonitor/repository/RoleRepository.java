package com.ecotrack.ecomonitor.repository;

import com.ecotrack.ecomonitor.entity.Role;
import com.ecotrack.ecomonitor.entity.enums.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByPermissao(Permissao permissao);
}
