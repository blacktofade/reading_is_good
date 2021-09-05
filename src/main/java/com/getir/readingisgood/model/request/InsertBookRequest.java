package com.getir.readingisgood.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class InsertBookRequest {

    @NotBlank(message = "Book name can not be empty!")
    private String name;

    @NotNull(message = "Stock can not be null!")
    @Min(value = 0, message = "Stock must be equal or greater than 0 !")
    private Long stock;

    @NotNull(message = "Price can not be null!")
    @Min(value = 0, message = "Price must be equal or greater than 0 !")
    private Double price;

}
