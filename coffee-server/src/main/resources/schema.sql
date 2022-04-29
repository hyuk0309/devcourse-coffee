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