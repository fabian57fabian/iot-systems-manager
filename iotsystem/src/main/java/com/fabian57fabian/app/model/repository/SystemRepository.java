package com.fabian57fabian.app.model.repository;
import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;

public interface SystemRepository {
	public List<SystemEntity> RetrieveSystemNames();

	public SystemEntity GetSystemById(int id);
}
