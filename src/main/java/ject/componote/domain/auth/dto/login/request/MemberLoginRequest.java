package ject.componote.domain.auth.dto.login.request;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank String providerType,
        @NotBlank String code) {
}
