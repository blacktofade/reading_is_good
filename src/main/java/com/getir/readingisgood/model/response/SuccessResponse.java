package com.getir.readingisgood.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse {
    private String successMessage;
    private HttpStatus status;
    private LocalDateTime dateTime;
}
