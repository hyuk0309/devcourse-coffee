package com.hyuk.coffeeserver.repository;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_DELETED_EXP_MSG;
import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_UPDATED_EXP_MSG;
import static com.hyuk.coffeeserver.util.JdbcUtils.toLocalDateTime;
import static com.hyuk.coffeeserver.util.JdbcUtils.toUUID;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NamedJdbcOrderRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NamedJdbcOrderRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update(
            "INSERT INTO orders(order_id, nick_name, order_status, created_at, updated_at) "
                + "VALUES (UUID_TO_BIN(:orderId), :nickName, :orderStatus, :createdAt, :updatedAt)",
            toOrderParamMap(order));
        order.getOrderItems().forEach(item -> jdbcTemplate.update(
            "INSERT INTO order_items(order_id, coffee_id, category, price, quantity, created_at, updated_at) "
                + "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:coffeeId), :category, :price, :quantity, :createdAt, :updatedAt)",
            toOrderItemParamMap(
                order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item))
        );
        return order;
    }

    @Override
    public List<Order> findOrdersOrderByCreatedAt() {
        return jdbcTemplate.query(
            "SELECT * FROM orders ORDER BY created_at DESC",
            Collections.emptyMap(),
            orderRowMapper
        );
    }

    @Override
    public List<Order> findOrdersOrderByCreatedAt(OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Optional<Order> findOrderWithOrderItems(UUID orderId) {
        var order = jdbcTemplate.query(
            "SELECT o.order_id as order_id, o.nick_name as nick_name, o.order_status as order_status,"
                + " o.created_at as created_at, o.updated_at as updated_at, oi.coffee_id as coffee_id,"
                + " oi.category as category, oi.price as price, oi.quantity as quantity"
                + " FROM orders o JOIN order_items oi on o.order_id = oi.order_id"
                + " WHERE o.order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes()),
            extractOrderWithOrderItems);
        if (order == null) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Override
    public void updateOrderStatusByOrderId(UUID orderId) {
        var update = jdbcTemplate.update(
            "UPDATE orders SET order_status = 'FINISHED' WHERE order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes()));
        if (update != 1) {
            throw new RuntimeException(NOTHING_WAS_UPDATED_EXP_MSG.toString());
        }
    }

    @Override
    public void deleteOrderAndOrderItems(UUID orderId) {
        var delete = jdbcTemplate.update(
            "DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes()));
        if (delete != 1) {
            throw new RuntimeException(NOTHING_WAS_DELETED_EXP_MSG.toString());
        }
    }

    private static final ResultSetExtractor<Order> extractOrderWithOrderItems = (resultSet) -> {
        UUID orderId = null;
        NickName nickName = null;
        List<OrderItem> orderItems = new ArrayList<>();
        OrderStatus orderStatus = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        while (resultSet.next()) {
            if (orderId == null) {
                orderId = toUUID(resultSet.getBytes("order_id"));
                nickName = new NickName(resultSet.getString("nick_name"));
                orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
                createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
                updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
            }

            var coffeeId = toUUID(resultSet.getBytes("coffee_id"));
            var category = Category.valueOf(resultSet.getString("category"));
            var price = resultSet.getLong("price");
            var quantity = resultSet.getInt("quantity");
            orderItems.add(new OrderItem(coffeeId, category, price, quantity));
        }

        if (orderId == null) {
            return null;
        }
        return new Order(orderId, nickName, orderItems, orderStatus, createdAt, updatedAt);
    };

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var nickName = new NickName(resultSet.getString("nick_name"));
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(
            orderId,
            nickName,
            null,
            orderStatus,
            createdAt,
            updatedAt
        );
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("nickName", order.getNickName().getNickName());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
        LocalDateTime updateAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("coffeeId", item.getCoffeeId().toString().getBytes());
        paramMap.put("category", item.getCategory().toString());
        paramMap.put("price", item.getPrice());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updateAt);
        return paramMap;
    }
}
