package ject.componote.infra.notification.dto.create.request;

import ject.componote.domain.auth.domain.Member;

public record NotificationParticipant (
        Long senderId,
        String senderNickname,
        Long receiverId,
        String receiverNickname,
        String receiverEmail
) {
    public static NotificationParticipant of(final Member sender, final Member receiver) {
        return new NotificationParticipant(
                sender.getId(),
                sender.getNickname().getValue(),
                receiver.getId(),
                receiver.getNickname().getValue(),
                receiver.getEmail().getValue()
        );
    }
}
