package ject.componote.domain.comment.dto.create.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @Nullable String image,
        @NotBlank String content,
        @NotNull Long componentId,
        @Nullable Long parentId
) {
}
