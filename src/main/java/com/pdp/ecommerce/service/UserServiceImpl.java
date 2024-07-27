package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.mapper.UserMapper;
import com.pdp.ecommerce.model.dto.TokenDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
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
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
}
