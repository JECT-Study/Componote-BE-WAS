package ject.componote.domain.auth.dto.verify.request;

import jakarta.validation.constraints.Email;

public record MemberSendVerificationCodeRequest(@Email String email) {
}
