package pl.sensi.news.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityGenerator {
    public static <T> ResponseEntity<ApplicationSuccessResponse<T>> prepareSuccessResponseEntity(
            T data,
            SuccessCode successCode,
            HttpStatus status
    ) {
        return new ResponseEntity<>(new ApplicationSuccessResponse<>(
                successCode,
                data), status);
    }
}
