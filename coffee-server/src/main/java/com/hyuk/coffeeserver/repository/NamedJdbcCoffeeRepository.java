package com.hyuk.coffeeserver.repository;

import static com.hyuk.coffeeserver.util.JdbcUtils.toLocalDateTime;
import static com.hyuk.coffeeserver.util.JdbcUtils.toUUID;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NamedJdbcCoffeeRepository implements
    CoffeeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NamedJdbcCoffeeRepository(
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Coffee insertCoffee(Coffee coffee) {
        var insert = jdbcTemplate.update(
            "INSERT INTO coffees(coffee_id, name, category, price, description, created_at, updated_at) "
                + "VALUES (UUID_TO_BIN(:id), :name, :category, :price, :description, :createdAt, :updatedAt)",
            toParamMap(coffee));
        if (insert != 1) {
            throw new RuntimeException("Nothing was inserted!");
        }
        return coffee;
    }

    @Override
    public Optional<Coffee> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM coffees WHERE name = :name",
                Collections.singletonMap("name", name),
                coffeeRowMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static final RowMapper<Coffee> coffeeRowMapper = (resultSet, i) -> {
        var id = toUUID(resultSet.getBytes("coffee_id"));
        var name = resultSet.getString("name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Coffee(id, name, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Coffee coffee) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("id", coffee.getId().toString().getBytes());
        paramMap.put("name", coffee.getName());
        paramMap.put("category", coffee.getCategory().toString());
        paramMap.put("price", coffee.getPrice());
        paramMap.put("description", coffee.getDescription());
        paramMap.put("createdAt", coffee.getCreatedAt());
        paramMap.put("updatedAt", coffee.getUpdatedAt());
        return paramMap;
    }
}
