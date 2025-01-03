package ject.componote.domain.comment.dto.reply.event;

public record CommentReplyCountIncreaseEvent(Long parentId) {
    public static CommentReplyCountIncreaseEvent from(final Long parentId) {
        return new CommentReplyCountIncreaseEvent(parentId);
    }
}
