package pl.sensi.news.article;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final int errorCode;
    private final String errorType;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.errorType = errorCode.name();
    }
}
