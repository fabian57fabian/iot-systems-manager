package com.fabian57fabian.app.model.entities;

public class SystemEntity extends SystemHeader {
	private String description;
	private boolean active;
		
	
	public SystemEntity(int id, String name, String description, boolean active) {
		super(id, name);
		this.description = description;
		this.active = active;
	}
}
