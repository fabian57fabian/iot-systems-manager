package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SensorEntity;

public interface ISensorService {
	public List<SensorEntity> getSensorNames();

	public SensorEntity getSensorById(int id);
	
	public List<SensorEntity> getSensorsOfSystem(int id);

	void create(SensorEntity sensor);
	
	void delete(int id);
}
