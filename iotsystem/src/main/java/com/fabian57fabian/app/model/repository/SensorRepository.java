package com.fabian57fabian.app.model.repository;

import java.util.List;

import com.fabian57fabian.app.model.entities.SensorEntity;

public interface SensorRepository {
	public List<SensorEntity> retrieveSensorsNames();

	public SensorEntity getSensorById(int id);

	void save(SensorEntity system);

	void delete(int id);
}
