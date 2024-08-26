package pl.sensi.news.user.account;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.article.WebClientType;
import pl.sensi.news.jwt.api.JwtService;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountLoginForm;
import pl.sensi.news.user.account.api.models.UserAccountRegisterForm;
import pl.sensi.news.user.account.api.models.UserAccountResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final ReCaptchaValidator reCaptchaValidator;

    @Override
    public String register(UserAccountRegisterForm form, String token) {
        reCaptchaValidator.verifyToken(token);

        Optional<UserAccount> optionalUserAccountByEmail = userAccountRepository.getByEmail(form.email());
        if (optionalUserAccountByEmail.isPresent()) {
            throw new ApplicationException(ErrorCode.USER_ACCOUNT_ALREADY_EXISTS);
        }

        Optional<UserAccount> optionalUserAccountByUsername = userAccountRepository.getByUsername(form.username());
        if (optionalUserAccountByUsername.isPresent()) {
            throw new ApplicationException(ErrorCode.USER_ACCOUNT_ALREADY_EXISTS);
        }

        UserAccount userAccount = userAccountMapper.toDomain(form).toBuilder()
                .password(passwordEncoder.encode(form.password()))
                .build();

        UserAccount createdUserAccount = userAccountRepository.save(userAccount);

        return jwtService.generateToken(createdUserAccount.id());
    }

    @Override
    public String login(UserAccountLoginForm form, String token) {
        reCaptchaValidator.verifyToken(token);

        UserAccount userAccount = userAccountRepository.getByEmail(form.email())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAccount.id(),
                        form.password()
                )
        );

        return jwtService.generateToken(userAccount.id());
    }

    @Override
    public UserAccountResponse getById(String id) {
        UserAccount userAccount = userAccountRepository.getById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        return userAccountMapper.toResponse(userAccount);
    }
}
