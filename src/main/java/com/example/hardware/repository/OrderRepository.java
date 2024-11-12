package com.example.hardware.repository;

import com.example.hardware.model.Component;
import com.example.hardware.model.Order;
import com.example.hardware.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
    List<Order> findAllByComponentAndIsConfirmed(Component component, Boolean isConfirmed);
    List<Order> findAllByUser(User user, Pageable pageable);
    List<Order> findAllByUserAndIsConfirmed(User user, Boolean confirmed, Pageable pageable);
    void deleteById(Long id);
    Long countOrdersByUser(User user);
    Optional<Order> findOrderById(Long id);
    @Modifying
    @Query("UPDATE Order o SET o.isConfirmed = true WHERE o.id = :userOrderId")
    void setStatusReturnedTrueForOrderById(@Param("userOrderId") Long userOrderId);
}
