package pl.sensi.news.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.sensi.news.jwt.api.JwtService;
import pl.sensi.news.jwt.api.models.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtRepository jwtRepository;

    @Value("${spring.security.key}")
    private String key;

    @Value("${spring.security.token-expiration-in-ms}")
    private long tokenExpirationInMs;

    @Override
    public void disableAllUserTokens(String id) {
        jwtRepository.deleteAllByUserAccountId(id);
    }

    @Override
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String extractTokenId(String token) {
        return extractClaim(token, claims -> claims.get("tokenId")).toString();
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(String userAccountId) {
        long currentTimeInMillis = System.currentTimeMillis();

        String tokenId = UUID.randomUUID().toString();

        String token = Jwts.builder()
                .subject(userAccountId)
                .issuedAt(new Date(currentTimeInMillis))
                .expiration(new Date(currentTimeInMillis + tokenExpirationInMs))
                .claim("tokenId", tokenId)
                .signWith(getSingInKey())
                .compact();

        disableAllUserTokens(userAccountId);
        save(Jwt.builder().tokenId(tokenId).userAccountId(userAccountId).build());

        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        final String tokenId = extractTokenId(token);
        return (userId.equals(userDetails.getUsername())) && !isTokenExpired(token) && jwtRepository.existsByTokenId(tokenId);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Jwt save(Jwt token) {
        return jwtRepository.save(token);
    }
}
