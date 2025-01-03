package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class InvalidCommentContentException extends CommentException {
    public InvalidCommentContentException() {
        super("댓글 내용이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
