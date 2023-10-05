package com.fabian57fabian.app.model.entities;

import java.util.Objects;

public class SystemEntity extends SystemHeader {
	private String description;
	private boolean active;
		
	
	public SystemEntity(int id, String name, String description, boolean active) {
		super(id, name);
		this.description = description;
		this.active = active;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		SystemEntity second = (SystemEntity)o;
		if(!super.equals(o))
			return false;
		return Objects.equals(description, second.description) 
				&& Objects.equals(active, second.active);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public String GetDescription() {
		return description;
	}

	public boolean GetAcrive() {
		return active;
	}
}
