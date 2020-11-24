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
		this.id = 1l;
		this.name = "";
		this.value = 1.0;
	}
	
	public Item(long id) {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[id:" + id + " name:" + name + " value:" + value + "]";
	}

}