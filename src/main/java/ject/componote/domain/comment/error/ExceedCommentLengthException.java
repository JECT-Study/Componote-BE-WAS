package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class ExceedCommentLengthException extends CommentException{
    public ExceedCommentLengthException(final int length) {
        super("댓글 길이가 너무 깁니다. 현재 길이: " + length, HttpStatus.BAD_REQUEST);
    }
}
