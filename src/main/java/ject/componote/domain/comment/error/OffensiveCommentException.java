package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class OffensiveCommentException extends CommentException {
    public OffensiveCommentException() {
        super("적절하지 않은 댓글 내용입니다.", HttpStatus.BAD_REQUEST);
    }
}
