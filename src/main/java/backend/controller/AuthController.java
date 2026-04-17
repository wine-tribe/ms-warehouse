package backend.controller;

import audit.api.annotation.Audit;
import audit.api.annotation.AuditParam;
import backend.security.model.AuthenticationRequest;
import backend.security.model.AuthenticationResponse;
import backend.security.model.RegisterRequest;
import backend.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Audit("WAREHOUSE_AUTH_REGISTER")
    @PostMapping("/register")
    public ResponseEntity<String> register(@AuditParam("request") @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok("Регистрация прошла успешно. Теперь войдите в систему.");
    }

    @Audit("WAREHOUSE_AUTH_AUTHENTICATE")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@AuditParam("request") @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

