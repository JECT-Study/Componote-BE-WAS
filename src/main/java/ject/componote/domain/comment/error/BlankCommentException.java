package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class BlankCommentException extends CommentException {
    public BlankCommentException() {
        super("댓글 내용이 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
