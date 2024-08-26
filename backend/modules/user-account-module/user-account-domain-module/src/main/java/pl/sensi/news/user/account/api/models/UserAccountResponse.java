package pl.sensi.news.user.account.api.models;

public record UserAccountResponse(
        String id,
        String email,
        String username,
        String role
) { }
