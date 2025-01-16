package ject.componote.domain.comment.dto.reply.event;

import ject.componote.domain.comment.domain.Comment;

public record CommentReplyNotificationEvent(Long senderId, Long parentId, Long commentId) {
    public static CommentReplyNotificationEvent from(final Comment reply) {
        return new CommentReplyNotificationEvent(reply.getMemberId(), reply.getParentId(), reply.getId());
    }
}
