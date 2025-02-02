package ject.componote.domain.auth.dto.update.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberEmailUpdateRequest(@Email String email, @NotBlank String verificationCode) {
}
