package pl.sensi.news.user.account.api.models;

import jakarta.validation.constraints.NotBlank;
import pl.sensi.news.article.ValidationMessageConst;

public record UserAccountLoginForm(
        @NotBlank(message = ValidationMessageConst.NO_EMAIL) String email,
        @NotBlank(message = ValidationMessageConst.NO_PASSWORD) String password
) { }
