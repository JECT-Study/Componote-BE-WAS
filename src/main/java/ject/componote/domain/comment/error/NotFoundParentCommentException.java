package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class NotFoundParentCommentException extends CommentException {
    public NotFoundParentCommentException(final Long parentId) {
        super("일치하는 부모 댓글을 찾을 수 없습니다. 댓글 ID: " + parentId, HttpStatus.NOT_FOUND);
    }
}
