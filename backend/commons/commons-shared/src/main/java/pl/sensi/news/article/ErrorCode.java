package pl.sensi.news.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ARTICLE_NOT_FOUND("Article was not found", 404),
    ARTICLE_HAS_NO_TAGS("Article must have at least one tag", 400),
    ARTICLE_STATUS_NOT_FOUND("Article status was not found", 404),
    IMAGE_UPLOAD_FAILED("Image upload failed", 500),
    CONTENT_GENERATION_FAILED("Content generation failed", 500),
    CONTENT_TRANSLATION_FAILED("Content translation failed", 500),
    TAG_NOT_FOUND("Tag was not found", 404),
    TAG_WAS_DELETED("Tag was deleted", 404),
    TAG_ALREADY_EXISTS("Tag already exists", 400),
    TAG_CANNOT_BE_DELETED("Tag cannot be deleted", 400),
    COMMENT_NOT_FOUND("Comment was not found", 404),
    CANNOT_REPLY_ON_REPLY("Cannot reply on reply", 400),
    USER_ACCOUNT_ALREADY_EXISTS("User account already exists", 400),
    USER_NOT_FOUND("User was not found", 404),
    JWT_MALFORMED("Token was malformed", 401),
    JWT_INVALID("Token is invalid", 401),
    JWT_EXPIRED("Token is expired", 401),
    UNAUTHORIZED("Unauthorized", 401),
    RE_CAPTCHA_TOKEN_INVALID("Re-captcha token is invalid", 401);

    private final String message;
    private final int code;

}
