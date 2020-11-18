package com.qa.ims.persistence.domain;

import java.util.Map;
import java.util.stream.Collectors;

public class Order {
	
	private long id;
	private Customer customer;
	private Map<Item, Integer> items;
	
	// customer
	public Order(long id, Customer customer, Map<Item, Integer> items) {
		this.setId(id);
		this.setCustomer(customer);
		this.setItems(items);
	}

	// getters and setter
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

	public Map<Item, Integer> getItems() {
		return items;
	}

	public void setItems(Map<Item, Integer> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		return "id:" + id + " items: " + items.entrySet().stream().map(Object::toString)
                .collect(Collectors.joining(", "));
	}

}
