package com.qa.ims.persistence.domain;

public class Item {
	
	private long id;
	private String name;
	private double value;
	
	// constructors
	public Item(String name, double value) {
		this.setName(name);
		this.setValue(value);
	}
	
	public Item(long id, String name, double value) {
		this.setId(id);
		this.setName(name);
		this.setValue(value);
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "id:" + id + " name:" + name + " value:" + value;
	}

}
