package com.example.hardware.service;

import com.example.hardware.model.Order;
import com.example.hardware.model.User;

import java.util.List;

public interface IOrderService {
    void saveOrder(Order order);
    Long getOrdersCount();
    Long getOrdersCountByUser(User user);
    List<Order> findOrdersByUserWithPagination(User user, int page, int pageSize);
    List<Order> findOrdersByUserAndConfirmationWithPagination(User user, Boolean isConfirmed, int page, int pageSize);
    List<Order> showAllOrdersWithPagination(int page, int pageSize);
    void deleteOrderById(Long id);
    Order getOrderById(Long orderId);
}