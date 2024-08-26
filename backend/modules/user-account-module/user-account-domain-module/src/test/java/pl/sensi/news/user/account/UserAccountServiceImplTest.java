package pl.sensi.news.user.account;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.jwt.api.JwtService;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountLoginForm;
import pl.sensi.news.user.account.api.models.UserAccountRegisterForm;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserAccountMapper userAccountMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private ReCaptchaValidator reCaptchaValidator;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    private UserAccountRegisterForm registerForm;

    private UserAccountLoginForm loginForm;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerForm = Instancio.of(UserAccountRegisterForm.class)
                .generate(field(UserAccountRegisterForm::email), gen -> gen.text().pattern("#c#c#c#c#c@#c#c#c.#c#c"))
                .generate(field(UserAccountRegisterForm::password), gen -> gen.string().minLength(8).maxLength(255))
                .create();

        loginForm = Instancio.of(UserAccountLoginForm.class)
                .generate(field(UserAccountLoginForm::email), gen -> gen.text().pattern("#c#c#c#c#c@#c#c#c.#c#c"))
                .generate(field(UserAccountLoginForm::password), gen -> gen.string().minLength(8).maxLength(255))
                .create();

        userAccount = Instancio.of(UserAccount.class)
                .set(field(UserAccount::email), registerForm.email())
                .set(field(UserAccount::username), registerForm.username())
                .set(field(UserAccount::password), registerForm.password())
                .create();
    }

    @Test
    public void register_shouldRegisterUserAccountIfValidFormAndUserAccountDoesNotAlreadyExists() {
        String expected = "mocked-jwt-token";

        when(userAccountRepository.getByEmail(registerForm.email())).thenReturn(Optional.empty());
        when(userAccountRepository.getByUsername(registerForm.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerForm.password())).thenReturn(registerForm.password());
        when(userAccountMapper.toDomain(registerForm)).thenReturn(userAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        when(jwtService.generateToken(userAccount.id())).thenReturn(expected);

        String result = userAccountService.register(registerForm, any(String.class));
        assertEquals(expected, result);
        verify(userAccountRepository).getByEmail(registerForm.email());
        verify(userAccountRepository).getByUsername(registerForm.username());
        verify(passwordEncoder).encode(registerForm.password());
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(jwtService).generateToken(userAccount.id());
    }

    @Test
    public void register_shouldThrowExceptionIfUserAccountAlreadyExists() {
        when(userAccountRepository.getByEmail(registerForm.email())).thenReturn(Optional.of(userAccount));

        Exception result = assertThrows(ApplicationException.class, () -> userAccountService.register(registerForm, any(String.class)));
        assertEquals(ErrorCode.USER_ACCOUNT_ALREADY_EXISTS.getMessage(), result.getMessage());
        verify(userAccountRepository, never()).getByUsername(registerForm.username());
    }

    @Test
    public void register_shouldThrowExceptionIfUsernameIsDuplicated() {
        when(userAccountRepository.getByEmail(registerForm.email())).thenReturn(Optional.empty());
        when(userAccountRepository.getByUsername(registerForm.username())).thenReturn(Optional.of(userAccount));

        Exception result = assertThrows(ApplicationException.class, () -> userAccountService.register(registerForm, any(String.class)));
        assertEquals(ErrorCode.USER_ACCOUNT_ALREADY_EXISTS.getMessage(), result.getMessage());
        verify(userAccountMapper, never()).toDomain(registerForm);
    }

    @Test
    public void login_shouldLoginUserAccountIfValidCredentialsAndUserAccountExists() {
        String expected = "mocked-jwt-token";

        when(userAccountRepository.getByEmail(loginForm.email())).thenReturn(Optional.of(userAccount));
        when(jwtService.generateToken(userAccount.id())).thenReturn(expected);

        String result = userAccountService.login(loginForm, any(String.class));
        assertEquals(expected, result);
    }

    @Test
    public void login_shouldThrowExceptionIfUserAccountDoesNotExist() {
        when(userAccountRepository.getByEmail(loginForm.email())).thenReturn(Optional.empty());

        Exception result = assertThrows(ApplicationException.class, () -> userAccountService.login(loginForm, any(String.class)));
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void login_shouldThrowExceptionIfNotValidCredentials() {
        when(userAccountRepository.getByEmail(loginForm.email())).thenReturn(Optional.of(userAccount));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> userAccountService.login(loginForm, any(String.class)));
    }

    @Test
    public void getById_shouldReturnUserAccountIfExists() {
        UserAccountResponse expected = Instancio.of(UserAccountResponse.class)
                        .set(field(UserAccountResponse::id), userAccount.id())
                        .set(field(UserAccountResponse::email), userAccount.email())
                        .set(field(UserAccountResponse::username), userAccount.username())
                        .set(field(UserAccountResponse::role), userAccount.role())
                                .create();

        when(userAccountRepository.getById(expected.id())).thenReturn(Optional.of(userAccount));
        when(userAccountMapper.toResponse(userAccount)).thenReturn(expected);

        UserAccountResponse result = userAccountService.getById(expected.id());

        assertEquals(expected, result);
    }

    @Test
    public void getById_shouldThrowExceptionIfUserAccountDoesNotExist() {
        when(userAccountRepository.getById(any(String.class))).thenReturn(Optional.empty());

        Exception result = assertThrows(ApplicationException.class, () -> userAccountService.getById(userAccount.id()));
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), result.getMessage());
    }
}