package com.getir.readingisgood;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.document.Order;
import com.getir.readingisgood.document.Role;
import com.getir.readingisgood.document.User;
import com.getir.readingisgood.enums.RoleType;
import com.getir.readingisgood.exception.OrderNotFoundException;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.model.request.GiveOrderRequest;
import com.getir.readingisgood.model.response.ErrorResponse;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.model.response.SuccessResponse;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.repository.UserRepository;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import com.getir.readingisgood.service.BookService;
import com.getir.readingisgood.service.OrderService;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookService bookService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private  OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void giveOrderShouldSuccess() {
        Role role = Role.builder().id("1").roleType(RoleType.ROLE_CUSTOMER).build();
        Book book = Book.builder().id("1").bookName("Efficient Java").stock(10L).build();
        User user = User.builder().id("11").username("test").password("test")
                .email("test@mail.com").role(role).build();
        GiveOrderRequest giveOrderRequest = GiveOrderRequest.builder().quantity(1L).bookId("1").build();
        Order resultOrder = Order.builder().id("1").quantity(1L).book(book).build();
        when(orderRepository.save(any())).thenReturn(resultOrder);
        when(bookService.findByBookId(any())).thenReturn(book);
        when(userDetailsService.getAuthenticatedUserId()).thenReturn("11");
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(user));
        ResponseEntity<?> result = orderService.giveOrder(giveOrderRequest);
        assertNotNull(result.getBody());
        assertEquals(((SuccessResponse) result.getBody()).getSuccessMessage(), "Order gave ! Order id is : 1");
    }

    @Test
    public void giveOrderShouldFailWhenStockIsNotEnough() {
        Role role = Role.builder().id("1").roleType(RoleType.ROLE_CUSTOMER).build();
        Book book = Book.builder().id("1").bookName("Efficient Java").stock(10L).build();
        User user = User.builder().id("11").username("test").password("test")
                .email("test@mail.com").role(role).build();
        GiveOrderRequest giveOrderRequest = GiveOrderRequest.builder().quantity(11L).bookId("1").build();
        when(bookService.findByBookId(any())).thenReturn(book);
        when(userDetailsService.getAuthenticatedUserId()).thenReturn("11");
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(user));
        ResponseEntity<?> result = orderService.giveOrder(giveOrderRequest);
        assertNotNull(result.getBody());
        assertEquals(((ErrorResponse) result.getBody()).getErrorMessage(), "Book stock is not enough !!");
    }

    @Test
    public void getOrderByIdSuccessWhenOrderIsFound(){
        Order order = Order.builder().id("1").build();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId("1");
        orderResponse.setUserName("test");
        orderResponse.setBookName("testBook");
        orderResponse.setQuantity(1L);
        when(userDetailsService.getCurrentUserRole()).thenReturn(RoleType.ROLE_ADMIN.name());
        when(orderRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(order));
        when(orderMapper.ordersToOrderResponse(any())).thenReturn(orderResponse);
        OrderResponse result = orderService.getOrderById("1");
        assertEquals(result.getOrderId(),orderResponse.getOrderId());
        assertEquals(result.getBookName(),orderResponse.getBookName());
    }

    @Test(expected = OrderNotFoundException.class)
    public void getOrderByIdFailWhenOrderIsNotFound(){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId("1");
        orderResponse.setUserName("test");
        orderResponse.setBookName("testBook");
        orderResponse.setQuantity(1L);
        when(userDetailsService.getCurrentUserRole()).thenReturn(RoleType.ROLE_ADMIN.name());
        OrderResponse result = orderService.getOrderById("1");
        assertEquals(result.getOrderId(),orderResponse.getOrderId());
        assertEquals(result.getBookName(),orderResponse.getBookName());
    }
}
