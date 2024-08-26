package pl.sensi.news.user.account.api.models;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record UserAccount(
        String id,
        String email,
        String username,
        String password,
        String role,
        LocalDateTime createdAt
) { }
