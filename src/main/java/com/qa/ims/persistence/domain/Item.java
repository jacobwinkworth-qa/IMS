package com.qa.ims.persistence.domain;

/**
 * Item entity.
 * 
 * @author Jacob Winkworth
 *
 */
public class Item {
	
	private long id;
	private String name;
	private double value;
	
	// constructors
	
	public Item() {
		this.id = 1L;
		this.name = "";
		this.value = 1.0;
	}
	
	public Item(Long id) {
		this();
		this.id = id;
	}
	
	public Item(String name, double value) {
		this();
		this.name = name;
		this.value = value;
	}
	
	public Item(long id, String name, double value) {
		this(name, value);
		this.id = id;
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