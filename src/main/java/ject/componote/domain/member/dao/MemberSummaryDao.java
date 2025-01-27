package ject.componote.domain.member.dao;

import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;

public record MemberSummaryDao(Email email, Nickname nickname, ProfileImage profileImage) {
}
