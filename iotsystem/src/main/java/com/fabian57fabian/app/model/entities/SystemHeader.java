package com.fabian57fabian.app.model.entities;

import java.util.Objects;

public class SystemHeader {
	private int Id;
	private String Name;
	
	public SystemHeader(int id, String name) {
		Id = id;
		Name = name;
	}
	
	public int GetId() { return Id; }
	public String GetName() { return Name; }
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		SystemHeader second = (SystemHeader)o;
		return Objects.equals(Id, second.Id) 
				&& Objects.equals(Name, second.Name);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
