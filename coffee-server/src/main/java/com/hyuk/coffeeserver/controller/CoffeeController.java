package com.hyuk.coffeeserver.controller;

import com.hyuk.coffeeserver.dto.CreateCoffeeRequest;
import com.hyuk.coffeeserver.service.CoffeeService;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{coffeeId}")
    public String viewCoffee(@PathVariable("coffeeId") UUID coffeeId, Model model) {
        var coffee = coffeeService.findCoffee(coffeeId);
        model.addAttribute("coffee", coffee);
        return "coffee/coffee";
    }

    @GetMapping("/new")
    public String viewCreateForm() {
        return "coffee/new-form";
    }

    @PostMapping("/new")
    public String createCoffee(CreateCoffeeRequest createCoffeeRequest) {
        coffeeService.createCoffee(
            createCoffeeRequest.getName(),
            createCoffeeRequest.getCategory(),
            createCoffeeRequest.getPrice()
        );
        return "redirect:/coffees";
    }
}
