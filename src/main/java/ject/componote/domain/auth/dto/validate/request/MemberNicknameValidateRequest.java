package ject.componote.domain.auth.dto.validate.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNicknameValidateRequest(@NotBlank String nickname) {
}
