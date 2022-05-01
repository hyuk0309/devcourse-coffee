package com.hyuk.coffeeserver.service;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import java.util.List;
import java.util.UUID;

public interface CoffeeService {

    Coffee createCoffee(String name, Category category, long price);

    void removeCoffee(UUID id);

    List<Coffee> findAllCoffees();
}
