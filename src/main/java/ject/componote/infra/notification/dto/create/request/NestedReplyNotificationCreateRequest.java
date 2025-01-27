package ject.componote.infra.notification.dto.create.request;

import ject.componote.domain.member.domain.Member;
import ject.componote.domain.comment.domain.Comment;

public record NestedReplyNotificationCreateRequest(NotificationParticipant participant, Long commentId) {
    public static NestedReplyNotificationCreateRequest of(final Member sender, final Member receiver, final Comment comment) {
        return new NestedReplyNotificationCreateRequest(
                NotificationParticipant.of(sender, receiver),
                comment.getId()
        );
    }
}
