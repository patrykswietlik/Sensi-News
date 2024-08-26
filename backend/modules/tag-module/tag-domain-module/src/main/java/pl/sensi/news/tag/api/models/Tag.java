package pl.sensi.news.tag.api.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Tag(
        String id,
        String engName,
        String plName,
        LocalDateTime deletedOn,
        int totalArticles,
        boolean removable
)
{ }
