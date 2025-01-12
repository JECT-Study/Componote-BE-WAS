package ject.componote.domain.auth.dto.verify.request;

import jakarta.validation.constraints.Email;

public record MemberEmailVerificationRequest(@Email String email) {
}
