package com.hyuk.coffeeserver.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.assertj.core.api.Assertions.assertThat;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.zaxxer.hikari.HikariDataSource;
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

    @Test
    @DisplayName("coffee 삽입")
    void testInsertCoffee() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee1", Category.AMERICANO, 2000);

        //when
        coffeeRepository.insertCoffee(coffee);

        //then
        assertThat(coffeeRepository.findByName(coffee.getName())).isNotEmpty();
    }

    @Test
    @DisplayName("특정 이름 coffee 조회 - 있음")
    void testFindByNameWhenExist() {
        //given
        var coffee = new Coffee(UUID.randomUUID(), "testCoffee2", Category.AMERICANO, 2000);
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
        var retrievedCoffee = coffeeRepository.findByName("testCoffee3");

        //then
        assertThat(retrievedCoffee).isEmpty();
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