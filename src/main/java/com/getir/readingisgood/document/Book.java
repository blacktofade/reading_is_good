package com.getir.readingisgood.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Document(collection= "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends AuditDocument {
    @Id
    private String id;

    @NotBlank(message = "Book name can not be empty!")
    private String bookName;

    @NotNull(message = "Stock can not be null!")
    @Min(value = 0, message = "Stock must be equal or greater than 0 !")
    private Long stock;

    @NotNull(message = "Price can not be null!")
    @Min(value = 0, message = "Price must be equal or greater than 0 !")
    private Double price;

}
