package ject.componote.domain.auth.dto.authorize.request;

import jakarta.validation.constraints.NotBlank;

public record MemberAuthorizeRequest(@NotBlank String providerName) {
}
