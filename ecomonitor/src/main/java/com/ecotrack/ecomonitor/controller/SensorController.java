package com.ecotrack.ecomonitor.controller;

import com.ecotrack.ecomonitor.entity.Sensor;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import com.ecotrack.ecomonitor.service.EstacaoService;
import com.ecotrack.ecomonitor.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensores")
@Tag(name = "Sensores", description = "Operações para cadastro, listagem e gerenciamento de sensores")
public class SensorController {

    private final EstacaoService estacaoService;
    private SensorService sensorService;

    public SensorController(SensorService sensorService, EstacaoService estacaoService) {
        this.sensorService = sensorService;
        this.estacaoService = estacaoService;
    }

    @Operation(
            summary = "Cadastra um novo sensor",
            description = "Recebe os dados do sensor e realiza o cadastro.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sensor cadastrado com sucesso", content = @Content(schema = @Schema(implementation = Sensor.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro do sensor")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<Sensor> Cadastrar(
            @Parameter(description = "Objeto sensor para cadastro", required = true)
            @RequestBody Sensor sensor) {

        Sensor sensorSalvo = sensorService.CadastrarSensor(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorSalvo);
    }

    @Operation(
            summary = "Lista todos os sensores",
            description = "Retorna a lista de sensores cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de sensores retornada", content = @Content(schema = @Schema(implementation = Sensor.class))),
                    @ApiResponse(responseCode = "204", description = "Nenhum sensor cadastrado")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<List<Sensor>> Listar() {
        List<Sensor> sensores = sensorService.listarSensores();

        if (sensores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(sensores);
    }

    @Operation(
            summary = "Busca sensor por ID",
            description = "Retorna o sensor correspondente ao ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor encontrado", content = @Content(schema = @Schema(implementation = Sensor.class))),
                    @ApiResponse(responseCode = "204", description = "Sensor não encontrado")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Sensor> buscarPorId(
            @Parameter(description = "ID do sensor", required = true)
            @PathVariable Long id) {

        Sensor sensor = sensorService.buscarSensor(id);
        if (sensor == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(sensor);
    }

    @Operation(
            summary = "Busca sensores por tipo",
            description = "Retorna uma lista de sensores filtrados pelo tipo informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de sensores retornada", content = @Content(schema = @Schema(implementation = Sensor.class))),
                    @ApiResponse(responseCode = "204", description = "Nenhum sensor encontrado para o tipo informado")
            }
    )
    @GetMapping("/buscar/tipo_sensor/{tipoSensor}")
    public ResponseEntity<List<Sensor>> buscarPorTipoSensor(
            @Parameter(description = "Tipo do sensor", required = true)
            @PathVariable TipoSensor tipoSensor) {

        List<Sensor> sensores = sensorService.buscarSensoresPorTipo(tipoSensor);
        if (sensores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(sensores);
    }

    @Operation(
            summary = "Remove um sensor por ID",
            description = "Remove o sensor identificado pelo ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sensor removido com sucesso ou não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Sensor> Deletar(
            @Parameter(description = "ID do sensor a ser removido", required = true)
            @PathVariable Long id) {

        Sensor sensor = sensorService.buscarSensor(id);
        if (sensor == null) {
            return ResponseEntity.noContent().build();
        }
        sensorService.removerSensor(id);

        return ResponseEntity.noContent().build();
    }
}
