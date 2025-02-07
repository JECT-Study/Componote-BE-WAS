package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class NotFoundParentCommentException extends CommentException {
    public NotFoundParentCommentException() {
        super("컴포넌트에 부모 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
