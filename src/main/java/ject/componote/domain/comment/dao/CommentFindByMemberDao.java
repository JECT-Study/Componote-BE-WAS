package ject.componote.domain.comment.dao;

import ject.componote.domain.comment.model.CommentContent;

import java.time.LocalDateTime;

public record CommentFindByMemberDao(Long id, Long parentId, String componentTitle, CommentContent parentContent, CommentContent content, LocalDateTime createdAt, boolean isReply) {
}
