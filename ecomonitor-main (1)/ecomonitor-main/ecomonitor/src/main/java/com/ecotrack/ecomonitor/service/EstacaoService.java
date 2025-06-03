package com.ecotrack.ecomonitor.service;

import com.ecotrack.ecomonitor.entity.Estacao;
import com.ecotrack.ecomonitor.entity.Sensor;
import com.ecotrack.ecomonitor.entity.enums.Status;
import com.ecotrack.ecomonitor.repository.EstacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável por conter as regras de negócio relacionadas à entidade Estacao.
 */
@Service
public class EstacaoService {

    private EstacaoRepository estacaoRepository;

    // Construtor com injeção do repositório de estação
    public EstacaoService(EstacaoRepository estacaoRepository) {
        this.estacaoRepository = estacaoRepository;
    }

    /**
     * Cadastra uma nova estação no sistema, associando os sensores e preenchendo status e data de instalação.
     * @param estacao Estação a ser cadastrada
     * @return Estação salva no banco de dados
     */
    @Transactional
    public Estacao cadastrarEstacao(Estacao estacao) {
        if (!estacao.getSensores().isEmpty()) {
            for (Sensor sensor : estacao.getSensores()) {
                // Associa a estação ao sensor
                sensor.setEstacao(estacao);
            }
        }

        estacao.setStatus(Status.ATIVA); // Define como ativa por padrão
        estacao.setDataInstalacao(LocalDate.now()); // Data atual

        return estacaoRepository.save(estacao);
    }

    /**
     * Lista todas as estações cadastradas.
     * @return Lista de estações
     */
    public List<Estacao> listarTodas() {
        return estacaoRepository.findAll();
    }

    /**
     * Busca uma estação pelo ID.
     * @param id Identificador da estação
     * @return Estação correspondente ou null
     */
    public Estacao buscarPorId(Long id) {
        return estacaoRepository.findById(id).orElse(null);
    }

    /**
     * Lista estações filtrando por status (ATIVA ou INATIVA).
     * @param status Status desejado
     * @return Lista de estações com o status informado
     */
    public List<Estacao> listarPorStatus(Status status) {
        return estacaoRepository.findByStatus(status);
    }

    /**
     * Deleta uma estação com base no ID.
     * @param id Identificador da estação
     */
    public void deletarEstacao(Long id) {
        estacaoRepository.deleteById(id);
    }

    /**
     * Desativa aleatoriamente uma estação cadastrada e registra a data de inativação.
     * Apenas para fins de simulação ou teste.
     */
    public void desativarEstacaoAleatorio() {
        List<Estacao> estacoes = estacaoRepository.findAll();
        if (estacoes.isEmpty()) {
            System.out.println("Nenhuma estação encontrada para ser desativada!");
            return;
        }

        // Seleciona uma estação aleatória da lista
        Estacao estacaoAleatoria = estacoes.get((int) (Math.random() * estacoes.size()));
        estacaoAleatoria.setStatus(Status.INATIVA);
        estacaoAleatoria.setDataInativacao(LocalDateTime.now());

        System.out.println("Estação " + estacaoAleatoria.getNome() +
                ", ID = " + estacaoAleatoria.getId() + " foi desativada!");
    }

    /**
     * Verifica quais estações estão inativas há mais de 2 minutos e emite alerta no console.
     * Pode ser utilizado com agendador para verificações periódicas.
     */
    public void verificarEstacoesInativas() {
        List<Estacao> inativas = listarPorStatus(Status.INATIVA);

        for (Estacao estacao : inativas) {
            if (estacao.getDataInativacao() != null) {
                Duration tempo = Duration.between(estacao.getDataInativacao(), LocalDateTime.now());

                if (tempo.toMinutes() >= 2) {
                    System.out.println("ALERTA: Estação " + estacao.getNome() +
                            ", ID = " + estacao.getId() + " está inativa há " +
                            tempo.toMinutes() + " minutos. ISSO É INACEITÁVEL!");

                    System.out.println(">> Enviando informação ao serviço de manutenção externa...\n");
                }
            }
        }
    }
}
