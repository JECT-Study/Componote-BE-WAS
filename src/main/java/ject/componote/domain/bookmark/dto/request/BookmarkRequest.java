package ject.componote.domain.bookmark.dto.request;

import com.google.firebase.database.annotations.NotNull;

public record BookmarkRequest(
    @NotNull Long id,
    @NotNull String type // "component" or "designSystem"
) {}