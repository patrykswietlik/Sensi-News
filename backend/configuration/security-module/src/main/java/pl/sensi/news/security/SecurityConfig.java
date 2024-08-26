package pl.sensi.news.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.user.account.UserAccountRepository;
import pl.sensi.news.user.account.api.models.UserAccount;

@Configuration
@RequiredArgsConstructor
class SecurityConfig {

    private final UserAccountRepository userAccountRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return id -> {
            UserAccount optionalUser = userAccountRepository.getById(id)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

            return User.withUsername(optionalUser.id())
                    .password(optionalUser.password())
                    .authorities(optionalUser.role())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
