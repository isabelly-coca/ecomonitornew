package com.ecotrack.ecomonitor.controller;

import com.ecotrack.ecomonitor.entity.LeituraSensor;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import com.ecotrack.ecomonitor.service.LeituraSensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@Tag(name = "Leituras de Sensor", description = "Operações relacionadas às leituras dos sensores das estações")
public class LeituraSensorController {

    private LeituraSensorService leituraSensorService;

    public LeituraSensorController(LeituraSensorService leituraSensorService) {
        this.leituraSensorService = leituraSensorService;
    }

    @Operation(
            summary = "Registra uma nova leitura de sensor",
            description = "Recebe uma leitura e a salva associada ao sensor correspondente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Leitura registrada com sucesso", content = @Content(schema = @Schema(implementation = LeituraSensor.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para a leitura")
            }
    )
    @PostMapping("/registrar")
    public ResponseEntity<LeituraSensor> registrarLeitura(@RequestBody LeituraSensor leitura) {
        LeituraSensor leituraSalva = leituraSensorService.registrarLeitura(leitura);
        return ResponseEntity.status(HttpStatus.CREATED).body(leituraSalva);
    }

    @Operation(
            summary = "Lista leituras por sensor",
            description = "Retorna todas as leituras registradas para o sensor informado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de leituras retornada", content = @Content(schema = @Schema(implementation = LeituraSensor.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor não encontrado ou sem leituras")
            }
    )
    @GetMapping("/buscar/sensor/{sensorId}")
    public ResponseEntity<List<LeituraSensor>> listarPorSensor(
            @Parameter(description = "ID do sensor", required = true)
            @PathVariable Long sensorId) {
        return ResponseEntity.ok(leituraSensorService.listarPorSensor(sensorId));
    }

    @Operation(
            summary = "Filtra leituras por estação, tipo e período",
            description = "Retorna leituras de sensores filtradas pelo ID da estação, tipo do sensor e intervalo de datas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leituras filtradas retornadas", content = @Content(schema = @Schema(implementation = LeituraSensor.class))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @GetMapping("/filtrar")
    public ResponseEntity<List<LeituraSensor>> filtrarPorEstacaoTipoPeriodo(
            @Parameter(description = "ID da estação", required = true)
            @RequestParam Long estacaoId,
            @Parameter(description = "Tipo do sensor", required = true)
            @RequestParam TipoSensor tipo,
            @Parameter(description = "Data e hora de início no formato ISO", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Data e hora de fim no formato ISO", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        List<LeituraSensor> leituras = leituraSensorService.listarPorEstacaoTipoPeriodo(estacaoId, tipo, inicio, fim);
        return ResponseEntity.ok(leituras);
    }

    @Operation(
            summary = "Obtém estatísticas das leituras",
            description = "Retorna estatísticas (média, máximo, mínimo) das leituras de sensores para uma estação, tipo e período especificados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas", content = @Content(schema = @Schema(implementation = LeituraSensorService.EstatisticaLeituraDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @GetMapping("/estatisticas")
    public ResponseEntity<LeituraSensorService.EstatisticaLeituraDTO> getEstatisticas(
            @Parameter(description = "ID da estação", required = true)
            @RequestParam Long estacaoId,
            @Parameter(description = "Tipo do sensor", required = true)
            @RequestParam TipoSensor tipo,
            @Parameter(description = "Data e hora de início no formato ISO", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Data e hora de fim no formato ISO", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        LeituraSensorService.EstatisticaLeituraDTO estatisticas = leituraSensorService.obterEstatisticas(estacaoId, tipo, inicio, fim);
        return ResponseEntity.ok(estatisticas);
    }

    @Operation(
            summary = "Obtém a média geral das leituras de uma estação",
            description = "Calcula e retorna a média geral das leituras de todos os sensores de uma estação específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Média calculada e retornada", content = @Content(schema = @Schema(implementation = Double.class))),
                    @ApiResponse(responseCode = "404", description = "Estação não encontrada")
            }
    )
    @GetMapping("/media-geral/estacao/{estacaoId}")
    public ResponseEntity<Double> obterMediaGeral(
            @Parameter(description = "ID da estação", required = true)
            @PathVariable Long estacaoId) {
        return ResponseEntity.ok(leituraSensorService.obterMediaGeralPorEstacao(estacaoId));
    }
}

