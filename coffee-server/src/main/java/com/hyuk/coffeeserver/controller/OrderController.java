package com.hyuk.coffeeserver.controller;

import static com.hyuk.coffeeserver.controller.OrderDtoConverter.toOrderDtoList;

import com.hyuk.coffeeserver.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewOrders(Model model) {
        var orderDtos = toOrderDtoList(orderService.searchOrdersOrderByCreatedAt());
        model.addAttribute("orderDtos", orderDtos);
        return "order/orders";
    }
}
