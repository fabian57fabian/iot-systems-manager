package com.fabian57fabian.app.view;

import java.util.List;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;

public interface IotView {
	public void ShowSystems(List<SystemHeader> systems);

	public void ShowOneSystem(SystemEntity getSystemById);
}
