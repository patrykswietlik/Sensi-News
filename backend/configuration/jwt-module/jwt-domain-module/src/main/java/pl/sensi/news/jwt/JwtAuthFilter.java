package pl.sensi.news.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.jwt.api.JwtService;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.replace(BEARER, "");
        String userId;

        try {
            userId = jwtService.extractUserId(token);
        } catch (MalformedJwtException e) {
            exceptionHandler(response, ErrorCode.JWT_MALFORMED);
            return;
        } catch (ExpiredJwtException e) {
            exceptionHandler(response, ErrorCode.JWT_EXPIRED);
            return;
        } catch (SignatureException e) {
            exceptionHandler(response, ErrorCode.JWT_INVALID);
            return;
        }

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;

            try {
                userDetails = userDetailsService.loadUserByUsername(userId);
            } catch (UsernameNotFoundException e) {
                exceptionHandler(response, ErrorCode.JWT_INVALID);
                return;
            }

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                exceptionHandler(response, ErrorCode.JWT_INVALID);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void exceptionHandler(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json");

        var json = new JSONObject()
                .put("message", errorCode.getMessage())
                .put("timestamp", LocalDateTime.now())
                .put("errorType", errorCode.name());

        response.getWriter().write(json.toString());
    }

}
