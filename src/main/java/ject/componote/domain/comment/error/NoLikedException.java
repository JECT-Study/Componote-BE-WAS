package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class NoLikedException extends CommentException {
    public NoLikedException(final Long commentId, final Long memberId) {
        super("해당 댓글에 좋아요를 누르지 않았습니다. 댓글 ID:" + commentId + ", 회원 ID: " + memberId, HttpStatus.BAD_REQUEST);
    }
}
