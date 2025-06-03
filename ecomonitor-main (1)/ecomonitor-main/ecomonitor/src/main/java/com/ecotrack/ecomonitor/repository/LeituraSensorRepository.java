package com.ecotrack.ecomonitor.repository;

import com.ecotrack.ecomonitor.entity.LeituraSensor;
import com.ecotrack.ecomonitor.entity.enums.TipoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findByEstacaoIdAndSensorTipoAndTimestampBetween(
            Long estacaoId, TipoSensor tipo, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT AVG(l.valor), MIN(l.valor), MAX(l.valor) FROM LeituraSensor l WHERE l.sensor.estacao.id = :estacaoId AND l.sensor.tipo = :tipo AND l.timestamp BETWEEN :inicio AND :fim")
    Object[] findStatsByEstacaoAndTipoAndPeriodo(@Param("estacaoId") Long estacaoId, @Param("tipo") TipoSensor tipo, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT AVG(l.valor) FROM LeituraSensor l WHERE l.sensor.estacao.id = :estacaoId")
    Double findMediaLeiturasPorEstacao(Long estacaoId);

    List<LeituraSensor> findBySensorId(Long sensorId);
}
