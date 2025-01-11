package ject.componote.domain.auth.dto.verify.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberEmailVerifyRequest(@Email String email, @NotBlank String verificationCode) {
}
