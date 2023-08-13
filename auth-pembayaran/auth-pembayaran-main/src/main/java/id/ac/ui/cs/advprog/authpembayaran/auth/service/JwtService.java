package id.ac.ui.cs.advprog.authpembayaran.auth.service;

import io.jsonwebtoken.Claims;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    Key getSignInKey();

    Claims extractAllClaims(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenValid(String token, UserDetails userDetails);

    String extractUserRole(String token);

    Object extractValue(String token, String key);
}