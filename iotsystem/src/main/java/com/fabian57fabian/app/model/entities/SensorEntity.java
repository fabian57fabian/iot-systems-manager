package com.fabian57fabian.app.model.entities;

import java.util.Objects;

public class SensorEntity extends EntityBase {
	private String description;
	private double offset;
	private double multiplier;

	public SensorEntity(int id, String name, String description, double offset, double multiplier) {
		super(id, name);
		this.description = description;
		this.offset = offset;
		this.multiplier = multiplier;
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
				&& Objects.equals(multiplier, second.multiplier);
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
}