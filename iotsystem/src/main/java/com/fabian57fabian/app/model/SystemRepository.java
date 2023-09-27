package com.fabian57fabian.app.model;
import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;

public interface SystemRepository {
	public List<SystemHeader> RetrieveSystemNames();

	public SystemEntity GetSystemById(int Id);
}
