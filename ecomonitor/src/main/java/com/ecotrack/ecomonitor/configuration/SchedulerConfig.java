package com.ecotrack.ecomonitor.configuration;


import com.ecotrack.ecomonitor.entity.LeituraSensor;
import com.ecotrack.ecomonitor.service.EstacaoService;
import com.ecotrack.ecomonitor.service.LeituraSensorService;
import com.ecotrack.ecomonitor.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private LeituraSensorService leituraService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private EstacaoService estacaoService;

    @Scheduled(fixedDelay = 300000)
    public void scheduleTask() {
        System.out.println("Nova leitura foi gerada!");
        leituraService.criarLeituraAleatoria();
    }

    @Scheduled(fixedDelay = 300000)
    public void scheduleTask2() {
        System.out.println("Sensor desativado!");
        sensorService.desativarSensorAleatorio();
    }

    @Scheduled(fixedDelay = 600000)
    public void scheduleTask3() {
        System.out.println("Sensor desativado!");
        sensorService.desativarSensorAleatorio();
    }

    @Scheduled(fixedDelay = 120000)
    public void scheduleTask4() {
        estacaoService.verificarEstacoesInativas();
    }
}
