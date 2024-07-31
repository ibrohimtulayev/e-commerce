package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.Role;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.exception.UserAlreadyExistException;
import com.pdp.ecommerce.exception.WrongConfirmationCodeException;
import com.pdp.ecommerce.mapper.UserMapper;
import com.pdp.ecommerce.model.dto.TokenDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

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

    @SneakyThrows
    @Override
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
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginDto.email(), userLoginDto.password()));
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
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAllUsersByRole(String role) {
        return userRepository.findByRoles(role);
    }

    @Override
    public Optional<User> getSignedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }



    public List<String> getUserSearchHistory() {
        Optional<User> userOpt = getSignedUser();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getSearchHistoryString() != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<String> fullHistory = objectMapper.readValue(user.getSearchHistoryString(), new TypeReference<>() {});
                    int size = fullHistory.size();
                    return size > 10 ? fullHistory.subList(size - 10, size) : fullHistory; // get last 10 search history
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }

    public void updateUserSearchHistory(String keyword) {
        Optional<User> userOpt = getSignedUser();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> history = getUserSearchHistory();

            // Check if the keyword is already in the search history
            if (!history.contains(keyword)) {
                history.add(keyword); // Add keyword only if it is not already present
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    user.setSearchHistoryString(objectMapper.writeValueAsString(history)); // Serialize back to string
                    userRepository.save(user); // Save the updated user entity
                } catch (JsonProcessingException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }
    }




}
