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
			view.showOneSystem(s);
			view.ShowSensorsOfSystem(sensorService.getSensorsOfSystem(id));
		} else {
			view.showOneSystemError("System not found.", null);
		}
	}
	
	public void addSystem(SystemEntity system) {
		systemService.create(system);
		view.onSystemAdded(system);
	}
	
	public void removeSystem(SystemEntity system) {
		systemService.delete(system.getId());
		view.onSystemRemoved(system);
	}
	
	public void addSensor(SensorEntity sensor) {
		sensorService.create(sensor);
		view.onSensorAdded(sensor);
	}
	
	public void removeSensor(SensorEntity sensor) {
		sensorService.delete(sensor.getId());
		view.onSensorRemoved(sensor);
	}
}
