package pl.sensi.news.user.account.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pl.sensi.news.article.ValidationMessageConst;

public record UserAccountRegisterForm(
        @NotBlank(message = ValidationMessageConst.NO_EMAIL) @Email String email,
        @NotBlank(message = ValidationMessageConst.NO_USERNAME) String username,
        @NotBlank(message = ValidationMessageConst.NO_PASSWORD) @Size(min = 8, max=255) String password
) { }
