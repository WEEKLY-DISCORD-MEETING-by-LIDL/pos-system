package wdmsystem.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody Login request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//        String token = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new LoginResponse(token));
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login request) {
        log.info("Login attempt for username: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            log.info("Authentication successful for username: {}", request.getUsername());

            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            log.info("Token generated for username: {}", request.getUsername());

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
//            log.error("Login failed for username: {}", request.getUsername(), e);  //this logs a way too long exception paragraph
            log.error("Login failed for username: {}", request.getUsername());
            return ResponseEntity.status(401).build();
        }
    }

}
