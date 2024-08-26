package pl.sensi.news.article;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Configuration
public class TranslatorClient {

    @Value("${deep-translate.api.url}")
    private String url;

    @Value("${deep-translate.api.host}")
    private String host;

    @Value("${deep-translate.api.key}")
    private String token;

    @Bean(WebClientType.TRANSLATOR)
    public WebClient translatorClient() {
        Consumer<HttpHeaders> headers = httpHeaders -> {
            httpHeaders.add("x-rapidapi-key", token);
            httpHeaders.add("x-rapidapi-host", host);
        };

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(headers)
                .build();
    }
}
