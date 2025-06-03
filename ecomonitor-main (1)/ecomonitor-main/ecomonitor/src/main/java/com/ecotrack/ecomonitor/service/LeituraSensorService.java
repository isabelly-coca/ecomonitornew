package com.ecotrack.ecomonitor.service;

import com.ecotrack.ecomonitor.entity.LeituraSensor;
import com.ecotrack.ecomonitor.entity.Sensor;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import com.ecotrack.ecomonitor.repository.LeituraSensorRepository;
import com.ecotrack.ecomonitor.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeituraSensorService {

    private LeituraSensorRepository leituraSensorRepository;
    private final SensorRepository sensorRepository;

    public LeituraSensorService(LeituraSensorRepository leituraSensorRepository, SensorRepository sensorRepository) {
        this.leituraSensorRepository = leituraSensorRepository;
        this.sensorRepository = sensorRepository;
    }

    public LeituraSensor registrarLeitura(LeituraSensor leitura) {
        Sensor sensor = leitura.getSensor();
        if (sensor == null) {
            throw new RuntimeException("Estação não encontrada");
        }

        leitura.setTimestamp(LocalDateTime.now());

        // Lógica de alerta simples
        if (isValorForaDoPadrao(leitura)) {
            leitura.setAlerta(true);
            throw new RuntimeException(("Valor fora do intervalo esperado para " + leitura.getSensor().getTipo())
                    + (" Timestamp: " + leitura.getTimestamp())
                    + (" Sensor: " + leitura.getSensor()));
        }

        return leituraSensorRepository.save(leitura);
    }

    public List<LeituraSensor> listarPorEstacaoTipoPeriodo(Long estacaoId, TipoSensor tipo, LocalDateTime inicio, LocalDateTime fim) {
        return leituraSensorRepository.findByEstacaoIdAndSensorTipoAndTimestampBetween(estacaoId, tipo, inicio, fim);
    }

    public List<LeituraSensor> listarPorSensor(Long sensorId) {
        return leituraSensorRepository.findBySensorId(sensorId);
    }






    public void criarLeituraAleatoria() {
        List<Sensor> sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            System.out.println("Nenhum sensor encontrado para gerar leitura.");
            return;
        }

        Sensor sensorAleatorio = sensores.get((int) (Math.random() * sensores.size()));
        TipoSensor tipo = sensorAleatorio.getTipo();

        float valorAleatorio;
        String unidade;

        switch (tipo) {
            case TEMPERATURA -> {
                valorAleatorio = (float) (-10 + Math.random() * 60);
                unidade = "°C";
            }
            case UMIDADE -> {
                valorAleatorio = (float) (10 + Math.random() * 80);
                unidade = "%";
            }
            case CO2 -> {
                valorAleatorio = (float) (300 + Math.random() * 1000);
                unidade = "ppm";
            }
            case RUIDO -> {
                valorAleatorio = (float) (30 + Math.random() * 70);
                unidade = "dB";
            }
            default -> {
                valorAleatorio = 0;
                unidade = "";
            }
        }

        LeituraSensor leitura = new LeituraSensor();
        leitura.setSensor(sensorAleatorio);
        leitura.setValor(valorAleatorio);
        leitura.setUnidade(unidade);
        leitura.setTimestamp(LocalDateTime.now());
        leitura.setAlerta(isValorForaDoPadrao(leitura));

        leituraSensorRepository.save(leitura);
        System.out.println("Uma nova leitura foi gerada pelo sensor: " + sensorAleatorio.getNome() + " ID = " + sensorAleatorio.getId());
    }

    private boolean isValorForaDoPadrao(LeituraSensor leitura) {
        float valor = leitura.getValor();
        return switch (leitura.getSensor().getTipo()) {
            case TEMPERATURA -> valor < -10 || valor > 45;
            case UMIDADE -> valor < 10 || valor > 90;
            case CO2 -> valor > 1000;
            case RUIDO -> valor > 85;
        };
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class EstatisticaLeituraDTO {
        private Double media;
        private Float min;
        private Float max;
    }

    public EstatisticaLeituraDTO obterEstatisticas(Long estacaoId, TipoSensor tipo, LocalDateTime inicio, LocalDateTime fim) {
        Object[] stats = leituraSensorRepository.findStatsByEstacaoAndTipoAndPeriodo(estacaoId, tipo, inicio, fim);
        if (stats == null || stats[0] == null) {
            throw new RuntimeException("Nenhuma leitura encontrada para os critérios fornecidos!");
        }
        return new EstatisticaLeituraDTO(
                (Double) stats[0],
                (Float) stats[1],
                (Float) stats[2]
        );
    }

    public Double obterMediaGeralPorEstacao(Long estacaoId) {
        Double media = leituraSensorRepository.findMediaLeiturasPorEstacao(estacaoId);
        if (media == null) {
            throw new RuntimeException("Nenhuma leitura encontrada para esta estação!");
        }
        return media;
    }
}
