package com.qa.ims.persistence.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Item entity.
 * 
 * @author Jacob Winkworth
 *
 */
public class Item {
	
	private long id;
	private String name;
	private BigDecimal value;
	
	// constructors
	
	public Item() {
		this.id = 1l;
		this.name = "";
		this.value = new BigDecimal(1).setScale(2, RoundingMode.HALF_UP);
	}
	
	public Item(long id) {
		this();
		this.id = id;
	}
	
	public Item(String name, double value) {
		this();
		this.name = name;
		this.value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
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
	
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[id:" + id + " name:" + name + " value:" + value.toString() + "]";
	}

}