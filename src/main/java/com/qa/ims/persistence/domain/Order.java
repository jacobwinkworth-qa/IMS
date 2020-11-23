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
	
	@Override
	public String toString() {
		return "order_id:" + id + " customer_id:" + customer.getId() + " items: [" + items.toString() + "]";
	}
}