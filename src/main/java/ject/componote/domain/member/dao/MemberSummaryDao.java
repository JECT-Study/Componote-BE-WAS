package ject.componote.domain.member.dao;

import ject.componote.domain.member.model.Email;
import ject.componote.domain.member.model.Nickname;
import ject.componote.domain.member.model.ProfileImage;

public record MemberSummaryDao(Email email, Nickname nickname, ProfileImage profileImage) {
}
