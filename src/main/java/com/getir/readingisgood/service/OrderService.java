package com.getir.readingisgood.service;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.document.User;
import com.getir.readingisgood.enums.OrderType;
import com.getir.readingisgood.enums.RoleType;
import com.getir.readingisgood.exception.OrderNotFoundException;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.model.request.GiveOrderRequest;
import com.getir.readingisgood.model.request.UpdateBookRequest;
import com.getir.readingisgood.model.response.ErrorResponse;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.model.response.SuccessResponse;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.repository.UserRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final UserDetailsServiceImpl userDetailsService;
    private final OrderMapper orderMapper;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public ResponseEntity<?> giveOrder(GiveOrderRequest giveOrderRequest) {
        String userId = userDetailsService.getAuthenticatedUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookService.findByBookId(giveOrderRequest.getBookId());
        Order order = Order.builder().user(user).book(book).
                quantity(giveOrderRequest.getQuantity()).orderType(OrderType.PURCHASED).build();
        if (book.getStock() < giveOrderRequest.getQuantity()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Book stock is not enough !!",
                            HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        } else {
            String orderId = orderRepository.save(order).getId();
            UpdateBookRequest updateStock = UpdateBookRequest.builder().id(book.getId()).stock(book.getStock() - giveOrderRequest.getQuantity())
                    .name(book.getBookName()).price(book.getPrice()).build();
            bookService.updateBook(updateStock);
            return ResponseEntity.ok()
                    .body(new SuccessResponse("Order gave ! Order id is : " + orderId,
                            HttpStatus.OK, LocalDateTime.now()));
        }
    }

    @Transactional
    public ResponseEntity<?> cancelOrder(String orderId) {
        Order order = null;
        if (userDetailsService.getCurrentUserRole().equals(RoleType.ROLE_ADMIN.name())) {
            order = orderRepository.findById(orderId).orElseThrow(() ->
                    new OrderNotFoundException("Order not found with that Id "));
        } else {
            String userId = userDetailsService.getAuthenticatedUserId();
            order = orderRepository.findByIdAndUser_Id(orderId, userId).orElseThrow(() ->
                    new OrderNotFoundException("Order not found with that Id"));
        }
        if (order.getOrderType() != OrderType.CANCELLED) {
            Query cancelOrderQuery = new Query();
            cancelOrderQuery.addCriteria(Criteria.where("_id").is(order.getId()));
            Update update = new Update();
            update.set("orderType", OrderType.CANCELLED);
            mongoTemplate.updateFirst(cancelOrderQuery, update, Order.class);
            UpdateBookRequest updateStock = UpdateBookRequest.builder()
                    .id(order.getBook().getId())
                    .stock(order.getBook().getStock() + order.getQuantity())
                    .name(order.getBook().getBookName())
                    .price(order.getBook().getPrice())
                    .build();
            bookService.updateBook(updateStock);
            return ResponseEntity.ok()
                    .body(new SuccessResponse("Order cancelled successfully! Id is : " + orderId,
                            HttpStatus.OK, LocalDateTime.now()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Order is already cancelled before!",
                            HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }


    }

    @Transactional
    public Page<OrderResponse> listOrdersByDate(Date startDate, Date endDate, Pageable pageRequest) {
        LocalDateTime startLocalDate = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        if (userDetailsService.getCurrentUserRole().equals(RoleType.ROLE_ADMIN.name())) {
            return orderRepository.findAllByCreatedDateIsBetween(startLocalDate, endLocalDate, pageRequest)
                    .map(orderMapper::ordersToOrderResponse);
        } else {
            String userId = userDetailsService.getAuthenticatedUserId();
            return orderRepository.findByUser_IdAndCreatedDateIsBetween(userId, startLocalDate, endLocalDate, pageRequest)
                    .map(orderMapper::ordersToOrderResponse);
        }
    }

    @Transactional
    public OrderResponse getOrderById(String orderId) {
        if (userDetailsService.getCurrentUserRole().equals(RoleType.ROLE_ADMIN.name())) {
            return orderMapper.ordersToOrderResponse(orderRepository.findById(orderId).orElseThrow(() ->
                    new OrderNotFoundException("Order not found with that Id")));
        } else {
            String userId = userDetailsService.getAuthenticatedUserId();
            return orderMapper.ordersToOrderResponse(orderRepository.findByIdAndUser_Id(orderId, userId).orElseThrow(() ->
                    new OrderNotFoundException("Order not found with that Id")));
        }
    }
}
