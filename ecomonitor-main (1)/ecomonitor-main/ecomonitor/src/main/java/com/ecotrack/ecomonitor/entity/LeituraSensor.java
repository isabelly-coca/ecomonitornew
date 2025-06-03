package com.ecotrack.ecomonitor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa uma leitura realizada por um sensor em uma estação.
 * Cada leitura contém um valor medido, unidade, timestamp e indica se gerou alerta.
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa uma leitura realizada por um sensor")
public class LeituraSensor {

    /**
     * Identificador único da leitura.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da leitura", example = "1")
    private Long id;

    /**
     * Sensor que realizou esta leitura.
     */
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    @Schema(description = "Sensor responsável pela leitura")
    private Sensor sensor;

    /**
     * Valor medido pelo sensor.
     */
    @Schema(description = "Valor medido pelo sensor", example = "23.5")
    private float valor;

    /**
     * Unidade de medida do valor medido (ex: °C, ppm, %).
     */
    @Schema(description = "Unidade de medida do valor medido", example = "°C")
    private String unidade;

    /**
     * Status da leitura (ex: OK, ERROR, ALERT).
     */
    @Schema(description = "Status da leitura", example = "OK")
    private String status;

    /**
     * Data e hora em que a leitura foi realizada.
     */
    @Schema(description = "Data e hora em que a leitura foi realizada", example = "2025-06-03T15:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Indica se a leitura gerou um alerta (true = alerta ativo).
     */
    @Schema(description = "Indica se a leitura gerou um alerta", example = "false")
    private boolean alerta;
}
