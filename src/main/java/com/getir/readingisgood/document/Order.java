package com.getir.readingisgood.document;

import com.getir.readingisgood.enums.OrderType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Document(collection= "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AuditDocument {
    @Id
    private String id;

    @NotNull(message = "Stock can not be null!")
    @Min(value = 1, message = "Stock must be equal or greater than 1!")
    private Long quantity;

    @DBRef
    private User user;

    @DBRef
    private Book book;

    private OrderType orderType;
}
