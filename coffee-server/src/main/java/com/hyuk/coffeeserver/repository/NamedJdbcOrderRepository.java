package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
