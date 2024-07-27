package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.exception.WrongConfirmationCodeException;
import com.pdp.ecommerce.mapper.UserMapper;
import com.pdp.ecommerce.model.dto.TokenDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public HttpEntity<?> register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String token = jwtUtils.generateConfirmationToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Override
    public HttpEntity<?> checkVerificationCode(String code, String header) throws JsonProcessingException, BadRequestException {
        if (header == null || !header.startsWith("Confirmation")) {
            throw new RuntimeException("Expected TempAuthorization token in the header!");
        }
        String token = header.substring(13);
        User user = jwtUtils.getUser(token);
        if(jwtUtils.checkVerificationCode(code, token)){
            User savedUser = userRepository.save(user);
            TokenDto tokenDto = new TokenDto(
                    jwtUtils.generateToken(savedUser),
                    jwtUtils.generateRefreshToken(savedUser));
            return ResponseEntity.status(HttpStatus.CREATED).body(tokenDto);
        }else {
            throw new WrongConfirmationCodeException("Entered code is wrong! Please, try again!");
        }
    }

    @Override
    public HttpEntity<?> login(UserLoginDto userLoginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.email(), userLoginDto.password()));
        User user = (User) authenticate.getPrincipal();
        TokenDto tokenDto = new TokenDto(
                jwtUtils.generateToken(user),
                jwtUtils.generateRefreshToken(user)
        );
        return ResponseEntity.ok(tokenDto);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
