package com.example.hardware.controller;

import com.example.hardware.component.ListPaginationData;
import com.example.hardware.model.*;
import com.example.hardware.service.*;
import com.example.hardware.service.implementation.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final IUserService userService;
    private final IOrderService orderService;
    private final IComponentService componentService;
    private final ListPaginationData listPaginationData;

    @Autowired
    public UserController(UserService userService, IOrderService orderService, IComponentService componentService, ListPaginationData listPaginationData) {
        this.userService = userService;
        this.orderService = orderService;
        this.componentService = componentService;
        this.listPaginationData = listPaginationData;
    }

    @GetMapping("/showUserCard")
    public String showUserCard(Model model, Principal principal) {
        User user = userService.findUserByName(principal.getName());
        listPaginationData.setTotalRecords(orderService.getOrdersCountByUser(user));
        model.addAttribute("orders", orderService.findOrdersByUserAndConfirmationWithPagination(user, false, listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "user/user-card";
    }

    @GetMapping("/showUserOrders")
    public String showUserConfirmedOrder(Model model, Principal principal) {
        User user = userService.findUserByName(principal.getName());
        listPaginationData.setTotalRecords(orderService.getOrdersCountByUser(user));
        model.addAttribute("orders", orderService.findOrdersByUserAndConfirmationWithPagination(user, true, listPaginationData.getPage(), listPaginationData.getPageSize()));
        return "user/user-order";
    }

    @PostMapping("/cancelOrder/{id}")
    public String cancelOrder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if(Objects.equals(orderService.getOrderById(id).getUser().getId(), user.getId())) {
            orderService.deleteOrderById(id);
        }
        return "redirect:/user/showUserCard";
    }

    @PostMapping("/confirmOrder/{id}")
    public String confirmOrder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if(Objects.equals(orderService.getOrderById(id).getUser().getId(), user.getId())) {
            Order order = orderService.getOrderById(id);
            order.setConfirmed(true);
            orderService.saveOrder(order);
        }
        return "redirect:/user/showUserCard";
    }

    @PostMapping("/addOrder/{componentId}")
    public String addNewOrder(@PathVariable Long componentId, @AuthenticationPrincipal User user) {

        if(user.getRole().getName().equals("USER")) {
            Component component = componentService.findComponentById(componentId);

            Order order = new Order();
            order.setUser(user);
            order.setComponent(component);
            order.setConfirmed(false);
            orderService.saveOrder(order);
        }
        return "redirect:/catalog/showComponentsCatalog";
    }
}
