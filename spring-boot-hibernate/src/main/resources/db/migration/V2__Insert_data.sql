INSERT INTO customer (name) VALUES ('John Doe');
INSERT INTO customer (name) VALUES ('Jane Smith');
INSERT INTO customer (name) VALUES ('Jackie');

INSERT INTO product (name, price) VALUES ('Laptop', 999.99);
INSERT INTO product (name, price) VALUES ('Smartphone', 499.99);
INSERT INTO product (name, price) VALUES ('Pillow', 100.99);

INSERT INTO purchase (customer_id, product_id, price_at_purchase) VALUES (1, 1, 999.99);
INSERT INTO purchase (customer_id, product_id, price_at_purchase) VALUES (1, 2, 499.99);
INSERT INTO purchase (customer_id, product_id, price_at_purchase) VALUES (3, 3, 129.99);
INSERT INTO purchase (customer_id, product_id, price_at_purchase) VALUES (1, 3, 100.99);