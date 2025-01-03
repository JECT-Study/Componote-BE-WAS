package ject.componote.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(
    @NotNull Long id,
    @NotNull String type // "component" or "designSystem"
) {}