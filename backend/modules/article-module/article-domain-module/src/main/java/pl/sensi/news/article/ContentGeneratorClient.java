package pl.sensi.news.article;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ContentGeneratorClient {

    @Value("${huggingface.api.url}")
    private String url;

    @Value("${huggingface.api.key}")
    private String token;

    @Bean(WebClientType.CONTENT_GENERATOR)
    public WebClient contentGeneratorClient() {

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }
}
