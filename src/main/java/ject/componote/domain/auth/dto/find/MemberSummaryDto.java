package ject.componote.domain.auth.dto.find;

import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.common.model.Image;

public record MemberSummaryDto(Nickname nickname, Image profilePhoto) {
}
