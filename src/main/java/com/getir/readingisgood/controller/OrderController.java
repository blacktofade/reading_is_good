package com.getir.readingisgood.controller;

import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.model.request.GiveOrderRequest;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/giveOrder")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> giveOrder(@Valid @RequestBody GiveOrderRequest giveOrderRequest) {
        return orderService.giveOrder(giveOrderRequest);
    }

    @PostMapping("/cancelOrder/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") String orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/getOrderById/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/listOrdersByDate")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<?>> listOrdersByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                                @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(orderService.listOrdersByDate(startDate,endDate,pageRequest));
    }
}
