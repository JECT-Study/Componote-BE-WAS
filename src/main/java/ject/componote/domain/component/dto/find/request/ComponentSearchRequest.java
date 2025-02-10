package ject.componote.domain.component.dto.find.request;

import jakarta.validation.constraints.NotBlank;

public record ComponentSearchRequest(@NotBlank String keyword) {
}
