package ject.componote.domain.comment.application;

import ject.componote.domain.member.dao.MemberRepository;
import ject.componote.domain.member.domain.Member;
import ject.componote.domain.member.error.NotFoundMemberException;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.reply.event.CommentReplyNotificationEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.infra.notification.application.NotificationProducer;
import ject.componote.infra.notification.dto.create.request.NestedReplyNotificationCreateRequest;
import ject.componote.infra.notification.dto.create.request.RootReplyNotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentReplyNotificationEventListener {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NotificationProducer notificationProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReplyNotificationEvent(final CommentReplyNotificationEvent event) {
        final Comment parent = findCommentById(event.parentId());
        final Long receiverId = parent.getMemberId();
        final Long senderId = event.senderId();
        if (isSelfReply(receiverId, senderId)) {
            return;
        }

        final Member receiver = findMemberById(receiverId);
        if (!receiver.hasEmail()) {
            return;
        }

        final Member sender = findMemberById(senderId);
        if (commentRepository.isRootComment(parent.getId())) {
            notificationProducer.sendRootReplyNotification(RootReplyNotificationCreateRequest.of(sender, receiver, parent));
            return;
        }

        notificationProducer.sendNestedReplyNotification(NestedReplyNotificationCreateRequest.of(sender, receiver, parent));
    }

    private boolean isSelfReply(final Long receiverId, final Long senderId) {
        return receiverId.equals(senderId);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(commentId));
    }

    private Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> NotFoundMemberException.createWhenInvalidMemberId(memberId));
    }
}
