package com.fabian57fabian.app.controller;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.service.ISystemService;
import com.fabian57fabian.app.view.IotView;

public class SystemsManagerController {
	private ISystemService systemService;
	private IotView view;

	public SystemsManagerController(ISystemService systemService, IotView view) {
		this.systemService = systemService;
		this.view = view;
	}

	public void viewAllSystems() {
		view.showSystems(systemService.getSystemNames());
	}

	public void expandOneSystem(int id) {
		SystemEntity s = systemService.getSystemById(id);
		if (s != null) {
			view.showOneSystem(s);
		} else {
			view.showOneSystemError("System not found.", null);
		}
	}

}
