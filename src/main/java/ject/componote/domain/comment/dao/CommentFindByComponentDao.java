package ject.componote.domain.comment.dao;

import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.Count;

import java.time.LocalDateTime;

public record CommentFindByComponentDao(Long memberId, Nickname nickname, BaseImage profileImage, Job job, Long commentId, Long parentId, BaseImage image, CommentContent content, LocalDateTime createdAt, Count likeCount, boolean isLiked, boolean isReply) {
}
