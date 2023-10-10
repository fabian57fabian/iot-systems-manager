package com.fabian57fabian.app.model.entities;

import java.util.Objects;

public class EntityBase {
	private int id;
	private String name;
	
	public EntityBase(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		EntityBase second = (EntityBase)o;
		return Objects.equals(id, second.id) 
				&& Objects.equals(name, second.name);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
