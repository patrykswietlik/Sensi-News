package pl.sensi.news.user.account;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.article.WebClientType;
import reactor.core.publisher.Mono;

@Service
public class ReCaptchaValidator {

    @Value("${recaptcha.api.secret}")
    private String secret;

    private final WebClient reCaptchaWebClient;

    @Autowired
    public ReCaptchaValidator(@Qualifier(WebClientType.RE_CAPTCHA) final WebClient reCaptchaWebClient) {
        this.reCaptchaWebClient = reCaptchaWebClient;
    }

    public void verifyToken(String token) {
        Mono<String> response = reCaptchaWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("secret", secret)
                        .queryParam("response", token)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        JSONObject responseObject = new JSONObject(response.block());

        if (!responseObject.getBoolean("success")) {
            throw new ApplicationException(ErrorCode.RE_CAPTCHA_TOKEN_INVALID);
        }
    }
}
