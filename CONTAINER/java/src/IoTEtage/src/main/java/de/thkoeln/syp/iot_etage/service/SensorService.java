package de.thkoeln.syp.iot_etage.service;

import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.utils.SensorDataMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

  @Autowired
  private SensorRepository repository;

  // Konstructor
  public Optional<SensorData> findById(long id) {
    return repository.findById(id);
  }

  // Methoden
  public List<SensorDataDto> findAll() {
    List<SensorDataDto> sensorDataDtos = new ArrayList<>();

    List<SensorData> sensorDatas = repository.findAllByOrderByTimestampDesc();

    sensorDatas.forEach(sensorData -> {
      sensorDataDtos.add(SensorDataMapper.convertSensorDataToSensorDataDto(sensorData));
    });
    
    return sensorDataDtos;
  }

  public boolean insertSensorData(SensorData sensorData) {

    if (sensorData != null) {
      repository.save(sensorData);
      return true;
    } else {
      return false;
    }

  }

  public List<SensorDataDto> insertSensorDatas(List<SensorDataDto> sensorDataDtoList) {
    
    List<SensorData> sensorDataList = new ArrayList<>();

    sensorDataDtoList.forEach(sensorDataDto -> {
      sensorDataList.add(SensorDataMapper.convertSensorDataDtoToSensorData(sensorDataDto));
    });

    List<SensorDataDto> sensorDataDtoListCreated = new ArrayList<>();
    List<SensorData> sensorDataListCreated = (List<SensorData>) repository.saveAll(sensorDataList);
    
    sensorDataListCreated.forEach(sensorData -> {
      sensorDataDtoListCreated.add(SensorDataMapper.convertSensorDataToSensorDataDto(sensorData));
    });

    return sensorDataDtoListCreated;
  }

}
