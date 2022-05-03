package com.hyuk.coffeeserver.controller;

import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.service.CoffeeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coffees")
public class ApiCoffeeController {

    private final CoffeeService coffeeService;

    public ApiCoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public List<Coffee> coffeeList() {
        return coffeeService.findAllCoffees();
    }
}
