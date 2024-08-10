package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.exception.UserAlreadyExistException;
import com.pdp.ecommerce.exception.WrongConfirmationCodeException;
import com.pdp.ecommerce.mapper.UserMapper;
import com.pdp.ecommerce.model.dto.TokenDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.model.projection.ProductProjection;
import com.pdp.ecommerce.repository.AddressRepository;
import com.pdp.ecommerce.repository.OrderRepository;
import com.pdp.ecommerce.repository.ProductRepository;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private final ProductRepository productRepository;
    private AddressRepository addressRepository;
    private  OrderService orderService;

    @Autowired
    public void setOrderService(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @SneakyThrows
    public HttpEntity<?> register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User has already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String token = jwtUtils.generateConfirmationToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Override
    public HttpEntity<?> checkVerificationCode(String code, String header) throws JsonProcessingException, BadRequestException {
        if (header == null || !header.startsWith("Confirmation")) {
            throw new RuntimeException("Expected Confirmation token in the header!");
        }
        String token = header.substring(13);
        User user = jwtUtils.getUser(token);
        if (jwtUtils.checkVerificationCode(code, token)) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        } else {
            throw new WrongConfirmationCodeException("Entered code is wrong! Please, try again!");
        }
    }

    @Override
    public HttpEntity<?> login(UserLoginDto userLoginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.email(), userLoginDto.password())
            );
            User user = (User) authenticate.getPrincipal();
            TokenDto tokenDto = new TokenDto(
                    jwtUtils.generateToken(user),
                    jwtUtils.generateRefreshToken(user)
            );
            return ResponseEntity.ok(tokenDto);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email or password is incorrect!");
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsersByRole(String role) {
        return userRepository.findByRoles(role);
    }

    @Override
    public Optional<User> getSignedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    @Override
    public HttpEntity<?> getUserSearchHistory() {
        Optional<User> userOpt = getSignedUser();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getSearchHistoryString() != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<String> fullHistory = objectMapper.readValue(user.getSearchHistoryString(), new TypeReference<>() {
                    });
                    int size = fullHistory.size();
                    return ResponseEntity.ok(size > 10 ? fullHistory.subList(size - 10, size) : fullHistory);
                } catch (JsonProcessingException e) {
                    log.error("Error processing JSON", e);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Override
    public void updateUserSearchHistory(String keyword) {
        Optional<User> userOpt = getSignedUser();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> history = new ArrayList<>();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                if (user.getSearchHistoryString() != null) {
                    history = objectMapper.readValue(user.getSearchHistoryString(), new TypeReference<>() {
                    });
                }

                if (!history.contains(keyword)) {
                    history.add(keyword);
                    user.setSearchHistoryString(objectMapper.writeValueAsString(history));
                    userRepository.save(user);
                }
            } catch (JsonProcessingException e) {
                log.error("Error processing JSON", e);
            }
        }
    }

    @Override
    public HttpEntity<?> getWishlist() {
        Optional<User> signedUser = getSignedUser();
        if (signedUser.isPresent()) {
            User user = signedUser.get();
            List<ProductProjection> products = userRepository.getUserFavouriteProducts(user.getId());
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

    @Override
    public HttpEntity<?> addToWishlist(UUID productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            getSignedUser().ifPresent(user -> user.getFavouriteProducts().add(product.get()));
            User user = getSignedUser().get();
            userRepository.save(user);
            return ResponseEntity.ok(product);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @Override
    public HttpEntity<?> clearWishlist() {
        getSignedUser().ifPresent(user -> user.getFavouriteProducts().clear());
        User user = getSignedUser().get();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }


    @Override
    @Transactional
    public ResponseEntity<String> removeFavouriteProduct(UUID id) {
        User currentUser = getSignedUser().orElseThrow(() -> new RuntimeException("User is not signed in"));

        List<Product> favouriteProducts = new ArrayList<>(currentUser.getFavouriteProducts());

        List<Product> newFavourite = favouriteProducts.stream()
                .filter(product -> !product.getId().equals(id))
                .collect(Collectors.toList());

        currentUser.setFavouriteProducts(newFavourite);
        userRepository.save(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }




    @Override
    public HttpEntity<?> changeAddress(Double lat, Double lon) {
        Optional<User> signedUser = getSignedUser();
        if (signedUser.isPresent()) {
            User user = signedUser.get();
            Address address = Address.builder()
                    .latitude(lat)
                    .longitude(lon)
                    .build();
            addressRepository.save(address);
            user.setAddress(address);
            userRepository.save(user);
            return ResponseEntity.ok("success");
        } else throw new RuntimeException("User is not signed in");
    }

    @Override
    @Transactional
    public HttpEntity<?> getOrders() {
        User user = getSignedUser().orElseThrow(() -> new RuntimeException("User is not signed in"));
        return orderService.findByUserOrders(user.getId());

    }


    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }



}


