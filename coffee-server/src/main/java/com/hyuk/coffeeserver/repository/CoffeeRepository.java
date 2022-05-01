package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Coffee;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoffeeRepository {

    Coffee insertCoffee(Coffee coffee);

    Optional<Coffee> findByName(String name);

    Optional<Coffee> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Coffee> findAll();
}
