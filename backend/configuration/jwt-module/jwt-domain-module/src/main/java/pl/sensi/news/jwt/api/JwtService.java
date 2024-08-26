package pl.sensi.news.jwt.api;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    void disableAllUserTokens(String id);
    String extractUserId(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(String email);
    boolean isTokenValid(String token, UserDetails userDetails);
}
