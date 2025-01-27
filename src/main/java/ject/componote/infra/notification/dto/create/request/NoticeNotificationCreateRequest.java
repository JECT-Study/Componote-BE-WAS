package ject.componote.infra.notification.dto.create.request;

import ject.componote.domain.member.domain.Member;
import ject.componote.domain.notice.domain.Notice;

public record NoticeNotificationCreateRequest(NotificationParticipant participant, Long noticeId) {
    public static NoticeNotificationCreateRequest of(final Member sender, final Member receiver, final Notice notice) {
        return new NoticeNotificationCreateRequest(
                NotificationParticipant.of(sender, receiver),
                notice.getId()
        );
    }
}
