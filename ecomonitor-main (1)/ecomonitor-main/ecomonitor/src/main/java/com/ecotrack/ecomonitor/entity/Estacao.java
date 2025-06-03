package com.ecotrack.ecomonitor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import com.ecotrack.ecomonitor.entity.enums.Status;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Representa uma estação de monitoramento ambiental")
public class Estacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da estação", example = "1")
    private Long id;

    @Schema(description = "Nome descritivo da estação", example = "Estação Central")
    private String nome;

    @Schema(description = "Latitude geográfica da estação", example = "-23.55052")
    private double latitude;

    @Schema(description = "Longitude geográfica da estação", example = "-46.633308")
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual da estação", example = "ATIVO")
    private Status status;

    @Schema(description = "Data de instalação da estação", example = "2024-01-15")
    private LocalDate dataInstalacao;

    @Schema(description = "Data e hora da inativação da estação, se houver", example = "2025-05-20T10:30:00")
    private LocalDateTime dataInativacao;

    @OneToMany(mappedBy = "estacao", cascade = CascadeType.ALL)
    @Schema(description = "Sensores vinculados a esta estação")
    private List<Sensor> sensores;
}
