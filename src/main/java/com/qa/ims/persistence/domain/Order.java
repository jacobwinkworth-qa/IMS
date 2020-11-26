package com.qa.ims.persistence.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class Order {
	
	private long id;
	private Customer customer;
	private HashMap<Item, Long> items;
	
	// constructors
	
	public Order() {
		this.id = 1L;
		this.customer = new Customer();
		this.items = new HashMap<>();
	}
	
	public Order(Long id) {
		this();
		this.id = id;;
	}
	
	public Order(Long id, Customer customer) {
		this();
		this.id = id;
		this.customer = customer;
	}
	
	public Order(Customer customer, HashMap<Item, Long> items) {
		this();
		this.customer = customer;
		this.items = items;
	}
	
	public Order(long id, Customer customer, HashMap<Item, Long> items) {
		this(customer, items);
		this.id = id;
	}

	// getters and setters
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public HashMap<Item, Long> getItems() {
		return items;
	}

	public void setItems(HashMap<Item, Long> items) {
		this.items = items;
	}
	
	public void addItem(Item item, Long quanity) {
		this.items.put(item, quanity);
	}
	
	public void removeItem(Item item) {
		this.items.remove(item);
	}
		
	public BigDecimal getTotal() {
		return items.entrySet().stream()
				.map(o -> o.getKey().getValue().multiply(new BigDecimal(o.getValue())))
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_UP);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		Order other = (Order) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id != other.id)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "order_id:" + id + ", customer_id:" + customer.getId() + "\nitems: " + items.toString() + "\ntotal: " + getTotal();
	}
}