package com.fabian57fabian.app.controller;

import java.util.List;

import com.fabian57fabian.app.view.IotView;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;
import com.fabian57fabian.app.model.repository.SystemRepository;
import com.fabian57fabian.app.model.service.SystemService;

public class SystemsManagerController {
	private SystemRepository db_connector;
	private SystemService systemService;
	private IotView view;

	public SystemsManagerController(SystemRepository db_connector, SystemService systemService, IotView view) {
		this.db_connector = db_connector;
		this.systemService = systemService;
		this.view = view;
	}

	public void viewAllSystems() {
		view.ShowSystems(db_connector.RetrieveSystemNames());
	}

	public void expandOneSystem(int id) {
		SystemEntity s = db_connector.GetSystemById(id);
		if (s != null) {
			view.ShowOneSystem(s);
		} else {
			view.ShowOneSystemError("System not found.");
		}
	}

}
