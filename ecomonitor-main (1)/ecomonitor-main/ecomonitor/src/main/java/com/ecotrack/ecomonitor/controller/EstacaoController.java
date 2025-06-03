package com.ecotrack.ecomonitor.controller;

import com.ecotrack.ecomonitor.entity.Estacao;
import com.ecotrack.ecomonitor.entity.enums.Status;
import com.ecotrack.ecomonitor.service.EstacaoService;
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
@RequestMapping("/api/estacoes")
@Tag(name = "Estações", description = "Gerenciamento das estações de monitoramento ambiental")
public class EstacaoController {

    private EstacaoService estacaoService;

    public EstacaoController(EstacaoService estacaoService) {
        this.estacaoService = estacaoService;
    }

    @Operation(
            summary = "Cria uma nova estação",
            description = "Recebe os dados da estação e salva no banco de dados, retornando a estação criada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estação criada com sucesso", content = @Content(schema = @Schema(implementation = Estacao.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            }
    )
    @PostMapping("/criar")
    public ResponseEntity<Estacao> criar(@RequestBody Estacao estacao) {
        Estacao estacaoSalva = estacaoService.cadastrarEstacao(estacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(estacaoSalva);
    }

    @Operation(
            summary = "Lista todas as estações",
            description = "Retorna uma lista com todas as estações cadastradas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de estações retornada", content = @Content(schema = @Schema(implementation = Estacao.class))),
                    @ApiResponse(responseCode = "204", description = "Nenhuma estação encontrada")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<List<Estacao>> listarTodas() {
        List<Estacao> estacoes = estacaoService.listarTodas();
        if (estacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estacoes);
    }

    @Operation(
            summary = "Busca uma estação pelo ID",
            description = "Retorna a estação correspondente ao ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estação encontrada", content = @Content(schema = @Schema(implementation = Estacao.class))),
                    @ApiResponse(responseCode = "404", description = "Estação não encontrada")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Estacao> buscarPorId(
            @Parameter(description = "ID da estação a ser buscada", required = true)
            @PathVariable Long id) {
        Estacao estacao = estacaoService.buscarPorId(id);
        if (estacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estacao);
    }

    @Operation(
            summary = "Busca estações por status",
            description = "Retorna uma lista de estações filtradas pelo status informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de estações filtrada retornada", content = @Content(schema = @Schema(implementation = Estacao.class))),
                    @ApiResponse(responseCode = "204", description = "Nenhuma estação encontrada com o status informado")
            }
    )
    @GetMapping("/buscar/status/{status}")
    public ResponseEntity<List<Estacao>> buscarPorStatus(
            @Parameter(description = "Status para filtrar as estações", required = true)
            @PathVariable Status status) {
        List<Estacao> estacoes = estacaoService.listarPorStatus(status);
        if (estacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estacoes);
    }

    @Operation(
            summary = "Deleta uma estação pelo ID",
            description = "Remove a estação identificada pelo ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estação deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Estação não encontrada")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Estacao> deletar(
            @Parameter(description = "ID da estação a ser deletada", required = true)
            @PathVariable Long id) {
        Estacao estacao = estacaoService.buscarPorId(id);
        if (estacao == null) {
            return ResponseEntity.notFound().build();
        }
        estacaoService.deletarEstacao(id);
        return ResponseEntity.noContent().build();
    }
}

