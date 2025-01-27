package ject.componote.infra.notification.dto.create.request;

import ject.componote.domain.member.domain.Member;
import ject.componote.domain.comment.domain.Comment;

public record CommentLikeNotificationCreateRequest(NotificationParticipant participant, Long commentId) {
    public static CommentLikeNotificationCreateRequest of(final Member sender, final Member receiver, final Comment comment) {
        return new CommentLikeNotificationCreateRequest(
                NotificationParticipant.of(sender, receiver),
                comment.getId()
        );
    }
}
