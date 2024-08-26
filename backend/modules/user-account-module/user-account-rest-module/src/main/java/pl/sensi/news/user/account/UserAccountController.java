package pl.sensi.news.user.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sensi.news.article.ApplicationSuccessResponse;
import pl.sensi.news.article.EndpointConst;
import pl.sensi.news.article.ReCaptchaConstants;
import pl.sensi.news.article.ResponseEntityGenerator;
import pl.sensi.news.article.SuccessCode;
import pl.sensi.news.security.SecurityUtils;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccountRegisterForm;
import pl.sensi.news.user.account.api.models.UserAccountLoginForm;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

@RestController
@RequestMapping(EndpointConst.USERS)
@RequiredArgsConstructor
class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping(EndpointConst.ID)
    public UserAccountResponse getById(@PathVariable String id) {
        return userAccountService.getById(id);
    }

    @GetMapping(EndpointConst.USERS_CURRENT)
    public UserAccountResponse getCurrent() {
        return userAccountService.getById(SecurityUtils.getLoggedInUserId());
    }

    @PostMapping(EndpointConst.USERS_REGISTER)
    public ResponseEntity<ApplicationSuccessResponse<String>> register(@RequestBody UserAccountRegisterForm form, @RequestHeader(name = ReCaptchaConstants.HEADER) String reCaptchaToken) {
        String token = userAccountService.register(form, reCaptchaToken);
        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                token,
                SuccessCode.USER_ACCOUNT_CREATED,
                HttpStatus.CREATED
        );
    }

    @PostMapping(EndpointConst.USERS_LOGIN)
    public ResponseEntity<ApplicationSuccessResponse<String>> login(@RequestBody UserAccountLoginForm form, @RequestHeader(name = ReCaptchaConstants.HEADER) String reCaptchaToken) {
        String token = userAccountService.login(form, reCaptchaToken);
        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                token,
                SuccessCode.USER_ACCOUNT_AUTHENTICATED,
                HttpStatus.OK
        );
    }
}
