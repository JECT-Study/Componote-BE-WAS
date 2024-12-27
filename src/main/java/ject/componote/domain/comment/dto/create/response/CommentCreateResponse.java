package ject.componote.domain.comment.dto.create.response;

import ject.componote.domain.comment.domain.Comment;

public record CommentCreateResponse(Long id) {
    public static CommentCreateResponse from(final Comment comment) {
        return new CommentCreateResponse(comment.getId());
    }
}
