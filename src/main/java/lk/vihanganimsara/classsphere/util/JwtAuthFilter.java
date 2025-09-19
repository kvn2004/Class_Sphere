package lk.vihanganimsara.classsphere.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Log request URI for debugging
        logger.info("Processing request: {}", request.getRequestURI());

        // Skip authentication for public endpoints
        String path = request.getRequestURI();
        if (path.equals("/api/auth/register") || path.equals("/api/auth/login")) {
            logger.debug("Skipping authentication for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = null;
        String username = null;

        // Check Authorization header first
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            logger.debug("JWT token found in Authorization header: {}", jwtToken);
        } else {
            // Check cookie if no header
            if (request.getCookies() != null) {
                jwtToken = Arrays.stream(request.getCookies())
                        .filter(cookie -> "token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
                if (jwtToken != null) {
                    logger.debug("JWT token found in cookie: {}", jwtToken);
                }
            }
        }

        // If no token, proceed without authentication
        if (jwtToken == null) {
            logger.debug("No JWT token found in request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Extract username from token
        try {
            username = jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            logger.error("Invalid JWT token for {}: {}", request.getRequestURI(), e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Authenticate if username exists and no authentication is set
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication set for user: {}", username);
            } else {
                logger.debug("Invalid JWT token for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}