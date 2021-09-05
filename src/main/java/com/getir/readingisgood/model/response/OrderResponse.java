package com.getir.readingisgood.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private String userName;
    private String orderId;
    private String bookName;
    private Double bookPrice;
    private Long quantity;
    private String orderType;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
