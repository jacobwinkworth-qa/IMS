package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAO implements Dao<Item> {
	
	public static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Reads all items from the database
	 * 
	 * @return A list of items
	 */
	@Override
	public List<Item> readAll() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from items");) {
			List<Item> items = new ArrayList<>();
			while (resultSet.next()) {
				items.add(modelFromResultSet(resultSet));
			}
			return items;
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return new ArrayList<>();
	}
	
	/**
	 * Fetches newest <code>Item</code> entity from data source.
	 * 
	 * @return <code>Item</code> entity or <code>null</code>
	 */
	public Item readLatest() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM items ORDER BY item_id DESC LIMIT 1");) {
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Creates a item in the database.
	 * 
	 * @param item - takes in a item object. id will be ignored
	 * @return New item entity or <code>null</code>
	 */
	@Override
	public Item create(Item item) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("INSERT INTO items(name, value) values('%s', %.2f)", 
					item.getName(), item.getValue()));
			return readLatest();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param id The id of the <code>Item</code> to fetch from the data source
	 * @return The matching <code>Item</code> entity or <code>null</code>
	 */
	public Item readItem(Long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM items where item_id = " + id);) {
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Updates a item in the database.
	 * 
	 * @param item - takes in a <code>Item</code> object, the id field will
	 *                 be used to update that item in the database
	 * @return updated <code>Item</code> entity or <strong>null</strong>
	 */
	@Override
	public Item update(Item item) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate(String.format("update items set name = '%s', value = %.2f where item_id = %d",
					item.getName(), item.getValue(), item.getId()));
					
			return readItem(item.getId());
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Deletes a item in the database.
	 * 
	 * @param id - id of the item
	 */
	@Override
	public int delete(long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			return statement.executeUpdate("delete from items where item_id = " + id);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return 0;
	}

	/**
	 * Converts a <code>ResultSet</code> into a <code>Item</code> entity.
	 * 
	 * @param resultSet The <code>ResultSet</code> returned by a JDBC query.
	 * @return <code>Item</code> entity.
	 */
	@Override
	public Item modelFromResultSet(ResultSet resultSet) throws SQLException {
		
		if (resultSet.next() == false) {
			return null;
		}
		
		long id = resultSet.getLong("item_id");
		String name = resultSet.getString("name");
		Double value = resultSet.getDouble("value");
		return new Item(id, name, value);
	}

}