package com.fabian57fabian.app.model.service;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.repository.SystemRepository;

public class SystemService implements ISystemService{
	private SystemRepository systemRepository;

	public SystemService(SystemRepository systemRepository) {
		this.systemRepository = systemRepository;
	}

	@Override
	public List<SystemEntity> getSystemNames() {
		return systemRepository.retrieveSystemNames();
	}

	@Override
	public SystemEntity getSystemById(int id) {
		return systemRepository.getSystemById(id);
	}

	@Override
	public Boolean create(SystemEntity system) {
		if(systemRepository.getSystemById(system.getId()) != null) {
			return false;
		}
		systemRepository.save(system);	
		return true;
	}

	@Override
	public void delete(int id) {
		systemRepository.delete(id);		
	}
}
