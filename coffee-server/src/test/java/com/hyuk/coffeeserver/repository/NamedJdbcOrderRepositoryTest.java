package com.hyuk.coffeeserver.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.entity.NickName;
import com.hyuk.coffeeserver.entity.Order;
import com.hyuk.coffeeserver.entity.OrderItem;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.zaxxer.hikari.HikariDataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@TestInstance(Lifecycle.PER_CLASS)
class NamedJdbcOrderRepositoryTest {

    @Autowired
    NamedJdbcOrderRepository orderRepository;

    @Autowired
    NamedJdbcCoffeeRepository coffeeRepository;

    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp() {
        var mysqldConfig = aMysqldConfig(v8_0_11)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
        embeddedMysql = anEmbeddedMysql(mysqldConfig)
            .addSchema("test-coffee_mgmt", classPathScript("schema.sql"))
            .start();
    }

    @AfterAll
    void clean() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("데이터 정상 저장")
    void testInsertSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "sweet americano", Category.AMERICANO, 1000L);
        coffeeRepository.insertCoffee(coffee);

        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());

        //when
        var insertedOrder = orderRepository.insert(order);

        //then
        assertAll(
            () -> assertThat(insertedOrder).isNotNull(),
            () -> assertThat(insertedOrder.getOrderId()).isEqualTo(order.getOrderId())
        );
    }

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:2215/test-coffee_mgmt")
                .username("test")
                .password("test1234!")
                .type(HikariDataSource.class)
                .build();
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }

        @Bean
        public NamedJdbcOrderRepository namedJdbcOrderRepository(
            NamedParameterJdbcTemplate jdbcTemplate) {
            return new NamedJdbcOrderRepository(jdbcTemplate);
        }

        @Bean
        public NamedJdbcCoffeeRepository namedJdbcCoffeeRepository(
            NamedParameterJdbcTemplate jdbcTemplate) {
            return new NamedJdbcCoffeeRepository(jdbcTemplate);
        }
    }
}