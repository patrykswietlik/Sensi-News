package pl.sensi.news.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    ARTICLE_CREATED("Article was created", 201),
    ARTICLE_DELETED("Article was deleted", 200),
    ARTICLE_UPDATED("Article was updated", 200),
    CONTENT_GENERATED("Content was generated", 200),
    TAG_CREATED("Tag was created", 201),
    TAG_DELETED("Tag was deleted", 200),
    COMMENT_CREATED("Comment was created", 201),
    COMMENT_DELETED("Comment was deleted", 200),
    COMMENT_UPDATED("Comment was updated", 200),
    RATING_CREATED("Rating was created, article was liked", 201),
    RATING_DELETED("Rating was deleted, article was disliked", 200),
    USER_ACCOUNT_CREATED("User account was created", 201),
    USER_ACCOUNT_AUTHENTICATED("User account was authenticated", 200);

    private final String message;
    private final int code;

}
