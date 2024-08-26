package pl.sensi.news.article;

import java.time.LocalDateTime;

public record ApplicationSuccessResponse<T>(
        String message,
        T data,
        LocalDateTime timestamp,
        String successType
) {
    public ApplicationSuccessResponse(SuccessCode successCode, T data) {
        this(successCode.getMessage(), data, LocalDateTime.now(), successCode.name());
    }

    public ApplicationSuccessResponse(SuccessCode successCode) {
        this(successCode, null);
    }
}
