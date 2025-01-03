package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class NotFoundCommentException extends CommentException {
    public NotFoundCommentException(final Long commentId) {
        super("댓글을 찾을 수 없습니다. 댓글 ID: " + commentId, HttpStatus.NOT_FOUND);
    }

    public NotFoundCommentException(final Long commentId, final Long memberId) {
        super("댓글을 찾을 수 없습니다. 댓글 ID: " + commentId + ", 회원 ID: " + memberId, HttpStatus.NOT_FOUND);
    }
}
