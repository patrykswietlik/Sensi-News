package pl.sensi.news.user.account.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.user.account.api.models.UserAccountRegisterForm;
import pl.sensi.news.user.account.api.models.UserAccountLoginForm;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

@Validated
public interface UserAccountService {

    String register(@Valid UserAccountRegisterForm form, @NotBlank String token);
    String login(@Valid UserAccountLoginForm form, @NotBlank String token);
    UserAccountResponse getById(String id);
}
