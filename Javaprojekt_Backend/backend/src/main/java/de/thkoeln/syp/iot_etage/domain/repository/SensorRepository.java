package de.thkoeln.syp.iot_etage.domain.repository;

import de.thkoeln.syp.iot_etage.domain.entity.Sensordata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensordataRepository  extends CrudRepository<Sensordata,Long> {
}
