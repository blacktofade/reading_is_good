package com.getir.readingisgood.mapper;

import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.model.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "bookName", source = "book.bookName")
    @Mapping(target = "bookPrice", source = "book.price")
    OrderResponse ordersToOrderResponse(Order order);

}
