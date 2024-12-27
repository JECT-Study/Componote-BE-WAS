package ject.componote.domain.auth.dto.validate.request;

import jakarta.validation.constraints.Email;

public record MemberEmailValidateRequest(@Email String email) {
}
