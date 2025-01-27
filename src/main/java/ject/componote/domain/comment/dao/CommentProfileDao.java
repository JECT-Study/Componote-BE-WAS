package ject.componote.domain.comment.dao;

import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.member.model.Nickname;
import ject.componote.domain.common.model.BaseImage;

public record CommentProfileDao(Long memberId, Nickname nickname, BaseImage profileImage, Job job) {
}
