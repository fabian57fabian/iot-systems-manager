package com.fabian57fabian.app.model.entities;

import java.util.Objects;

public class SensorEntity extends EntityBase {
	private String description;
	private double offset;
	private double multiplier;
	private int systemId;

	public SensorEntity(int id, String name, String description, double offset, double multiplier, int systemId) {
		super(id, name);
		this.description = description;
		this.offset = offset;
		this.multiplier = multiplier;
		this.setSystemId(systemId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		SensorEntity second = (SensorEntity) o;
		if (!super.equals(o))
			return false;
		return Objects.equals(description, second.description) && Objects.equals(offset, second.offset)
				&& Objects.equals(multiplier, second.multiplier) && Objects.equals(systemId, second.systemId);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public String getDescription() {
		return description;
	}

	public double getOffset() {
		return offset;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
}
