package com.getir.readingisgood.service;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.exception.OrderNotFoundException;
import com.getir.readingisgood.model.request.InsertBookRequest;
import com.getir.readingisgood.model.request.UpdateBookRequest;
import com.getir.readingisgood.model.response.ErrorResponse;
import com.getir.readingisgood.model.response.SuccessResponse;
import com.getir.readingisgood.repository.BookRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final MongoTemplate mongoTemplate;
    private final UserDetailsServiceImpl userDetailsService;
    private final BookRepository bookRepository;

    @Transactional
    public ResponseEntity<?> insertBook(InsertBookRequest insertBookRequest) {
        Book book = Book.builder().bookName(insertBookRequest.getName())
                .stock(insertBookRequest.getStock()).price(insertBookRequest.getPrice())
                .build();
        String bookId = bookRepository.save(book).getId();
        log.trace("New book inserted! ID : {} , USER : {} , TIME : {}",bookId,
                userDetailsService.getAuthenticatedUserId(),LocalDateTime.now());
        return ResponseEntity.ok()
                .body(new SuccessResponse("Book inserted successfully! Id is : " + bookId,
                HttpStatus.OK, LocalDateTime.now()));
    }

    @Transactional
    public ResponseEntity<?> updateBook(UpdateBookRequest updateBookRequest) {
        if (bookRepository.findById(updateBookRequest.getId()).isPresent()) {
            Query updateQuery = new Query();
            updateQuery.addCriteria(Criteria.where("_id").is(updateBookRequest.getId()));
            Update update = new Update();
            update.set("stock", updateBookRequest.getStock());
            update.set("bookName", updateBookRequest.getName());
            update.set("price", updateBookRequest.getPrice());
            mongoTemplate.updateFirst(updateQuery, update, Book.class);
            log.trace("Book updated! ID : {} , USER : {} , TIME : {}",updateBookRequest.getId(),
                    userDetailsService.getAuthenticatedUserId(),LocalDateTime.now());
            return ResponseEntity.ok().body(new SuccessResponse("Book updated successfully!",
                    HttpStatus.OK, LocalDateTime.now()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Book can't found", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }
    }

    @Transactional
    public Book findByBookId(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Book not found with that Id"));
    }
}
