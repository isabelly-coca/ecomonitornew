package com.ecotrack.ecomonitor.repository;

import com.ecotrack.ecomonitor.entity.Sensor;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByTipo(TipoSensor tipo);
}
