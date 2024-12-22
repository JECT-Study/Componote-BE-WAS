package ject.componote.domain.auth.dao;

import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;

public record MemberSummaryDao(Nickname nickname, ProfileImage profileImage) {
}
