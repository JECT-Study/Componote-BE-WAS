package ject.componote.domain.comment.dto.like.event;

import ject.componote.domain.member.model.AuthPrincipal;

public record CommentUnlikeEvent(Long commentId, Long memberId) {
    public static CommentUnlikeEvent of(final AuthPrincipal authPrincipal, final Long commentId) {
        return new CommentUnlikeEvent(commentId, authPrincipal.id());
    }
}
