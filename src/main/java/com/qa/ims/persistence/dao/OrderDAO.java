package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
				ResultSet resultSet = statement.executeQuery("SELECT o.customer_id, oi.order_id, "
						+ "GROUP_CONCAT(i.item_id, ', ', i.name, ', ', i.value SEPARATOR '; ') items "
						+ "FROM orders o "
						+ "INNER JOIN orders_items oi "
						+ "ON o.order_id = oi.order_id "
						+ "INNER JOIN items i "
						+ "ON oi.item_id = i.item_id "
						+ "GROUP BY oi.order_id");) {
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
				ResultSet resultSet = statement.executeQuery("SELECT o.customer_id, oi.order_id, "
						+ "GROUP_CONCAT(i.item_id, ', ', i.name, ', ', i.value SEPARATOR '; ') items "
						+ "FROM orders o "
						+ "INNER JOIN orders_items oi "
						+ "ON o.order_id = oi.order_id "
						+ "INNER JOIN items i "
						+ "ON oi.item_id = i.item_id "
						+ "GROUP BY oi.order_id "
						+ "ORDER BY o.order_id DESC LIMIT 1");) {
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
			statement.executeUpdate(String.format("INSERT INTO orders(customer_id) values(%d)",
					order.getCustomer().getId()));
				ArrayList<Item> items = order.getItems();
				for (Item item : items) {
					statement.executeUpdate(String.format("INSERT INTO orders_items(order_id, item_id) "
							+ "values(last_insert_id(), %d)", item.getId()));
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
				ResultSet resultSet = statement.executeQuery(String.format("SELECT o.customer_id, oi.order_id, "
						+ "GROUP_CONCAT(i.item_id, ', ', i.name, ', ', i.value SEPARATOR '; ') items "
						+ "FROM orders o "
						+ "INNER JOIN orders_items oi "
						+ "ON o.order_id = oi.order_id "
						+ "INNER JOIN items i "
						+ "ON oi.item_id = i.item_id "
						+ "WHERE o.order_id = %d "
						+ "GROUP BY oi.order_id", id));) {
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
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("UPDATE orders "
					+ "SET customer_id = %d WHERE order_id = %d ", order.getCustomer().getId(), order.getId()));
			
			return readOrder(order.getId());
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	public Order addLine(Long orderId, Long itemId) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("INSERT INTO orders_items(order_id, item_id) "
					+ "values(%d, %d) ", orderId, itemId));
			
			return readOrder(orderId);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	public Order deleteLine(Long orderId, Long itemId) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("DELETE FROM orders_items "
					+ "WHERE order_id = %d AND item_id = %d", orderId, itemId));
			
			return readOrder(orderId);
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
	@Override
	public int delete(long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			return statement.executeUpdate("DELETE from orders where order_id = " + id);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
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
		
		// build customer
		Long customerId = resultSet.getLong("customer_id");
		Customer customer = new Customer(customerId);
		
		// build items
		String itemString = resultSet.getString("items");
		
		ArrayList<Item> itemArrayList = new ArrayList<>();
		Arrays.stream(itemString.split(";"))
				.map(s -> s.split(","))
				.forEach(s -> itemArrayList.add(new Item(Long.parseLong(s[0].trim()),
						s[1].trim(), Double.parseDouble(s[2].trim()))));
		
		itemArrayList.sort(Comparator.comparing(Item::getId)); // order by item ids (fixes ordering after ORDER BY clause)
		

		// build order
		Long orderId = resultSet.getLong("order_id");
		Order order = new Order(orderId, customer, itemArrayList);
		
		return order;
	}
	
}