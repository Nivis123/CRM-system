package com.example.crm.repository;

import com.example.crm.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
//Это интерфейс для аналитики суммы по продавцам за определнный период времени.Сделал через список.
public interface SellerRepository extends JpaRepository<Seller, Long> {

    @Query("SELECT s, SUM(t.amount) FROM Seller s JOIN s.transactions t WHERE t.transactionDate BETWEEN :start AND :end GROUP BY s ORDER BY SUM(t.amount) DESC")
    List<Object[]> findSellersWithTotalAmountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}