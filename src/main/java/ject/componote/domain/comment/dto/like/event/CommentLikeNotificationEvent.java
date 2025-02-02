package ject.componote.domain.comment.dto.like.event;

import ject.componote.domain.comment.domain.Comment;

public record CommentLikeNotificationEvent(Long senderId, Long receiverId, Long commentId) {
    public static CommentLikeNotificationEvent of(final Comment comment, final Long senderId) {
        return new CommentLikeNotificationEvent(senderId, comment.getMemberId(), comment.getId());
    }
}
