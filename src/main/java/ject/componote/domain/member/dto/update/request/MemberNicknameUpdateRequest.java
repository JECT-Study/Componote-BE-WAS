package ject.componote.domain.member.dto.update.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNicknameUpdateRequest(@NotBlank String nickname) {
}
