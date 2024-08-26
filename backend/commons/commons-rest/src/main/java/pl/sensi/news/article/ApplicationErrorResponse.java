package pl.sensi.news.article;

import java.time.LocalDateTime;

public record ApplicationErrorResponse(
        String message,
        LocalDateTime timestamp,
        String errorType
) {
}
