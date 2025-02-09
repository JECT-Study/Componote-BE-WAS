package ject.componote.domain.comment.dto.image.event;

import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.model.CommentImage;

public record CommentImageMoveEvent(CommentImage image) {
    public static CommentImageMoveEvent from(final Comment comment) {
        return new CommentImageMoveEvent(comment.getImage());
    }
}
