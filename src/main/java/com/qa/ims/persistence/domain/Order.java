package com.qa.ims.persistence.domain;

import java.util.ArrayList;

public class Order {
	
	private long id;
	private Customer customer;
	private ArrayList<Item> items;
	
	// constructors
	
	public Order() {
		this.id = 1L;
		this.customer = new Customer();
		this.items = new ArrayList<>();
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
	
	public Order(Customer customer, ArrayList<Item> items) {
		this();
		this.customer = customer;
		this.items = items;
	}
	
	public Order(long id, Customer customer, ArrayList<Item> items) {
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

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	public Double getTotal() {
		return items.stream().mapToDouble(o -> o.getValue()).sum();
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