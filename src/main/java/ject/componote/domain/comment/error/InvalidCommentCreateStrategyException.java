package ject.componote.domain.comment.error;

import org.springframework.http.HttpStatus;

public class InvalidCommentCreateStrategyException extends CommentException {
    public InvalidCommentCreateStrategyException() {
        super("댓글 생성에 실패하였습니다. 요청 Body 확인해주세요.", HttpStatus.BAD_REQUEST);
    }
}
