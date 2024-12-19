package wdmsystem.auth;

import wdmsystem.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (jwt != null) {
            try {
                Claims claims = jwtUtil.validateToken(jwt);
                String username = claims.getSubject();
                List<String> roles = parseRoles(claims.get("roles"));
                Integer merchantId = claims.get("merchantId", Integer.class);
                Integer employeeId = claims.get("employeeId", Integer.class);
                
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                CustomUserDetails userDetails = new CustomUserDetails(username, null, merchantId, employeeId, authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.err.println("JWT Validation failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<String> parseRoles(Object roles) {
        List<String> rolesList = new ArrayList<>();

        if (roles instanceof List<?> rolesListFromJwt) {
            for (Object roleObject : rolesListFromJwt) {
                if (roleObject instanceof LinkedHashMap<?, ?> roleMap) {
                    Object authority = roleMap.get("authority");
                    if (authority != null) {
                        rolesList.add(authority.toString());
                    }
                } else {
                    rolesList.add(roleObject.toString());
                }
            }
        }
        if (rolesList.isEmpty()) {
            throw new UnauthorizedException("No roles found in JWT.");
        }
        return rolesList;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

