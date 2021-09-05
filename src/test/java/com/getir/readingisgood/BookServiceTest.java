package com.getir.readingisgood;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.model.request.InsertBookRequest;
import com.getir.readingisgood.model.request.UpdateBookRequest;
import com.getir.readingisgood.model.response.SuccessResponse;
import com.getir.readingisgood.repository.BookRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import com.getir.readingisgood.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private BookService bookService;

    @Test
    public void insertBookShouldInsertSuccessfully() {
        InsertBookRequest insertBookRequest = InsertBookRequest.builder().name("Efficient Java").stock(10L).build();
        Book book = Book.builder().id("1").bookName("Efficient Java").stock(10L).build();
        when(bookRepository.save(any())).thenReturn(book);
        ResponseEntity<?> result = bookService.insertBook(insertBookRequest);
        assertNotNull(result.getBody());
        assertEquals(((SuccessResponse) result.getBody()).getSuccessMessage(), "Book inserted successfully! Id is : 1");
    }

    @Test
    public void updateBookShouldInsertSuccessfully() {
        UpdateBookRequest updateBookRequest = UpdateBookRequest.builder().id("1L").name("Efficient Java").stock(10L).build();
        Book book = Book.builder().id("1").bookName("Efficient Java").stock(10L).build();
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(book));
        when(userDetailsService.getAuthenticatedUserId()).thenReturn("11");
        bookService.updateBook(updateBookRequest);
        verify(mongoTemplate, times(1)).updateFirst(any(), any(), (Class<?>) any());
    }

    @Test
    public void findByIdShouldReturnSuccess() {
        Book book = Book.builder().id("1").bookName("Efficient Java").stock(10L).build();
        when(bookRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(book));
        Book bookResult = bookService.findByBookId("1");
        assertNotNull(book);
        assertEquals(bookResult.getId(), book.getId());
    }

}
