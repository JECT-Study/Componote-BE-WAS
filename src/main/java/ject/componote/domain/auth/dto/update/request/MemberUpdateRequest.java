package ject.componote.domain.auth.dto.update.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ject.componote.domain.auth.domain.Job;

public record MemberUpdateRequest(
        @NotBlank String nickname,
        @NotNull Job job,
        @Nullable String profileImageObjectKey
        ) {
}
