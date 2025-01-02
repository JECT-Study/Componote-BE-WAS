package ject.componote.domain.comment.dao;

import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.CommentImage;
import ject.componote.domain.common.model.Count;

import java.time.LocalDateTime;

public record CommentFindByParentDao(
        Long memberId,
        Nickname nickname,
        ProfileImage profileImage,
        Job job,
        Long commentId,
        CommentImage commentImage,
        CommentContent content,
        LocalDateTime createdAt,
        Count likeCount,
        boolean isLiked) {
}
