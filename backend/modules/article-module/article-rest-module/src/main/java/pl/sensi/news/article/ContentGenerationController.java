package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sensi.news.article.api.ContentGenerationService;
import pl.sensi.news.article.api.models.ContentGenerationRequest;

@RestController
@RequestMapping(EndpointConst.GENERATE)
@RequiredArgsConstructor
public class ContentGenerationController {

    private final ContentGenerationService contentGenerationService;

    @PostMapping
    public ResponseEntity<ApplicationSuccessResponse<String>> generate(@RequestBody ContentGenerationRequest request, @RequestHeader("Accept-Language") String lang) {
        String generatedText = contentGenerationService.generate(request, lang);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                generatedText,
                SuccessCode.CONTENT_GENERATED,
                HttpStatus.OK
        );
    }
}
