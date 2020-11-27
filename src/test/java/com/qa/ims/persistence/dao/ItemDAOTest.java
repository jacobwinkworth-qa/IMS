package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAOTest {

	private final ItemDAO DAO = new ItemDAO();

	@Before
	public void setup() {
		DBUtils.getInstance().executeSQLFiles("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}

	@Test
	public void testCreate() {
		final Item created = new Item(4l, "cheese toastie", 10.0);
		assertEquals(created, DAO.create(created));
	}

	@Test
	public void testReadAll() {
		List<Item> expected = new ArrayList<>();
		expected.add(new Item(1l, "ball", 9.99));
		expected.add(new Item(2l, "shoe", 5.0));
		expected.add(new Item(3l, "frisbee", 10.0));
		assertEquals(expected, DAO.readAll());
	}

	@Test
	public void testReadLatest() {
		assertEquals(new Item(3l, "frisbee", 10.0), DAO.readLatest());
	}

	@Test
	public void testRead() {
		final long ID = 1l;
		assertEquals(new Item(ID, "ball", 9.99), DAO.readItem(ID));
	}

	@Test
	public void testUpdate() {
		final Item updated = new Item(1l, "football", 19.99);
		assertEquals(updated, DAO.update(updated));

	}

	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1));
	}

}
