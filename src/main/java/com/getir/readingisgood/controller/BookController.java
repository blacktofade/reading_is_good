package com.getir.readingisgood.controller;


import com.getir.readingisgood.model.request.InsertBookRequest;
import com.getir.readingisgood.model.request.UpdateBookRequest;
import com.getir.readingisgood.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/insertBook")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> insertBook(@Valid @RequestBody InsertBookRequest insertBookRequest) {
        return bookService.insertBook(insertBookRequest);

    }

    @PostMapping("/updateBook")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBook(@Valid @RequestBody UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(updateBookRequest);

    }
}
