package com.ecotrack.ecomonitor.entity;

import com.ecotrack.ecomonitor.entity.enums.Status;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Sensor", description = "Representa um sensor vinculado a uma estação, responsável por registrar leituras ambientais.")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do sensor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estacao_id")
    @Schema(description = "Estação à qual o sensor está associado")
    private Estacao estacao;

    @Schema(description = "Nome do sensor", example = "Sensor de Temperatura")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual do sensor", example = "ATIVO")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo do sensor", example = "TEMPERATURA")
    private TipoSensor tipo;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    @Schema(description = "Lista de leituras realizadas pelo sensor")
    private List<LeituraSensor> leituras;
}
