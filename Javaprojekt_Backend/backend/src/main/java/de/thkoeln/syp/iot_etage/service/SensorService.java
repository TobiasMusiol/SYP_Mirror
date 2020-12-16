package de.thkoeln.syp.iot_etage.service;

import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository repository;


    public Optional<SensorData> findById(long id){
            return repository.findById(id);
    }

    public List<SensorData> findAll(){
        return (List<SensorData>) repository.findAllByOrderByDateDesc();
    }

    public boolean insertSensorData(SensorData sensorData){

        if(sensorData!=null) {
            repository.save(sensorData);
            return true;
        }else{
            return false;
        }

    }

    public boolean insertSensorDatas(List<SensorData> sensorDataList){
            repository.saveAll(sensorDataList);
            return true;
    }

}
