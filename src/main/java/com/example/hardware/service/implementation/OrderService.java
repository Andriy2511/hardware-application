package com.example.hardware.service.implementation;

import com.example.hardware.model.Component;
import com.example.hardware.model.Order;
import com.example.hardware.model.User;
import com.example.hardware.repository.OrderRepository;
import com.example.hardware.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    @Override
    public Long getOrdersCount() {
        return orderRepository.count();
    }

    @Override
    public Long getOrdersCountByUser(User user) {
        return orderRepository.countOrdersByUser(user);
    }

    @Override
    public List<Order> findOrdersByUserWithPagination(User user, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderRepository.findAllByUser(user, pageable);
    }

    @Override
    public List<Order> findOrdersByUserAndConfirmationWithPagination(User user, Boolean isConfirmed, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderRepository.findAllByUserAndIsConfirmed(user, isConfirmed, pageable);
    }

    @Override
    public List<Order> showAllOrdersWithPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderRepository.findAll(pageable).getContent();
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findOrderById(orderId).get();
    }
}
