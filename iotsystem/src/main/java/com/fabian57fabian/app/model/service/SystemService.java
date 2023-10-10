package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;

public interface SystemService {
	public List<SystemEntity> getAllSystems();

	public SystemEntity getSystem(int id);
}
