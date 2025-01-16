package ject.componote.domain.comment.dto.reply.event;

import ject.componote.domain.comment.domain.Comment;

public record CommentReplyNotificationEvent(Long senderId, Long parentId, Long commentId) {
    public static CommentReplyNotificationEvent from(final Comment comment) {
        return new CommentReplyNotificationEvent(comment.getMemberId(), comment.getParentId(), comment.getId());
    }
}
