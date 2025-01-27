package ject.componote.domain.member.dto.update.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberEmailUpdateRequest(@Email String email, @NotBlank String verificationCode) {
}
