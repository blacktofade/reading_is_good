package com.getir.readingisgood.model.request;

import com.getir.readingisgood.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GiveOrderRequest {

    @NotBlank(message = "Id can not be empty!")
    private String bookId;

    @NotNull
    @Min(value = 1, message = "Quantity can not be lesser than 1 !")
    private Long quantity;

}
