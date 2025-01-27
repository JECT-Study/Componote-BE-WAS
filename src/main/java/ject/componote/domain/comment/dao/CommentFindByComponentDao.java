package ject.componote.domain.comment.dao;

import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.member.model.Nickname;
import ject.componote.domain.member.model.ProfileImage;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.CommentImage;
import ject.componote.domain.common.model.Count;

import java.time.LocalDateTime;

public record CommentFindByComponentDao(
        Long memberId,
        Nickname nickname,
        ProfileImage profileImage,
        Job job,
        Long commentId,
        Long parentId,
        CommentImage commentImage,
        CommentContent content,
        LocalDateTime createdAt,
        Count likeCount,
        Count replyCount,
        boolean isLiked,
        boolean isReply) {
}
