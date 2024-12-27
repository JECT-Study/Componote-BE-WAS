package ject.componote.domain.auth.dto.update.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNicknameUpdateRequest(@NotBlank String nickname) {
}
