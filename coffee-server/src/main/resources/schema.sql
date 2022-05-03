CREATE TABLE coffees
(
    coffee_id   BINARY(16)   PRIMARY KEY,
    name        VARCHAR(20)  NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    price       bigint       NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    created_at  datetime(6)  NOT NULL,
    updated_at  datetime(6)  DEFAULT NULL,
    CONSTRAINT unq_coffee_name UNIQUE (name)
);

CREATE TABLE orders
(
    order_id     binary(16) PRIMARY KEY,
    nick_name    VARCHAR(50) NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    created_at   datetime(6) NOT NULL,
    updated_at   datetime(6) DEFAULT NULL
);

CREATE TABLE order_items
(
    seq        bigint      NOT NULL PRIMARY KEY auto_increment,
    order_id   binary(16)  NOT NULL,
    coffee_id  binary(16)  NOT NULL,
    category   VARCHAR(50) NOT NULL,
    price      bigint      NOT NULL,
    quantity   int         NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    index (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_coffee FOREIGN KEY (coffee_id) REFERENCES coffees (coffee_id)
);