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
	public void create(SensorEntity sensor) {
		sensorRepository.save(sensor);	
	}

	@Override
	public void delete(int id) {
		sensorRepository.delete(id);		
	}
}
