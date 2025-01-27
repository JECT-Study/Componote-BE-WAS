package ject.componote.domain.comment.dto.like.event;

import ject.componote.domain.member.model.AuthPrincipal;

public record CommentLikeEvent(Long commentId, Long memberId) {
    public static CommentLikeEvent of(final AuthPrincipal authPrincipal, final Long commentId) {
        return new CommentLikeEvent(commentId, authPrincipal.id());
    }
}
