package ject.componote.domain.design.dto.search.request;

import jakarta.annotation.Nullable;
import java.util.List;

public record DesignSystemSummaryRequest(@Nullable List<String> filters) {
}

