package com.hyuk.coffeeserver.repository;

import com.hyuk.coffeeserver.entity.Coffee;
import java.util.Optional;

public interface CoffeeRepository {

    Coffee insertCoffee(Coffee coffee);

    Optional<Coffee> findByName(String name);
}
