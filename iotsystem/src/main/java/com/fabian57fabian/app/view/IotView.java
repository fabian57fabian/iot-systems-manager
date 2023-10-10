package com.fabian57fabian.app.view;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;

public interface IotView {
	public void showSystems(List<SystemEntity> systems);

	public void showOneSystem(SystemEntity getSystemById);

	public void showOneSystemError(String string);
}
