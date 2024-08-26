package pl.sensi.news.jwt.api.models;

import lombok.Builder;

@Builder
public record Jwt(
        String id,
        String tokenId,
        String userAccountId
) { }
