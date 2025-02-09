package ject.componote.domain.component.dto.event;

import ject.componote.domain.comment.domain.Comment;

public record ComponentCommentCountDecreaseEvent(Long componentId) {
    public static ComponentCommentCountDecreaseEvent from(final Comment comment) {
        return new ComponentCommentCountDecreaseEvent(comment.getComponentId());
    }
}
