package verzelEvents.controller;

import verzelEvents.dto.request.CreateStaffRequest;
import verzelEvents.dto.request.LoginRequest;
import verzelEvents.dto.request.RegisterRequest;
import verzelEvents.dto.response.AuthResponse;
import verzelEvents.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/staff")
    public ResponseEntity<AuthResponse> criarStaff(@Valid @RequestBody CreateStaffRequest request) {
        AuthResponse response = authService.criarStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}