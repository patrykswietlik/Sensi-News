package pl.sensi.news.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.sensi.news.article.AuthorityUserType;
import pl.sensi.news.jwt.JwtAuthFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityFilterChainConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final String[] permittedGetPaths = new String[]{
            "/tags/**",
            "/articles/**",
            "/comments/**"
    };

    private final String[] permittedPostPaths = new String[]{
            "/users/**",
            "/tags/**",
    };

    private final String[] adminPaths = new String[]{
            "/tags/**",
            "/articles/*/status",
            "/articles/pending"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, permittedGetPaths).permitAll()
                        .requestMatchers(HttpMethod.POST, permittedPostPaths).permitAll()
                        .requestMatchers(adminPaths).hasAuthority(AuthorityUserType.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
