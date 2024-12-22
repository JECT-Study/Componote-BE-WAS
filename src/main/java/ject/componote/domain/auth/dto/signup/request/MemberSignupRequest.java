package ject.componote.domain.auth.dto.signup.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ject.componote.domain.auth.domain.Member;

public record MemberSignupRequest(
        @NotBlank String nickname,
        @NotBlank String job,
        @Nullable String profileImageObjectKey,
        @NotNull Long socialAccountId) {
    public Member toMember() {
        return Member.of(
                nickname,
                job,
                profileImageObjectKey,
                socialAccountId
        );
    }
}
