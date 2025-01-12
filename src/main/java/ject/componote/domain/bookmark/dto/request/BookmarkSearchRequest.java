package ject.componote.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkSearchRequest(
    @NotNull String type,
    @NotNull String sortType
) {}