package com.hyuk.coffeeserver.repository;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_DELETED_EXP_MSG;
import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_INSERTED_EXP_MSG;
import static com.hyuk.coffeeserver.util.JdbcUtils.toLocalDateTime;
import static com.hyuk.coffeeserver.util.JdbcUtils.toUUID;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NamedJdbcCoffeeRepository implements
    CoffeeRepository {

    //컬럼
    private static final String COLUMN_COFFEE_ID = "coffee_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    //SQL
    private static final String INSERT_SQL =
        "INSERT INTO coffees(coffee_id, name, category, price, description, created_at, updated_at) "
            + "VALUES (UUID_TO_BIN(:id), :name, :category, :price, :description, :createdAt, :updatedAt)";
    private static final String SELECT_BY_NAME_SQL = "SELECT * FROM coffees WHERE name = :name";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM coffees WHERE coffee_id = UUID_TO_BIN(:id)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM coffees";
    private static final String DELETE_ALL_SQL = "DELETE FROM coffees";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM coffees where coffee_id = UUID_TO_BIN(:id)";

    //ParamMap Key
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_CATEGORY = "category";
    private static final String PARAM_PRICE = "price";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_CREATED_AT = "createdAt";
    private static final String PARAM_UPDATED_AT = "updatedAt";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NamedJdbcCoffeeRepository(
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Coffee insertCoffee(Coffee coffee) {
        var insert = jdbcTemplate.update(
            INSERT_SQL,
            toParamMap(coffee));
        if (insert != 1) {
            throw new RuntimeException(NOTHING_WAS_INSERTED_EXP_MSG);
        }
        return coffee;
    }

    @Override
    public Optional<Coffee> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_BY_NAME_SQL,
                Collections.singletonMap(PARAM_NAME, name),
                coffeeRowMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Coffee> findById(UUID id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_BY_ID_SQL,
                Collections.singletonMap(PARAM_ID, id.toString().getBytes()),
                coffeeRowMapper
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID id) {
        var delete = jdbcTemplate.update(
            DELETE_BY_ID_SQL,
            Collections.singletonMap(PARAM_ID, id.toString().getBytes())
        );
        if (delete != 1) {
            throw new RuntimeException(NOTHING_WAS_DELETED_EXP_MSG);
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_SQL, Collections.emptyMap());
    }

    @Override
    public List<Coffee> findAll() {
        return jdbcTemplate.query(
            SELECT_ALL_SQL,
            coffeeRowMapper
        );
    }

    private static final RowMapper<Coffee> coffeeRowMapper = (resultSet, i) -> {
        var id = toUUID(resultSet.getBytes(COLUMN_COFFEE_ID));
        var name = resultSet.getString(COLUMN_NAME);
        var category = Category.valueOf(resultSet.getString(COLUMN_CATEGORY));
        var price = resultSet.getLong(COLUMN_PRICE);
        var description = resultSet.getString(COLUMN_DESCRIPTION);
        var createdAt = toLocalDateTime(resultSet.getTimestamp(COLUMN_CREATED_AT));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp(COLUMN_UPDATED_AT));
        return new Coffee(id, name, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Coffee coffee) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put(PARAM_ID, coffee.getId().toString().getBytes());
        paramMap.put(PARAM_NAME, coffee.getName());
        paramMap.put(PARAM_CATEGORY, coffee.getCategory().toString());
        paramMap.put(PARAM_PRICE, coffee.getPrice());
        paramMap.put(PARAM_DESCRIPTION, coffee.getDescription());
        paramMap.put(PARAM_CREATED_AT, coffee.getCreatedAt());
        paramMap.put(PARAM_UPDATED_AT, coffee.getUpdatedAt());
        return paramMap;
    }
}
