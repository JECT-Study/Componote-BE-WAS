package ject.componote.domain.comment.dto.update.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(@Nullable String imageObjectKey,
                                   @NotBlank String content) {
}
