package com.hyuk.coffeeserver.repository;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_UPDATED_EXP_MSG;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class NamedJdbcOrderRepositoryTest {

    @Autowired
    NamedJdbcOrderRepository orderRepository;

    @Autowired
    NamedJdbcCoffeeRepository coffeeRepository;

    EmbeddedMysql embeddedMysql;

    Coffee coffee;

    @BeforeAll
    void setUp() {
        //embedded db set up
        var mysqldConfig = aMysqldConfig(v8_0_11)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
        embeddedMysql = anEmbeddedMysql(mysqldConfig)
            .addSchema("test-coffee_mgmt", classPathScript("schema.sql"))
            .start();

        //coffee data set up
        coffee = new Coffee(UUID.randomUUID(), "sweet americano", Category.AMERICANO, 1000L);
        coffeeRepository.insertCoffee(coffee);
    }

    @AfterAll
    void clean() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("주문 정상 저장")
    void testInsertSuccess() {
        //given
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

    @Test
    @DisplayName("전체 주문 생성일 기준 내림 차순 조회")
    void testFindOrdersOrderByCreatedAtSuccess() {
        //given
        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());

        orderRepository.insert(order);

        //when
        var orders = orderRepository.findOrdersOrderByCreatedAt();

        //then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 주문 주문한 아이템과 함께 조회 - 주문 존재")
    void testFindOrdersOrderByCreatedAtWhenExist() {
        //given
        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());
        orderRepository.insert(order);

        //when
        var retrievedOrder = orderRepository.findOrderWithOrderItems(order.getOrderId());

        //then
        assertAll(
            () -> assertThat(retrievedOrder).isNotEmpty(),
            () -> assertThat(retrievedOrder.get().getOrderId()).isEqualTo(order.getOrderId()),
            () -> assertThat(retrievedOrder.get().getOrderItems().size()).isEqualTo(1),
            () -> assertThat(retrievedOrder.get().getOrderItems().get(0).getCoffeeId())
                .isEqualTo(coffee.getId())
        );
    }

    @Test
    @DisplayName("특정 주문 주문한 아이템과 함께 조회 - 주문 존재 x")
    void testFindOrdersOrderByCreatedAtWhenNoExist() {
        //given
        //when
        var retrievedOrder = orderRepository.findOrderWithOrderItems(UUID.randomUUID());

        //then
        assertThat(retrievedOrder).isEmpty();
    }

    @Test
    @DisplayName("ORDERED 주문 FINISHED로 상태 변경")
    void testUpdateOrderStatusByOrderIdSuccess() {
        //given
        var orderItems = List.of(
            new OrderItem(coffee.getId(), coffee.getCategory(), coffee.getPrice(), 1));

        var order = new Order(
            UUID.randomUUID(),
            new NickName("test"),
            orderItems,
            OrderStatus.ORDERED,
            LocalDateTime.now(),
            LocalDateTime.now());
        orderRepository.insert(order);

        //when
        orderRepository.updateOrderStatusByOrderId(order.getOrderId());

        //then
        var retrievedOrder = orderRepository.findOrderWithOrderItems(order.getOrderId());
        assertAll(
            () -> assertThat(retrievedOrder).isNotEmpty(),
            () -> assertThat(retrievedOrder.get().getOrderStatus()).isEqualTo(OrderStatus.FINISHED)
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문 상태 변경")
    void testUpdateOrderStatusByOrderIdFailBecauseNotExistOrderId() {
        //given
        var invalidId = UUID.randomUUID();

        //when
        //then
        assertThatThrownBy(() -> orderRepository.updateOrderStatusByOrderId(invalidId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(NOTHING_WAS_UPDATED_EXP_MSG.toString());
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
        public TransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
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