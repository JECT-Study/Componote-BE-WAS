package ject.componote.domain.auth.dto.signup.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ject.componote.domain.auth.domain.Member;

public record MemberSignupRequest(
        @Email String email,
        @NotBlank String nickname,
        @NotBlank String job,
        @Nullable String profileImageTempKey,
        @NotNull Long socialAccountId) {
    public Member toMember(final Long socialAccountId, final String permanentKey) {
        return Member.of(nickname, email, job, permanentKey, socialAccountId);
    }
}
