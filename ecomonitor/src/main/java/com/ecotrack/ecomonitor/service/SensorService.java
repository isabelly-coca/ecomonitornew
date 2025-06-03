package com.ecotrack.ecomonitor.service;

import com.ecotrack.ecomonitor.entity.LeituraSensor;
import com.ecotrack.ecomonitor.entity.Sensor;
import com.ecotrack.ecomonitor.entity.enums.Status;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import com.ecotrack.ecomonitor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorService {

    private SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public Sensor CadastrarSensor(Sensor sensor) {

        if(!sensor.getLeituras().isEmpty()){
            for (LeituraSensor leituras : sensor.getLeituras()) {
                leituras.setSensor(sensor);
            }
        }

        return sensorRepository.save(sensor);
    }

    public List<Sensor> listarSensores(){
        return sensorRepository.findAll();
    }

    public Sensor buscarSensor(Long id){
        return sensorRepository.findById(id).orElse(null);
    }

    public List<Sensor> buscarSensoresPorTipo(TipoSensor tipo){
        return sensorRepository.findByTipo(tipo);
    }

    public void removerSensor(Long id){
        sensorRepository.deleteById(id);
    }




    public void desativarSensorAleatorio(){
        List<Sensor> sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            System.out.println("Nenhum sensor encontrado para ser desativado!");
            return;
        }

        Sensor sensorAleatorio = sensores.get((int) (Math.random() * sensores.size()));
        sensorAleatorio.setStatus(Status.INATIVA);
        System.out.println("Sensor " + sensorAleatorio.getNome() + ", ID = " + sensorAleatorio.getId() + " foi desativado!");
    }
}
