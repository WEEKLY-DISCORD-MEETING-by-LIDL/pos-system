package wdmsystem.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import wdmsystem.auth.CustomUserDetailsService;
import wdmsystem.auth.JwtAuthTokenFilter;
import wdmsystem.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthTokenFilter jwtAuthTokenFilter, CustomUserDetailsService customUserDetailsService,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthTokenFilter = jwtAuthTokenFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Disable CSRF (for development; re-enable for production)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/employees/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers("/categories/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/orders/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/orderDiscounts/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/discounts/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/variants/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers("/customers/**").hasAnyRole("ADMIN", "OWNER","REGULAR")
                        .requestMatchers("/merchants/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/pay/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/reservations/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/services/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .requestMatchers("/tax/**").hasAnyRole("ADMIN", "OWNER", "REGULAR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Origin", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
