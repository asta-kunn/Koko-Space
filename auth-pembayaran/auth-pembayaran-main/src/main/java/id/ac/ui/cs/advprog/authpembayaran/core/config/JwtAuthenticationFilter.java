package id.ac.ui.cs.advprog.authpembayaran.core.config;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.service.JwtService;
import id.ac.ui.cs.advprog.authpembayaran.core.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.core.model.JwtPayload;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Do not change this code
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String JWT_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(JWT_HEADER);
        final String jwtToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        try {
            userEmail = jwtService.extractClaim(jwtToken, Claims::getSubject);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(jwtService.extractUserRole(jwtToken)));
            String role = jwtService.extractUserRole(jwtToken);
            Integer id = Integer.parseInt(jwtService.extractValue(jwtToken, "id").toString());
            String name = jwtService.extractValue(jwtToken, "name").toString();
            Boolean active = Boolean.parseBoolean(jwtService.extractValue(jwtToken, "active").toString());
            Boolean verified = Boolean.parseBoolean(jwtService.extractValue(jwtToken, "verified").toString());
            Double saldo = Double.parseDouble(jwtService.extractValue(jwtToken, "saldo").toString());
            User user = User.builder().saldo(saldo).email(userEmail).verified(verified).active(active).id(id).build();
            JwtPayload jwtPayload = new JwtPayload(id, role, name, active, saldo);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, jwtPayload,
                    authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }
}
