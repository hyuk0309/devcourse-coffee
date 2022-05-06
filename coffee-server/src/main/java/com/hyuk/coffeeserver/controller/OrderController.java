package com.hyuk.coffeeserver.controller;

import static com.hyuk.coffeeserver.controller.OrderDtoConverter.toOrderDtoList;
import static com.hyuk.coffeeserver.controller.OrderDtoConverter.toOrderDtoWithItem;

import com.hyuk.coffeeserver.dto.OrderDto;
import com.hyuk.coffeeserver.entity.OrderStatus;
import com.hyuk.coffeeserver.service.OrderService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewOrders(
        @RequestParam(name = "orderStatus", required = false) OrderStatus orderStatus,
        Model model) {

        List<OrderDto> orderDtos;
        if (orderStatus == null) {
            orderDtos = toOrderDtoList(orderService.searchOrdersOrderByCreatedAt());
        } else {
            orderDtos = toOrderDtoList(orderService.searchOrdersOrderByCreatedAt(orderStatus));
        }

        model.addAttribute("orderDtos", orderDtos);
        return "order/orders";
    }

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable("orderId") UUID orderId, Model model) {
        var orderDtoWithItem = toOrderDtoWithItem(orderService.searchOrderWithOrderItems(orderId));
        model.addAttribute("orderDtoWithItem", orderDtoWithItem);
        return "order/order";
    }

    @PostMapping("/{orderId}/change-status")
    public String changeOrderStatus(@PathVariable("orderId") UUID orderId) {
        orderService.changeOrderStatus(orderId);
        return "redirect:/orders/" + orderId.toString();
    }

    @PostMapping("/{orderId}/delete")
    public String removeOrder(@PathVariable("orderId") UUID orderId) {
        orderService.removeOrder(orderId);
        return "redirect:/orders";
    }
}
