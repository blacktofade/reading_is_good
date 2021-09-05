package com.getir.readingisgood.service;

import com.getir.readingisgood.document.User;
import com.getir.readingisgood.enums.RoleType;
import com.getir.readingisgood.exception.RoleNotFoundException;
import com.getir.readingisgood.mapper.OrderMapper;
import com.getir.readingisgood.model.response.AuthenticationResponse;
import com.getir.readingisgood.model.request.LoginRequest;
import com.getir.readingisgood.model.request.SignupRequest;
import com.getir.readingisgood.model.response.ErrorResponse;
import com.getir.readingisgood.model.response.OrderResponse;
import com.getir.readingisgood.model.response.SuccessResponse;
import com.getir.readingisgood.repository.OrderRepository;
import com.getir.readingisgood.repository.RoleRepository;
import com.getir.readingisgood.repository.UserRepository;
import com.getir.readingisgood.security.JwtTokenUtil;
import com.getir.readingisgood.security.UserDetailsImpl;
import com.getir.readingisgood.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthenticationResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @Transactional
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("This Username is already taken!",
                            HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("This email already taken!",
                            HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());
        if (userRepository.count() == 0) {
            user.setRole(roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(() ->
                    new RoleNotFoundException("This role not found in database.")));
        } else {
            user.setRole(roleRepository.findByRoleType(RoleType.ROLE_CUSTOMER).orElseThrow(() ->
                    new RoleNotFoundException("This role not found in database.")));
        }
        userRepository.save(user);
        log.trace("New user registered! ID : {} ,TIME : {}",user.getId(),LocalDateTime.now());
        return ResponseEntity.ok(new SuccessResponse("User registered successfully!",
                HttpStatus.OK, LocalDateTime.now()));
    }

    @Transactional
    public Page<OrderResponse> getOrders(Pageable pageRequest) {
        if (userDetailsService.getCurrentUserRole().equals(RoleType.ROLE_ADMIN.name())) {
            return orderRepository.findAll(pageRequest).map(orderMapper::ordersToOrderResponse);
        } else {
            String userId = userDetailsService.getAuthenticatedUserId();
            return orderRepository.findByUser_Id(userId, pageRequest).map(orderMapper::ordersToOrderResponse);
        }
    }

}
