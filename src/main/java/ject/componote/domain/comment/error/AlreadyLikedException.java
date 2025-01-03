package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class AlreadyLikedException extends CommentException {
    public AlreadyLikedException(final Long commentId, final Long memberId) {
        super("이미 좋아요를 누른 댓글입니다. 댓글 ID " + commentId + ", 회원 ID: " + memberId, HttpStatus.BAD_REQUEST);
    }
}
