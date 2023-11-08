package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;

public interface ISystemService {
	public List<SystemEntity> getSystemNames();

	public SystemEntity getSystemById(int id);

	Boolean create(SystemEntity system);
	
	void delete(int id);
}
