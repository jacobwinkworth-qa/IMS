package com.qa.ims.persistence.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAO implements Dao<Order> {

	public static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Reads all orders from the database
	 * 
	 * @return A list of orders
	 */
	@Override
	public List<Order> readAll() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from orders");) {
			List<Order> orders = new ArrayList<>();
			while (resultSet.next()) {
				orders.add(modelFromResultSet(resultSet));
			}
			return orders;
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return new ArrayList<>();
	}
	
	/**
	 * Fetches newest <code>Order</code> entity from data source.
	 * 
	 * @return <code>Order</code> entity or <code>null</code>
	 */
	public Order readLatest() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM orders ORDER BY id DESC LIMIT 1");) {
			resultSet.next();
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Fetches newest <code>Order</code> entity from data source.
	 * 
	 * @return <code>Order</code> entity or <code>null</code>
	 */
	@Override
	public Order create(Order order) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("INSERT INTO orders(order_id, customer_id) values('%s', %d)",
					order.getId(), order.getCustomer().getId()));
				Map<Item, Integer> items = order.getItems();
				for (Map.Entry<Item, Integer> entry : items.entrySet()) {
					statement.executeUpdate(String.format("INSERT INTO orders_items(order_id, item_id, quantity) values(%d, %d, %d)",
							order.getId(), entry.getKey().getId(), entry.getKey()));
				}
			return readLatest();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param id - The id of the <code>Order</code> to fetch from the data source
	 * @return The matching <code>Order</code> entity or <code>null</code>
	 */
	public Order readOrder(Long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM orders o"
						+ "INNER JOIN orders_items oi"
						+ "ON o.id = oi.order_id"
						+ "WHERE id = " + id);) {
			resultSet.next();
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Creates an order in the database.
	 * 
	 * @param order - takes in an order object. id will be ignored
	 * @return New order entity or <code>null</code>
	 */
	@Override
	public Order update(Order order) {
		return null;
	}

	/**
	 * 
	 * @param id - The id of the <code>Order</code> to fetch from the data source
	 * @return The matching <code>Order</code> entity or <code>null</code>
	 */
	@Override
	public int delete(long id) {
		return 0;
	}

	/**
	 * Updates a order in the database.
	 * 
	 * @param order - takes in a <code>Customer</code> object, the id field will
	 *                 be used to update that customer in the database
	 * @return updated <code>Customer</code> entity or <strong>null</strong>
	 */
	@Override
	public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
		Long order_id = resultSet.getLong("order_id");
		
		// build customer
		Long customer_id = resultSet.getLong("customer_id");
		String first_name = resultSet.getString("first_name");
		String surname = resultSet.getString("surname");
		Customer customer = new Customer(customer_id, first_name, surname);
		
		// build items
		Array item_ids_array = resultSet.getArray("item_id");
		Long[] item_ids = (Long[])item_ids_array.getArray();
		Array item_names_array = resultSet.getArray("name");
		String[] item_names = (String[])item_names_array.getArray();
		Array item_values_array = resultSet.getArray("value");
		Double[] item_values = (Double[])item_values_array.getArray();
		
		// list of items
		List<Item> items = IntStream.range(0, item_ids.length).boxed()
				.map(i -> new Item(item_ids[i], item_names[i], item_values[i]))
				.collect(Collectors.toList());
		
		// build items map
		Array quantities_array = resultSet.getArray("quantity");
		Integer[] quantities = (Integer[])quantities_array.getArray();
		
		Map<Item, Integer> items_quantities = IntStream.range(0, items.size()).boxed()
			    .collect(Collectors.toMap(i -> items.get(i), i -> quantities[i]));
		
		return new Order(order_id, customer, items_quantities);
	}
	
}