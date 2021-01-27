package de.thkoeln.syp.iot_etage.domain.repository;

import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends CrudRepository<SensorData,Long> {
    public List<SensorData> findAllByOrderByTimestampDesc();

    public SensorData findTopBySensorTypeOrderByTimestamp(String sensorType);
}
