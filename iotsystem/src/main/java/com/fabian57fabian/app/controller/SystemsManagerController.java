package com.fabian57fabian.app.controller;

import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.service.ISensorService;
import com.fabian57fabian.app.model.service.ISystemService;
import com.fabian57fabian.ui.view.IotView;

public class SystemsManagerController {
	private ISystemService systemService;
	private ISensorService sensorService;
	private IotView view;

	public SystemsManagerController(ISystemService systemService, ISensorService sensorService, IotView view) {
		this.systemService = systemService;
		this.sensorService = sensorService;
		this.view = view;
	}

	public void viewAllSystems() {
		view.showSystems(systemService.getSystemNames());
	}

	public void expandOneSystem(int id) {
		SystemEntity s = systemService.getSystemById(id);
		if (s != null) {
			view.showSensorsOfSystem(sensorService.getSensorsOfSystem(id));
		} else {
			view.showOneSystemError("System not found.", null);
		}
	}

	public void addSystem(SystemEntity system) {
		Boolean res = systemService.create(system);
		if (Boolean.FALSE.equals(res)) {
			view.showOneSystemError("System with same id already exists.", null);
		} else {
			view.onSystemAdded(system);
		}
	}

	public void removeSystem(SystemEntity system) {
		systemService.delete(system.getId());
		view.onSystemRemoved(system);
	}

	public void addSensor(SensorEntity sensor) {
		Boolean res = sensorService.create(sensor);
		if (Boolean.FALSE.equals(res)) {
			view.showOneSensorError("Sensor with same id already exists.", null);
		} else {
			view.onSensorAdded(sensor);
		}
	}

	public void removeSensor(SensorEntity sensor) {
		sensorService.delete(sensor.getId());
		view.onSensorRemoved(sensor);
	}

	public void calibrateSensor(SensorEntity sensor, Double newOffset, Double newMultiplier) {
		sensorService.modify(sensor.getId(), new SensorEntity(sensor.getId(), sensor.getName(), sensor.getDescription(), sensor.getUnit(), newOffset, newMultiplier, sensor.getSystemId()));
		// Refresh view
		view.showSensorsOfSystem(sensorService.getSensorsOfSystem(sensor.getSystemId()));
	}
}
