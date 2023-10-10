package com.fabian57fabian.app.controller;

import com.fabian57fabian.app.view.IotView;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.service.SystemService;

public class SystemsManagerController {
	private SystemService systemService;
	private IotView view;

	public SystemsManagerController(SystemService systemService, IotView view) {
		this.systemService = systemService;
		this.view = view;
	}

	public void viewAllSystems() {
		view.showSystems(systemService.getAllSystems());
	}

	public void expandOneSystem(int id) {
		SystemEntity s = systemService.getSystem(id);
		if (s != null) {
			view.showOneSystem(s);
		} else {
			view.showOneSystemError("System not found.", null);
		}
	}

}
