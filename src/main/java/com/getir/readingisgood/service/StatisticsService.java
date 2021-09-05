package com.getir.readingisgood.service;

import com.getir.readingisgood.enums.OrderType;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.model.response.MonthlyStatsResponse;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final OrderMapper orderMapper;

    public List<MonthlyStatsResponse> getMonthlyStatistics() {
        String userId = userDetailsService.getAuthenticatedUserId();
        List<OrderResponse> orders = orderRepository.findByUser_IdAndOrderType(userId, OrderType.PURCHASED)
                .stream().map(orderMapper::ordersToOrderResponse).collect(Collectors.toList());

        Map<Integer, List<OrderResponse>> ordersGroupedByMonth = orders.stream()
                .collect(groupingBy(order -> order.getCreatedDate().get(ChronoField.MONTH_OF_YEAR)));

        DecimalFormat amountFormat = new DecimalFormat("#.##");
        List<MonthlyStatsResponse> monthlyStatsResponseList = new ArrayList<>();
        for (var monthName : ordersGroupedByMonth.entrySet()) {

            String month = Month.of(monthName.getKey()).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
            int totalBookCount = 0;
            double totalPurchasedAmount = 0.0;

            for (OrderResponse orderResponse : monthName.getValue()) {
                totalBookCount += orderResponse.getQuantity();
                totalPurchasedAmount += orderResponse.getBookPrice() * orderResponse.getQuantity();
            }

            monthlyStatsResponseList.add(
                    MonthlyStatsResponse.builder()
                            .month(month)
                            .totalOrderCount(monthName.getValue().size())
                            .totalBookCount(totalBookCount)
                            .totalAmountOfPurchasements(Double.valueOf(amountFormat.format(totalPurchasedAmount)))
                            .build()
            );
        }

        return monthlyStatsResponseList;
    }
}
