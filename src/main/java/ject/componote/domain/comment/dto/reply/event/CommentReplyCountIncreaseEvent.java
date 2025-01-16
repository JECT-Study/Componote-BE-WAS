package ject.componote.domain.comment.dto.reply.event;

import ject.componote.domain.comment.domain.Comment;

public record CommentReplyCountIncreaseEvent(Long parentId) {
    public static CommentReplyCountIncreaseEvent from(final Comment comment) {
        return new CommentReplyCountIncreaseEvent(comment.getParentId());
    }
}
