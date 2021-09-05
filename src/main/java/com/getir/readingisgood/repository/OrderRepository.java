package com.getir.readingisgood.repository;

import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    Page<Order> findByUser_Id (String userId, Pageable page);

    Optional<Order> findByIdAndUser_Id (String id, String userId);

    List<Order> findByUser_IdAndOrderType (String userId, OrderType orderType);

    Page<Order> findAllByCreatedDateIsBetween(LocalDateTime dt1, LocalDateTime dt2,Pageable page);

    Page<Order> findByUser_IdAndCreatedDateIsBetween(String userId,LocalDateTime dt1, LocalDateTime dt2,Pageable page);
}
