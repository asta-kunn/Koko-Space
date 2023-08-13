package id.ac.ui.cs.advprog.coworkingspace.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import id.ac.ui.cs.advprog.coworkingspace.auth.model.JwtPayload;
import id.ac.ui.cs.advprog.coworkingspace.auth.service.JwtService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        if (authHeader == null || !authHeader.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);

        List<GrantedAuthority> authorities = new ArrayList<>();

        String role = jwtService.extractUserRole(jwtToken);
        Integer userId = jwtService.extractUserId(jwtToken);
        String name = jwtService.extractValue(jwtToken, "name").toString();
        Boolean active = (Boolean) jwtService.extractValue(jwtToken, "active");
        Double saldo = Double.parseDouble(jwtService.extractValue(jwtToken, "saldo").toString());
        authorities.add(new SimpleGrantedAuthority(role));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtPayload jwtPayload = new JwtPayload(userId, role, name, active, saldo);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                null,
                jwtPayload,
                authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
