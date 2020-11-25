package com.qa.ims.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.qa.ims.controller.OrderController;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	@Mock
	private Utils utils;

	@Mock
	private OrderDAO orderDAO;
	
	@Mock
	private ItemDAO itemDAO;
	
	@Mock
	private CustomerDAO customerDAO;

	@InjectMocks
	private OrderController controller;

	@Test
	public void testCreate() {
		final Long ORDER_ID = 1l, CUSTOMER_ID = 1l;
		Long[] itemArray = new Long[] {1l, 2l};
		
		ArrayList<Item> itemArrayList = new ArrayList<>();
		itemArrayList.add(new Item(itemArray[0]));
		itemArrayList.add(new Item(itemArray[1]));

		Mockito.when(utils.getLong()).thenReturn(CUSTOMER_ID);
		Customer customer = new Customer(CUSTOMER_ID);
		
		Mockito.when(customerDAO.readCustomer(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(utils.getLongArray()).thenReturn(itemArray);
		Mockito.when(itemDAO.readItem(itemArray[0])).thenReturn(new Item(itemArray[0]));
		Mockito.when(itemDAO.readItem(itemArray[1])).thenReturn(new Item(itemArray[1]));
		
		Order order = new Order(customer, new ArrayList<>());
		order.addItem(new Item(itemArray[0]));
		order.addItem(new Item(itemArray[1]));
		
		Order created = new Order(ORDER_ID, customer, new ArrayList<>());
		created.addItem(new Item(itemArray[0], "ball", 9.99));
		created.addItem(new Item(itemArray[1], "shoe", 5.0));
		
		Mockito.when(orderDAO.create(order)).thenReturn(created);

		assertEquals(created, controller.create());

		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(customerDAO, Mockito.times(1)).readCustomer(CUSTOMER_ID);
		Mockito.verify(utils, Mockito.times(1)).getLongArray();
		Mockito.verify(itemDAO, Mockito.times(1)).readItem(itemArray[0]);
		Mockito.verify(itemDAO, Mockito.times(1)).readItem(itemArray[1]);
		Mockito.verify(orderDAO, Mockito.times(1)).create(order);
	}

	@Test
	public void testReadAll() {
		List<Order> orders = new ArrayList<>();
		
		Customer customer = new Customer(1l);
		Long[] itemArray = new Long[] {1l, 2l};
		
		ArrayList<Item> items = new ArrayList<>();
		items.add(new Item(itemArray[0], "ball", 9.99));
		items.add(new Item(itemArray[1], "shoe", 5.0));
		
		Order order = new Order(1l, customer, items);
		orders.add(order);

		Mockito.when(orderDAO.readAll()).thenReturn(orders);

		assertEquals(orders, controller.readAll());

		Mockito.verify(orderDAO, Mockito.times(1)).readAll();
	}

	@Test
	public void testUpdate() {
		final Long ORDER_ID = 1l, ITEM_ID = 3l;
		Long customerId = 1l;
		Long[] itemArray = new Long[] {1l, 2l};
		
		Customer customer = new Customer(customerId);
		
		ArrayList<Item> itemArrayList = new ArrayList<>();
		itemArrayList.add(new Item(itemArray[0], "ball", 9.99));
		itemArrayList.add(new Item(itemArray[1], "shoe", 5.0));
		
		Order updated = new Order(ORDER_ID, customer, itemArrayList);
		
		// test 'add item'
		Item item = new Item(ITEM_ID, "bag", 10);
		updated.addItem(item);
		
		Mockito.when(utils.getLong()).thenReturn(ORDER_ID, ITEM_ID);
		Mockito.when(orderDAO.readOrder(ORDER_ID)).thenReturn(updated);
		
		Mockito.when(utils.getString()).thenReturn("add item");
		Mockito.when(itemDAO.readItem(ITEM_ID)).thenReturn(item);
		Mockito.when(orderDAO.addLine(ORDER_ID, ITEM_ID)).thenReturn(updated);
		
		assertEquals(updated, this.controller.update());
		
		// test 'delete item'
		updated.removeItem(item);
		
		Mockito.when(utils.getLong()).thenReturn(ORDER_ID, ITEM_ID);
		Mockito.when(orderDAO.readOrder(ORDER_ID)).thenReturn(updated);
		
		Mockito.when(utils.getString()).thenReturn("delete item");
		Mockito.when(itemDAO.readItem(ITEM_ID)).thenReturn(item);
		Mockito.when(orderDAO.deleteLine(ORDER_ID, ITEM_ID)).thenReturn(updated);
		
		assertEquals(updated, this.controller.update());
		
		// test update customer
		customerId = 2l;
		updated.getCustomer().setId(customerId);
		
		Mockito.when(utils.getLong()).thenReturn(ORDER_ID, customerId);
		Mockito.when(orderDAO.readOrder(ORDER_ID)).thenReturn(updated);
		
		Mockito.when(utils.getString()).thenReturn("update customer");
		Mockito.when(customerDAO.readCustomer(customerId)).thenReturn(customer);
		Mockito.when(orderDAO.update(new Order(ORDER_ID, customer))).thenReturn(updated);
		
		assertEquals(updated, this.controller.update());

		Mockito.verify(utils, Mockito.times(6)).getLong();
		Mockito.verify(utils, Mockito.times(3)).getString();
		
		Mockito.verify(orderDAO, Mockito.times(3)).readOrder(ORDER_ID);
		Mockito.verify(itemDAO, Mockito.times(2)).readItem(ITEM_ID);
		Mockito.verify(customerDAO, Mockito.times(1)).readCustomer(customerId);
		
		Mockito.verify(orderDAO, Mockito.times(1)).addLine(ORDER_ID, ITEM_ID);
		Mockito.verify(orderDAO, Mockito.times(1)).deleteLine(ORDER_ID, ITEM_ID);
		Mockito.verify(orderDAO, Mockito.times(1)).update(new Order(ORDER_ID, customer));
	}

//	@Test
//	public void testDelete() {
//		final long ID = 1L;
//
//		Mockito.when(utils.getLong()).thenReturn(ID);
//		Mockito.when(dao.delete(ID)).thenReturn(1);
//
//		assertEquals(1L, this.controller.delete());
//
//		Mockito.verify(utils, Mockito.times(1)).getLong();
//		Mockito.verify(dao, Mockito.times(1)).delete(ID);
//	}

}
