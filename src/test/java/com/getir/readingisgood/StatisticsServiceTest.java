package com.getir.readingisgood;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.document.Role;
import com.getir.readingisgood.document.User;
import com.getir.readingisgood.enums.OrderType;
import com.getir.readingisgood.enums.RoleType;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.model.response.MonthlyStatsResponse;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import com.getir.readingisgood.service.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    public void getMonthlyOrderStatisticShouldReturnSuccessfully() {
        List<OrderResponse> orderList = new ArrayList<>();
        List<Order> orders = Collections.singletonList(Order.builder().id("1").build());
        LocalDateTime now = LocalDateTime.now();
        Month month = now.getMonth();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId("1");
        orderResponse.setUserName("test");
        orderResponse.setBookName("testBook");
        orderResponse.setBookPrice(20.0);
        orderResponse.setQuantity(1L);
        orderResponse.setCreatedDate(now);
        orderList.add(orderResponse);

        when(userDetailsService.getAuthenticatedUserId()).thenReturn("11");
        when(orderRepository.findByUser_IdAndOrderType(any(), any())).thenReturn(orders);
        when(orderMapper.ordersToOrderResponse(any())).thenReturn(orderResponse);

        List<MonthlyStatsResponse> monthlyStatsResponseList = statisticsService.getMonthlyStatistics();
        assertEquals(1, monthlyStatsResponseList.size());
        MonthlyStatsResponse monthlyStatsResponse = monthlyStatsResponseList.get(0);
        assertEquals(month.toString().toLowerCase(), monthlyStatsResponse.getMonth().toLowerCase());
        assertEquals(1L, monthlyStatsResponse.getTotalOrderCount());

    }
}
