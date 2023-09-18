package com.fabian57fabian.app.controller;

import java.util.List;

import com.fabian57fabian.app.model.DatabaseConnector;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;

public class SystemsManagerController {
	private DatabaseConnector db_connector;
	
	public SystemsManagerController(DatabaseConnector db_connector) {
		this.db_connector = db_connector;
	}
	
	public List<SystemHeader> GetSystems() {
		return db_connector.RetrieveSystemNames();
	}

	public SystemEntity GetOneSystem(int id) {
		return db_connector.GetSystemById(id);
	}

}
