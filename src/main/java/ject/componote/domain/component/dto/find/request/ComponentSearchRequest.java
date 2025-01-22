package ject.componote.domain.component.dto.find.request;

import jakarta.annotation.Nullable;
import ject.componote.domain.component.domain.ComponentType;

import java.util.List;

public record ComponentSearchRequest(
        String keyword,
        @Nullable List<ComponentType> types) {
}
