package ject.componote.domain.component.dto.event;

import ject.componote.domain.comment.domain.Comment;

public record ComponentCommentCountIncreaseEvent(Long componentId) {
    public static ComponentCommentCountIncreaseEvent from(final Comment comment) {
        return new ComponentCommentCountIncreaseEvent(comment.getComponentId());
    }
}
