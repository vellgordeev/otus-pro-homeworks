CREATE TABLE products
(
    id         bigserial primary key,
    name       VARCHAR(255) NOT NULL,
    price      DOUBLE       NOT NULL,
    created_at timestamp,
    updated_at timestamp
);
