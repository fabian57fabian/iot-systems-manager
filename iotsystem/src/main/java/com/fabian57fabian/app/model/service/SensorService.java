package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.repository.SensorRepository;

public class SensorService implements ISensorService{
	private SensorRepository sensorRepository;

	public SensorService(SensorRepository sensorRepository) {
		this.sensorRepository = sensorRepository;
	}

	@Override
	public List<SensorEntity> getSensorNames() {
		return sensorRepository.retrieveSensorsNames();
	}

	@Override
	public SensorEntity getSensorById(int id) {
		return sensorRepository.getSensorById(id);
	}
	
	@Override
	public List<SensorEntity> getSensorsOfSystem(int system_id){
		return sensorRepository.getSensorsOfSystem(system_id);
	}

	@Override
	public Boolean create(SensorEntity sensor) {
		if(sensorRepository.getSensorById(sensor.getId()) != null) {
			return false;
		}
		sensorRepository.save(sensor);	
		return true;
	}

	@Override
	public void delete(int id) {
		sensorRepository.delete(id);		
	}

	@Override
	public void modify(int id, SensorEntity new_sensor) {
		sensorRepository.update(id, new_sensor);		
	}
}
