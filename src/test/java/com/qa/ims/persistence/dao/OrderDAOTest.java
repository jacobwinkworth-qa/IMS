package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAOTest {

	private final OrderDAO DAO = new OrderDAO();

	@Before
	public void setup() {
		DBUtils.getInstance().executeSQLFiles("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}

	@Test
	public void testCreate() {
		Customer customer = new Customer(1l);
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(1l, "ball", 9.99));
		items.add(new Item(2l, "shoe", 5.0));
		final Order created = new Order(3l, customer, items);
		assertEquals(created, DAO.create(created));
	}

	@Test
	public void testReadAll() {
		List<Order> expected = new ArrayList<>();
		Customer customer1 = new Customer(1l);
		Customer customer2 = new Customer(2l);
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(1l, "ball", 9.99));
		items.add(new Item(2l, "shoe", 5.0));
		expected.add(new Order(1l, customer1, items));
		expected.add(new Order(2l, customer2, items));
		assertEquals(expected, DAO.readAll());
	}

	@Test
	public void testReadLatest() {
		Customer customer = new Customer(2l);
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(1l, "ball", 9.99));
		items.add(new Item(2l, "shoe", 5.0));
		assertEquals(new Order(2l, customer, items), DAO.readLatest());
	}

	@Test
	public void testRead() {
		final long ID = 1l;
		Customer customer = new Customer(1l);
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(1l, "ball", 9.99));
		items.add(new Item(2l, "shoe", 5.0));
		assertEquals(new Order(ID, customer, items), DAO.readOrder(ID));
	}

	@Test
	public void testUpdate() {
		Customer customer = new Customer(2l);
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(1l, "ball", 9.99));
		items.add(new Item(2l, "shoe", 5.0));
		final Order updated = new Order(1l, customer, items);
		assertEquals(updated, DAO.update(updated));

	}

	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1));
	}

}

