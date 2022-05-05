package com.hyuk.coffeeserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/homes";
    }

    @GetMapping("/homes")
    public String viewHomePage() {
        return "home";
    }
}
