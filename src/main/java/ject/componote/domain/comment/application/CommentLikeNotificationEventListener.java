package ject.componote.domain.comment.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.error.NotFoundMemberException;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.like.event.CommentLikeNotificationEvent;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.infra.notification.application.NotificationProducer;
import ject.componote.infra.notification.dto.create.request.CommentLikeNotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommentLikeNotificationEventListener {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NotificationProducer notificationProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeNotificationEvent(final CommentLikeNotificationEvent event) {
        final Member sender = findMemberById(event.senderId());
        final Member receiver = findMemberById(event.receiverId());
        final Comment comment = findCommentById(event.commentId()); // 추후, 댓글 내용을 알림에 포함할 수 있으므로 SELECT 수행
        notificationProducer.sendCommentLikeNotification(
                CommentLikeNotificationCreateRequest.of(sender, receiver, comment)
        );
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
