package ject.componote.domain.auth.dto.signup.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberSignupRequest(
        @Email String email,
        @NotBlank String nickname,
        @NotBlank String job,
        @Nullable String profileImage,
        @NotBlank Long socialAccountId) {
}
