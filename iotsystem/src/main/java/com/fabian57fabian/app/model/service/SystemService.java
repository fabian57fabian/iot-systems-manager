package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;

public interface SystemService {
	public List<SystemHeader> getAllSystems();

	public SystemEntity getSystem(int id);
}