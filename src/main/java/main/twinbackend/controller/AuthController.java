package main.twinbackend.controller;

import lombok.RequiredArgsConstructor;
import main.twinbackend.dto.LoginRequest;
import main.twinbackend.service.JwtService;
import main.twinbackend.service.TokenStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        if (!"admin".equals(request.getUsername())
                || !"admin".equals(request.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sai tài khoản hoặc mật khẩu");
        }

        String token = jwtService.generateToken("admin");

        return ResponseEntity.ok(
                Map.of("token", token));
    }
}