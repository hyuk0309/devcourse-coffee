package com.hyuk.coffeeserver.controller;

import com.hyuk.coffeeserver.service.CoffeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public String viewAllCoffee(Model model) {
        var coffees = coffeeService.findAllCoffees();
        model.addAttribute("coffees", coffees);
        return "coffee/coffees";
    }
}