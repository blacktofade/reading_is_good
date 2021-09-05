package com.getir.readingisgood.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MonthlyStatsResponse {

    private String month;
    private int totalOrderCount;
    private int totalBookCount;
    private Double totalAmountOfPurchasements;
}
