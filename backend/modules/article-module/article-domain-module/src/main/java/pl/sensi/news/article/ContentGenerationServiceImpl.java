package pl.sensi.news.article;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sensi.news.article.api.ContentGenerationService;
import pl.sensi.news.article.api.models.ContentGenerationRequest;
import reactor.core.publisher.Mono;

@Service
public class ContentGenerationServiceImpl implements ContentGenerationService {

    @Value("${deep-translate.api.pl}")
    private String polish;

    @Value("${deep-translate.api.en}")
    private String english;

    private final WebClient contentGeneratorClient;

    private final WebClient translatorClient;

    @Autowired
    public ContentGenerationServiceImpl(
            @Qualifier(WebClientType.CONTENT_GENERATOR) WebClient contentGeneratorClient,
            @Qualifier(WebClientType.TRANSLATOR) WebClient translatorClient
    ) {
        this.contentGeneratorClient = contentGeneratorClient;
        this.translatorClient = translatorClient;
    }

    @Override
    public String generate(ContentGenerationRequest request, String lang) {
        String input = request.input();
        boolean toPolish = lang.equals("pl");

        if (toPolish) {
            input = translate(input, polish, english);
        } 

        JSONObject requestBody = prepareContentGenerationRequestBody(input);

        try {
            Mono<String> response = contentGeneratorClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody.toString())
                    .retrieve()
                    .bodyToMono(String.class);

            JSONArray responseArray = new JSONArray(response.block());

            if (responseArray.isEmpty()) {
                throw new ApplicationException(ErrorCode.CONTENT_GENERATION_FAILED);
            }

            return prepareContentGenerationResponse(responseArray, toPolish);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.CONTENT_GENERATION_FAILED);
        }
    }

    private JSONObject prepareContentGenerationRequestBody(String input) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("inputs", input);
        requestBody.put("wait_for_model", "true");
        return requestBody;
    }

    private String prepareContentGenerationResponse(JSONArray responseArray, boolean toPolish) {
        JSONObject response = responseArray.getJSONObject(0);
        String plainText = response.optString("generated_text", ErrorCode.CONTENT_TRANSLATION_FAILED.getMessage()).replace("\n\n", " ");

        if (toPolish) {
            return translate(plainText, english, polish);
        }

        return plainText;
    }

    private String translate(String text, String source, String target) {

        JSONObject requestBody = prepareTranslationRequestBody(text, source, target);

        try {
            Mono<String> response = translatorClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody.toString())
                    .retrieve()
                    .bodyToMono(String.class);

            JSONObject responseObject = new JSONObject(response.block());

            if (responseObject.isEmpty()) {
                throw new ApplicationException(ErrorCode.CONTENT_TRANSLATION_FAILED);
            }

            return prepareTranslationResponse(responseObject);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.CONTENT_TRANSLATION_FAILED);
        }
    }

    private JSONObject prepareTranslationRequestBody(String text, String source, String target) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", text);
        requestBody.put("source", source);
        requestBody.put("target", target);
        return requestBody;
    }

    private String prepareTranslationResponse(JSONObject responseObject) {
        return responseObject.getJSONObject("data").getJSONObject("translations").getString("translatedText");
    }
}
