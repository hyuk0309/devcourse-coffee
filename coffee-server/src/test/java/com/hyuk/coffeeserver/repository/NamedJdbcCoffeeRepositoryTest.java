package com.hyuk.coffeeserver.repository;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.NOTHING_WAS_DELETED_EXP_MSG;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.zaxxer.hikari.HikariDataSource;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
class NamedJdbcCoffeeRepositoryTest {

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

    @AfterEach
    void clear() {
        coffeeRepository.deleteAll();
    }

    @Test
    @DisplayName("coffee 삽입")
    void testInsertCoffee() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee", Category.AMERICANO, 2000);

        //when
        coffeeRepository.insertCoffee(coffee);

        //then
        assertThat(coffeeRepository.findByName(coffee.getName())).isNotEmpty();
    }

    @Test
    @DisplayName("특정 이름 coffee 조회 - 있음")
    void testFindByNameWhenExist() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee", Category.AMERICANO, 2000L);
        coffeeRepository.insertCoffee(coffee);

        //when
        var retrievedCoffee = coffeeRepository.findByName(coffee.getName());

        //then
        assertThat(retrievedCoffee).isNotEmpty();
    }

    @Test
    @DisplayName("특정 이름 coffee 조회 - 없음")
    void testFindByNameWhenNoExist() {
        //given
        //when
        var retrievedCoffee = coffeeRepository.findByName("testCoffee");

        //then
        assertThat(retrievedCoffee).isEmpty();
    }

    @Test
    @DisplayName("특정 아이디 coffee 조회 - 있음")
    void testFindByIdExist() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee", Category.AMERICANO, 2500L);
        coffeeRepository.insertCoffee(coffee);

        //when
        var retrievedCoffee = coffeeRepository.findById(coffee.getId());

        //then
        assertAll(
            () -> assertThat(retrievedCoffee).isNotEmpty(),
            () -> assertThat(retrievedCoffee.get().getId()).isEqualTo(coffee.getId()),
            () -> assertThat(retrievedCoffee.get().getName()).isEqualTo(coffee.getName()),
            () -> assertThat(retrievedCoffee.get().getCategory()).isEqualTo(coffee.getCategory()),
            () -> assertThat(retrievedCoffee.get().getPrice()).isEqualTo(coffee.getPrice())
        );
    }

    @Test
    @DisplayName("특정 아이디 coffee 조회 - 없음")
    void testFindByIdNoExist() {
        //given
        var invalidId = UUID.randomUUID();

        //when
        var retrievedCoffee = coffeeRepository.findById(invalidId);

        //then
        assertThat(retrievedCoffee).isEmpty();
    }

    @Test
    @DisplayName("모든 커피 삭제")
    void testDeleteAll() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee", Category.AMERICANO, 2500L);
        var coffee2 = new Coffee(UUID.randomUUID(), "testCoffee1", Category.LATTE, 4000L);

        coffeeRepository.insertCoffee(coffee);
        coffeeRepository.insertCoffee(coffee2);

        //when
        coffeeRepository.deleteAll();

        //then
        var retrievedCoffee = coffeeRepository.findById(coffee.getId());
        var retrievedCoffee2 = coffeeRepository.findById(coffee2.getId());
        assertAll(
            () -> assertThat(retrievedCoffee).isEmpty(),
            () -> assertThat(retrievedCoffee2).isEmpty()
        );
    }

    @Test
    @DisplayName("특정 ID coffee 정상 삭제")
    void testDeleteByIdSuccess() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 1500L);
        coffeeRepository.insertCoffee(coffee);

        //when
        coffeeRepository.deleteById(coffee.getId());

        //then
        var retrievedCoffee = coffeeRepository.findById(coffee.getId());
        assertThat(retrievedCoffee).isEmpty();
    }

    @Test
    @DisplayName("특정 ID coffee 삭제 실패")
    void testDeleteByIdFailBecauseNotExist() {
        //given
        //when
        //then
        assertThatThrownBy(() -> coffeeRepository.deleteById(UUID.randomUUID()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(NOTHING_WAS_DELETED_EXP_MSG);
    }

    @Test
    @DisplayName("모든 커피 조회")
    void testFindAll() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "coffeeName", Category.AMERICANO, 2000L);
        var coffee2 = new Coffee(UUID.randomUUID(), "coffeeName2", Category.LATTE, 3000L);

        coffeeRepository.insertCoffee(coffee);
        coffeeRepository.insertCoffee(coffee2);

        //when
        var coffees = coffeeRepository.findAll();

        //then
        assertThat(coffees.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 커피 조회")
    void testFindAllWhenEmpty() {
        //given
        //when
        var coffees = coffeeRepository.findAll();

        //then
        assertThat(coffees).isEmpty();
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
        public NamedJdbcCoffeeRepository namedJdbcCoffeeRepository(
            NamedParameterJdbcTemplate jdbcTemplate) {
            return new NamedJdbcCoffeeRepository(jdbcTemplate);
        }
    }
}