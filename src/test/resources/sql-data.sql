INSERT INTO customers (first_name, surname) 
VALUES ('jacob', 'winkworth');

INSERT INTO customers (first_name, surname) 
VALUES ('ian', 'biel');

INSERT INTO items (name, value)
VALUES ('ball', 9.99);

INSERT INTO items (name, value)
VALUES ('shoe', 5.0);

INSERT INTO orders (customer_id)
VALUES (1);
INSERT INTO orders_items (order_id, item_id, quantity)
VALUES (last_insert_id(), 1, 1);
INSERT INTO orders_items (order_id, item_id, quantity)
VALUES (last_insert_id(), 2, 1);

INSERT INTO orders (customer_id)
VALUES (2);
INSERT INTO orders_items (order_id, item_id, quantity)
VALUES (last_insert_id(), 1, 1);
INSERT INTO orders_items (order_id, item_id, quantity)
VALUES (last_insert_id(), 2, 1);