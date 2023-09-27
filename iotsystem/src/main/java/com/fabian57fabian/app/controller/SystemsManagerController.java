package com.fabian57fabian.app.controller;

import java.util.List;

import com.fabian57fabian.app.view.IotView;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;
import com.fabian57fabian.app.model.repository.SystemRepository;

public class SystemsManagerController {
	private SystemRepository db_connector;
	private IotView view;
	
	public SystemsManagerController(SystemRepository db_connector, IotView view) {
		this.db_connector = db_connector;
		this.view = view;
	}
	
	public void viewAllSystems() {
		view.ShowSystems(db_connector.RetrieveSystemNames());
	}

	public void ExpandOneSystem(int id) {
		view.ShowOneSystem(db_connector.GetSystemById(id));
	}

}
