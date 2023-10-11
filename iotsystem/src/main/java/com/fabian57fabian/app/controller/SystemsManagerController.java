package com.fabian57fabian.app.controller;

import com.fabian57fabian.app.view.IotView;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.repository.SystemRepository;

public class SystemsManagerController {
	private SystemRepository systemRepository;
	private IotView view;

	public SystemsManagerController(SystemRepository systemRepository, IotView view) {
		this.systemRepository = systemRepository;
		this.view = view;
	}

	public void viewAllSystems() {
		view.showSystems(systemRepository.retrieveSystemNames());
	}

	public void expandOneSystem(int id) {
		SystemEntity s = systemRepository.getSystemById(id);
		if (s != null) {
			view.showOneSystem(s);
		} else {
			view.showOneSystemError("System not found.", null);
		}
	}

}
