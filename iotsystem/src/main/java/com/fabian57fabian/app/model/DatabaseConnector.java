package com.fabian57fabian.app.model;
import java.util.List;

import com.fabian57fabian.app.model.entities.SystemHeader;

public interface DatabaseConnector {
	public List<SystemHeader> RetrieveSystemNames();
}
