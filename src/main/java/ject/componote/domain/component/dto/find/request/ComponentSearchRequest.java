package ject.componote.domain.component.dto.find.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import ject.componote.domain.component.domain.ComponentType;

import java.util.List;

public record ComponentSearchRequest(
        @NotBlank String keyword,
        @Nullable List<ComponentType> types) {
}
