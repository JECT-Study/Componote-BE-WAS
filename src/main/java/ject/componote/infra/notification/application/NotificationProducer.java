package ject.componote.infra.notification.application;

import ject.componote.infra.notification.dto.create.request.CommentLikeNotificationCreateRequest;
import ject.componote.infra.notification.dto.create.request.NestedReplyNotificationCreateRequest;
import ject.componote.infra.notification.dto.create.request.NoticeNotificationCreateRequest;
import ject.componote.infra.notification.dto.create.request.RootReplyNotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendRootReplyNotification(final RootReplyNotificationCreateRequest request) {
        rabbitTemplate.convertAndSend("was-notification", "notification.create", request);
    }

    public void sendNestedReplyNotification(final NestedReplyNotificationCreateRequest request) {
        rabbitTemplate.convertAndSend("was-notification", "notification.create", request);   // 라우팅 키, exchange 설정 필요
    }

    public void sendNoticeNotification(final NoticeNotificationCreateRequest request) {
        rabbitTemplate.convertAndSend("was-notification", "notification.create", request);   // 라우팅 키, exchange 설정 필요
    }

    public void sendCommentLikeNotification(final CommentLikeNotificationCreateRequest request) {
        rabbitTemplate.convertAndSend("was-notification", "notification.create", request);   // 라우팅 키, exchange 설정 필요
    }
}
