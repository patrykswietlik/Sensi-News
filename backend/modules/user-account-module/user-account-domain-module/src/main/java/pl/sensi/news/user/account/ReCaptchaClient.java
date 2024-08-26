package pl.sensi.news.user.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sensi.news.article.WebClientType;

@Configuration
public class ReCaptchaClient {

    @Value("${recaptcha.api.url}")
    private String url;

    @Bean(WebClientType.RE_CAPTCHA)
    public WebClient reCaptchaWebClient() {

        return WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
